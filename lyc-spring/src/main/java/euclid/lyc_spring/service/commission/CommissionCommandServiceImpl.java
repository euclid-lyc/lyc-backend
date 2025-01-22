package euclid.lyc_spring.service.commission;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ChatHandler;
import euclid.lyc_spring.apiPayload.exception.handler.CommissionHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.CommissionClothes;
import euclid.lyc_spring.domain.chat.Message;
import euclid.lyc_spring.domain.chat.Schedule;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.commission_style.*;
import euclid.lyc_spring.domain.chat.commission.commission_info.*;
import euclid.lyc_spring.domain.enums.MessageCategory;
import euclid.lyc_spring.domain.mapping.MemberChat;
import euclid.lyc_spring.dto.request.CommissionRequestDTO;
import euclid.lyc_spring.dto.request.InfoDTO;
import euclid.lyc_spring.dto.request.StyleDTO;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import euclid.lyc_spring.dto.response.CommissionDTO;
import euclid.lyc_spring.repository.*;
import euclid.lyc_spring.repository.commission.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static euclid.lyc_spring.domain.enums.CommissionStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommissionCommandServiceImpl implements CommissionCommandService {

    private final MemberRepository memberRepository;
    private final CommissionRepository commissionRepository;
    private final ChatRepository chatRepository;
    private final ScheduleRepository scheduleRepository;

    private final MemberChatRepository memberChatRepository;

    private final CommissionClothesRepository commissionClothesRepository;

    private final CommissionBodyTypeRepository commissionBodyTypeRepository;
    private final CommissionFitRepository commissionFitRepository;
    private final CommissionMaterialRepository commissionMaterialRepository;
    private final CommissionStyleRepository commissionStyleRepository;

    private final CommissionHopeColorRepository commissionHopeColorRepository;
    private final CommissionHopeFitRepository commissionHopeFitRepository;
    private final CommissionHopeMaterialRepository commissionHopeMaterialRepository;
    private final CommissionHopeStyleRepository commissionHopeStyleRepository;

    @Transactional
    public CommissionDTO.CommissionViewDTO writeCommission(CommissionRequestDTO.CommissionDTO commissionRequestDTO) {

        Member member = Authorization();
        Member director = memberRepository.findById(commissionRequestDTO.getDirectorId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 셀프 의뢰 금지
        if(member.equals(director)) {
            throw new CommissionHandler(ErrorStatus.DIRECTOR_EQUAL_MEMBER);
        }

        // 진행중인 의뢰가 있을 때에는 새로운 의뢰 불가능
        if (commissionRepository.findByDirectorIdAndMemberIdAndStatusNot(
                director.getId(), member.getId(), TERMINATED).isPresent()) {
            throw new CommissionHandler(ErrorStatus.COMMISSION_ALREADY_EXISTS);
        }
        if (commissionRepository.findByDirectorIdAndMemberIdAndStatusNot(
                member.getId(), director.getId(), TERMINATED).isPresent()) {
            throw new CommissionHandler(ErrorStatus.COMMISSION_ALREADY_EXISTS);
        }

        InfoDTO.BasicInfoDTO basicInfoDTO = commissionRequestDTO.getBasicInfo();
        StyleDTO.StyleInfoDTO styleInfoDTO = commissionRequestDTO.getStyle();
        InfoDTO.OtherMattersDTO otherMattersDTO = commissionRequestDTO.getOtherMatters();

        Commission commission = Commission.builder()
                .status(REQUIRED)
                .createdAt(LocalDateTime.now())
                .member(member)
                .director(director)
                .height(basicInfoDTO.getHeight())
                .weight(basicInfoDTO.getWeight())
                .topSize(basicInfoDTO.getTopSize())
                .bottomSize(basicInfoDTO.getBottomSize())
                .text(basicInfoDTO.getText())
                .dateToUse(otherMattersDTO.getDateToUse())
                .desiredDate(otherMattersDTO.getDesiredDate())
                .minPrice(otherMattersDTO.getMinPrice())
                .maxPrice(otherMattersDTO.getMaxPrice())
                .freeText(otherMattersDTO.getText())
                .isShared(otherMattersDTO.getIsShared())
                .build();

        commission = commissionRepository.save(commission);

        createCommissionHopeStyle(commission, styleInfoDTO.getStyleList());
        createCommissionHopeFit(commission, styleInfoDTO.getFitList());
        createCommissionHopeMaterial(commission, styleInfoDTO.getMaterialList());
        createCommissionStyleColor(commission, styleInfoDTO.getColorList());

        createCommissionStyle(commission, basicInfoDTO.getInfoStyle());
        createCommissionFit(commission, basicInfoDTO.getInfoFit());
        createCommissionMaterial(commission, basicInfoDTO.getInfoMaterial());
        createCommissionBodyType(commission, basicInfoDTO.getInfoBodyType());

        return CommissionDTO.CommissionViewDTO.toDTO(commission);
    }

    @Override
    public CommissionDTO.ClothesViewDTO saveCommissionedClothes(Long chatId, CommissionRequestDTO.ClothesDTO clothesRequestDTO) {

        Member member = Authorization();
        Chat chat = chatRepository.findById(chatId).
                orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));
        Commission commission = commissionRepository.findByChat(chat).
                orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        if(!commission.getDirector().getId().equals(member.getId()))
            throw new CommissionHandler(ErrorStatus.DIRECTOR_NOT_EQUAL_MEMBER);

        if(chat.getSavedClothesCount()==9)
            throw new CommissionHandler(ErrorStatus.COMMISSION_CLOTHES_NOT_SAVED);

        CommissionClothes commissionClothes = CommissionClothes.builder()
                .chat(chat)
                .image(clothesRequestDTO.getImage())
                .url(clothesRequestDTO.getUrl())
                .build();

        commissionClothesRepository.save(commissionClothes);
        chat.setSavedClothesCount(chat.getSavedClothesCount()+1);

        return CommissionDTO.ClothesViewDTO.toDTO(commissionClothes);
    }

    @Override
    public CommissionDTO.ClothesViewDTO deleteCommissionedClothes(Long chatId, Long clothesId) {

        Member member = Authorization();
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));
        Commission commission = commissionRepository.findByChat(chat)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));
        CommissionClothes clothes = commissionClothesRepository.findCommissionClothesByIdAndChat(clothesId,chat)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_CLOTHES_NOT_FOUND));

        if(!commission.getDirector().getId().equals(member.getId()))
            throw new CommissionHandler(ErrorStatus.DIRECTOR_NOT_EQUAL_MEMBER);

        commissionClothesRepository.delete(clothes);
        chat.setSavedClothesCount(chat.getSavedClothesCount()-1);

        return CommissionDTO.ClothesViewDTO.toDTO(clothes);
    }

    @Override
    public ChatResponseDTO.ShareClothesListDTO changeCommissionedClothesPublic(Long chatId) {

        Member member = Authorization();
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));
        Commission commission = commissionRepository.findByChat(chat)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        if(commission.getDirector().getId().equals(member.getId())) {
            if(chat.getIsShared())
                throw new CommissionHandler(ErrorStatus.COMMISSION_CLOTHES_LIST_IS_PUBLIC);
            else
                chat.setIsShared(true);

            return ChatResponseDTO.ShareClothesListDTO.toDTO(chat);
        }

        throw new CommissionHandler(ErrorStatus.DIRECTOR_NOT_EQUAL_MEMBER);
    }

    @Override
    public ChatResponseDTO.ShareClothesListDTO changeCommissionedClothesPrivate(Long chatId) {

        Member member = Authorization();
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));
        Commission commission = commissionRepository.findByChat(chat)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        if(commission.getDirector().getId().equals(member.getId())){
            if(chat.getIsShared())
                chat.setIsShared(false);
            else
                throw new CommissionHandler(ErrorStatus.COMMISSION_CLOTHES_LIST_IS_PRIVATE);

            return ChatResponseDTO.ShareClothesListDTO.toDTO(chat);
        }

        throw new CommissionHandler(ErrorStatus.DIRECTOR_NOT_EQUAL_MEMBER);
    }

    @Override
    public CommissionDTO.CommissionViewDTO acceptCommission(Long commissionId) {

        Member member = Authorization();
        Commission commission = commissionRepository.findById(commissionId)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        if(commission.getStatus().equals(REQUIRED) && commission.getDirector().getId().equals(member.getId())){
            commission.setStatus(APPROVED);
            commissionRepository.save(commission);

            Chat chat = chatRepository.findByChatMembers(commission.getDirector().getId(), commission.getMember().getId())
                    .orElseGet(() -> {
                        Chat newChat = Chat.builder()
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .commission(commission)
                                .build();
                        newChat = chatRepository.save(newChat);

                        MemberChat memberChat1 = MemberChat.builder()
                                .chat(newChat)
                                .member(member)
                                .build();
                        memberChat1 = memberChatRepository.save(memberChat1);

                        MemberChat memberChat2 = MemberChat.builder()
                                .chat(newChat)
                                .member(commission.getMember())
                                .build();
                        memberChat2 = memberChatRepository.save(memberChat2);

                        newChat.addMemberChat(memberChat1);
                        newChat.addMemberChat(memberChat2);

                        return newChat;
                    });
            chat.setCommission(commission);

            Schedule schedule = Schedule.builder()
                .date(commission.getDesiredDate())
                .memo("수령 희망 날짜")
                .chat(chat)
                .build();
            schedule = scheduleRepository.save(schedule);

            chat.getMemberChatList()
                .forEach(memberChat -> {
                    // 기본 메시지 삽입
                    Message message = Message.builder()
                            .content("의뢰가 수락되었습니다.")
                            .isText(true)
                            .isChecked(Boolean.FALSE)
                            .category(MessageCategory.SYSTEM)
                            .memberChat(memberChat)
                            .build();
                    memberChat.addMessage(message);
                });

            chat.addSchedule(schedule);
            chat.setCommission(commission);
            chatRepository.save(chat);

            return CommissionDTO.CommissionViewDTO.toDTO(commission);
        }

        throw new CommissionHandler(ErrorStatus.DIRECTOR_NOT_EQUAL_MEMBER);
    }

    @Override
    public CommissionDTO.CommissionViewDTO declineCommission(Long commissionId) {

        Member member = Authorization();
        Commission commission = commissionRepository.findById(commissionId)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        if(commission.getStatus().equals(REQUIRED) && commission.getDirector().getId().equals(member.getId())){
            commission.setStatus(TERMINATED);
            commission.setFinishedAt(LocalDateTime.now());
            commissionRepository.save(commission);

            return CommissionDTO.CommissionViewDTO.toDTO(commission);
        }

        throw new CommissionHandler(ErrorStatus.DIRECTOR_NOT_EQUAL_MEMBER);
    }

    @Override
    public CommissionDTO.CommissionViewDTO updateCommission(Long commissionId, CommissionRequestDTO.CommissionDTO commissionRequestDTO) {

        Member member = Authorization();
        Commission commission = commissionRepository.findById(commissionId)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        // 의뢰 요청자만 수정 가능
        if(!member.getId().equals(commission.getMember().getId()))
            throw new CommissionHandler(ErrorStatus.CLIENT_NOT_EQUAL_MEMBER);

        InfoDTO.BasicInfoDTO basicInfoDTO = commissionRequestDTO.getBasicInfo();
        StyleDTO.StyleInfoDTO styleInfoDTO = commissionRequestDTO.getStyle();
        InfoDTO.OtherMattersDTO otherMattersDTO = commissionRequestDTO.getOtherMatters();

        if(otherMattersDTO.getDesiredDate().isBefore(LocalDate.now()) || otherMattersDTO.getDateToUse().isBefore(LocalDate.now())){
            throw new CommissionHandler(ErrorStatus.COMMISSION_INVALID_DATE);
        }

        updateCommissionInfo(commission, basicInfoDTO);
        updateCommissionStyle(commission, styleInfoDTO);
        commission.updateCommissionOther(otherMattersDTO);

        return CommissionDTO.CommissionViewDTO.toDTO(commission);
    }

    @Override
    public CommissionDTO.CommissionViewDTO requestCommissionTermination(Long chatId) {

        Member member = Authorization();
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));
        Commission commission = commissionRepository.findByChat(chat)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        if(commission.getMember().getId().equals(member.getId())){
            if(!commission.getStatus().equals(APPROVED))
                throw new CommissionHandler(ErrorStatus.COMMISSION_STATUS_NOT_APPROVED);

        } else if(commission.getDirector().getId().equals(member.getId())){
            if(!commission.getStatus().equals(APPROVED))
                throw new CommissionHandler(ErrorStatus.COMMISSION_STATUS_NOT_APPROVED);
        } else {
            throw new CommissionHandler(ErrorStatus.BAD_REQUEST);
        }

        commission.setStatus(WAIT_FOR_TERMINATION);
        commissionRepository.save(commission);

        // 기본 메시지 삽입
        chat.getMemberChatList().stream()
                .filter(memberChat -> memberChat.getMember().equals(member))
                .forEach(memberChat -> {
                    Message message = Message.builder()
                            .content("의뢰 종료가 요청되었습니다.")
                            .isText(true)
                            .isChecked(Boolean.FALSE)
                            .category(MessageCategory.SYSTEM)
                            .memberChat(memberChat)
                            .build();
                    memberChat.addMessage(message);
                });

        return CommissionDTO.CommissionViewDTO.toDTO(commission);

    }

    @Override
    public CommissionDTO.CommissionViewDTO declineCommissionTermination(Long chatId) {

        Member member = Authorization();
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));
        Commission commission = commissionRepository.findByChat(chat)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        // 의뢰 당사자인 경우
        if(commission.getMember().getId().equals(member.getId())
                || commission.getDirector().getId().equals(member.getId())){
            if (!commission.getStatus().equals(WAIT_FOR_TERMINATION))
                throw new CommissionHandler(ErrorStatus.COMMISSION_STATUS_NOT_WAIT_FOR_TERMINATION);

            commission.setStatus(APPROVED);
            commissionRepository.save(commission);

            // 기본 메시지 삽입
            chat.getMemberChatList().stream()
                    .filter(memberChat -> memberChat.getMember().equals(member))
                    .forEach(memberChat -> {
                        Message message = Message.builder()
                                .content("의뢰 종료 요청이 거절되었습니다.")
                                .isText(true)
                                .isChecked(Boolean.FALSE)
                                .category(MessageCategory.SYSTEM)
                                .memberChat(memberChat)
                                .build();
                        memberChat.addMessage(message);
                    });

            return CommissionDTO.CommissionViewDTO.toDTO(commission);
        }

        throw new CommissionHandler(ErrorStatus.BAD_REQUEST);
    }

    @Override
    public CommissionDTO.CommissionViewDTO terminateCommission(Long chatId) {

        Member member = Authorization();
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));
        Commission commission = commissionRepository.findByChat(chat)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        if(commission.getMember().getId().equals(member.getId())
                || commission.getDirector().getId().equals(member.getId())){
            if (!commission.getStatus().equals(WAIT_FOR_TERMINATION))
                throw new CommissionHandler(ErrorStatus.COMMISSION_STATUS_NOT_WAIT_FOR_TERMINATION);

            commission.setStatus(TERMINATED);
            commission.setFinishedAt(LocalDateTime.now());
            commissionRepository.save(commission);

            // 기본 메시지 삽입
            chat.getMemberChatList().stream()
                    .filter(memberChat -> memberChat.getMember().equals(member))
                    .forEach(memberChat -> {
                        Message message = Message.builder()
                                .content("의뢰가 종료되었습니다.")
                                .isText(true)
                                .isChecked(Boolean.FALSE)
                                .category(MessageCategory.SYSTEM)
                                .memberChat(memberChat)
                                .build();
                        memberChat.addMessage(message);
                    });

            return CommissionDTO.CommissionViewDTO.toDTO(commission);
        }

        throw new CommissionHandler(ErrorStatus.BAD_REQUEST);
    }

/*-------------------------------------------------- Authorization --------------------------------------------------*/

    private Member Authorization(){
        String loginId = SecurityUtils.getAuthorizedLoginId();
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

/*-------------------------------------------------- Update Methods --------------------------------------------------*/

    private void updateCommissionInfo(Commission commission, InfoDTO.BasicInfoDTO basicInfoDTO) {

        commission.updateCommissionInfo(basicInfoDTO);

        // update CommissionStyle
        commissionStyleRepository.deleteAllByCommission(commission);
        commission.deleteAllStyles();
        createCommissionStyle(commission, basicInfoDTO.getInfoStyle());

        // update CommissionBodyType
        commissionBodyTypeRepository.deleteAllByCommission(commission);
        commission.deleteAllBodyTypes();
        createCommissionBodyType(commission, basicInfoDTO.getInfoBodyType());

        // update CommissionFit
        commissionFitRepository.deleteAllByCommission(commission);
        commission.deleteAllFits();
        createCommissionFit(commission, basicInfoDTO.getInfoFit());

        // update CommissionMaterial
        commissionMaterialRepository.deleteAllByCommission(commission);
        commission.deleteAllMaterials();
        createCommissionMaterial(commission, basicInfoDTO.getInfoMaterial());

    }

    private void updateCommissionStyle(Commission commission, StyleDTO.StyleInfoDTO styleInfoDTO){

        // update CommissionHopeStyle
        commissionHopeStyleRepository.deleteAllByCommission(commission);
        commission.deleteAllHopeStyles();
        createCommissionHopeStyle(commission, styleInfoDTO.getStyleList());

        // update CommissionHopeFit
        commissionHopeFitRepository.deleteAllByCommission(commission);
        commission.deleteAllHopeFits();
        createCommissionHopeFit(commission, styleInfoDTO.getFitList());

        // update CommissionHopeMaterial
        commissionHopeMaterialRepository.deleteAllByCommission(commission);
        commission.deleteAllHopeMaterials();
        createCommissionHopeMaterial(commission, styleInfoDTO.getMaterialList());

        // update CommissionHopeColor
        commissionHopeColorRepository.deleteAllByCommission(commission);
        commission.deleteAllHopeColors();
        createCommissionStyleColor(commission, styleInfoDTO.getColorList());

    }

/*-------------------------------------------------- Create Methods --------------------------------------------------*/

    private void createCommissionStyleColor(Commission commission, StyleDTO.ColorListDTO colorList) {
        if (colorList == null || colorList.getColorList() == null) {return;}

        colorList.getColorList()
                .forEach(color -> {
                    CommissionHopeColor styleColor = CommissionHopeColor.builder()
                            .color(color)
                            .isPrefer(true)
                            .commission(commission)
                            .build();
                    styleColor = commissionHopeColorRepository.save(styleColor);
                    commission.addHopeColor(styleColor);
                });
    }

    private void createCommissionHopeMaterial(Commission commission, StyleDTO.MaterialListDTO materialList) {
        if (materialList == null || materialList.getMaterialList() == null) {return;}

        materialList.getMaterialList()
                .forEach(material -> {
                    CommissionHopeMaterial styleMaterial = CommissionHopeMaterial.builder()
                            .material(material)
                            .isPrefer(true)
                            .commission(commission)
                            .build();
                    styleMaterial = commissionHopeMaterialRepository.save(styleMaterial);
                    commission.addHopeMaterial(styleMaterial);
                });
    }

    private void createCommissionHopeFit(Commission commission, StyleDTO.FitListDTO fitList) {
        if (fitList == null || fitList.getFitList() == null) {return;}

        fitList.getFitList()
                .forEach(fit -> {
                    CommissionHopeFit hopeFit = CommissionHopeFit.builder()
                            .fit(fit)
                            .isPrefer(true)
                            .commission(commission)
                            .build();
                    hopeFit = commissionHopeFitRepository.save(hopeFit);
                    commission.addHopeFit(hopeFit);
                });
    }

    private void createCommissionHopeStyle(Commission commission, StyleDTO.StyleListDTO styleList) {
        if (styleList == null || styleList.getStyleList() == null) {return;}

        styleList.getStyleList()
                .forEach(style -> {
                    CommissionHopeStyle hopeStyle = CommissionHopeStyle.builder()
                            .style(style)
                            .isPrefer(true)
                            .commission(commission)
                            .build();
                    hopeStyle = commissionHopeStyleRepository.save(hopeStyle);
                    commission.addHopeStyle(hopeStyle);
                });
    }

    private void createCommissionStyle(Commission commission, InfoDTO.InfoStyleListDTO infoStyleListDTO){
        if (infoStyleListDTO == null || infoStyleListDTO.getPreferredStyleList() == null) {return;}

        infoStyleListDTO.getPreferredStyleList()
                .forEach(style -> {
                    CommissionStyle infoStyle = CommissionStyle.builder()
                            .style(style)
                            .isPrefer(true)
                            .commission(commission)
                            .build();
                    infoStyle = commissionStyleRepository.save(infoStyle);
                    commission.addStyle(infoStyle);
                });

        infoStyleListDTO.getNonPreferredStyleList()
                .forEach(style -> {
                    CommissionStyle infoStyle = CommissionStyle.builder()
                            .style(style)
                            .isPrefer(false)
                            .commission(commission)
                            .build();
                    infoStyle = commissionStyleRepository.save(infoStyle);
                    commission.addStyle(infoStyle);
                });
    }

    private void createCommissionFit(Commission commission, InfoDTO.InfoFitListDTO infoFitListDTO){
        if (infoFitListDTO == null || infoFitListDTO.getPreferredFitList() == null) {return;}
        infoFitListDTO.getPreferredFitList()
                .forEach(fit -> {
                    CommissionFit infoFit = CommissionFit.builder()
                            .fit(fit)
                            .isPrefer(true)
                            .commission(commission)
                            .build();
                    infoFit = commissionFitRepository.save(infoFit);
                    commission.addFit(infoFit);
                });

        infoFitListDTO.getNonPreferredFitList()
                .forEach(fit -> {
                    CommissionFit infoFit = CommissionFit.builder()
                            .fit(fit)
                            .isPrefer(false)
                            .commission(commission)
                            .build();
                    infoFit = commissionFitRepository.save(infoFit);
                    commission.addFit(infoFit);
                });
    }

    private void createCommissionBodyType(Commission commission, InfoDTO.InfoBodyTypeListDTO infoBodyTypeListDTO) {
        if (infoBodyTypeListDTO == null || infoBodyTypeListDTO.getGoodBodyTypeList() == null) {return;}

        infoBodyTypeListDTO.getGoodBodyTypeList()
                .forEach(bodyType -> {
                    CommissionBodyType infoBodyType = CommissionBodyType.builder()
                            .bodyType(bodyType)
                            .isGood(true)
                            .commission(commission)
                            .build();
                    infoBodyType = commissionBodyTypeRepository.save(infoBodyType);
                    commission.addBodyType(infoBodyType);
                });

        infoBodyTypeListDTO.getBadBodyTypeList()
                .forEach(bodyType -> {
                    CommissionBodyType infoBodyType = CommissionBodyType.builder()
                            .bodyType(bodyType)
                            .isGood(false)
                            .commission(commission)
                            .build();
                    infoBodyType = commissionBodyTypeRepository.save(infoBodyType);
                    commission.addBodyType(infoBodyType);
                });
    }

    private void createCommissionMaterial(Commission commission, InfoDTO.InfoMaterialListDTO infoMaterialListDTO) {
        if (infoMaterialListDTO == null || infoMaterialListDTO.getPreferredMaterialList() == null) {return;}

        infoMaterialListDTO.getPreferredMaterialList()
                .forEach(material -> {
                    CommissionMaterial infoMaterial = CommissionMaterial.builder()
                            .material(material)
                            .isPrefer(true)
                            .commission(commission)
                            .build();
                    infoMaterial = commissionMaterialRepository.save(infoMaterial);
                    commission.addMaterial(infoMaterial);
                });

        infoMaterialListDTO.getNonPreferredMaterialList()
                .forEach(material -> {
                    CommissionMaterial infoMaterial = CommissionMaterial.builder()
                            .material(material)
                            .isPrefer(false)
                            .commission(commission)
                            .build();
                    infoMaterial = commissionMaterialRepository.save(infoMaterial);
                    commission.addMaterial(infoMaterial);
                });
    }
}
