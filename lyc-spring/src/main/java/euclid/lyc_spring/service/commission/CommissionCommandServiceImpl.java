package euclid.lyc_spring.service.commission;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ChatHandler;
import euclid.lyc_spring.apiPayload.exception.handler.CommissionHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.CommissionClothes;
import euclid.lyc_spring.domain.chat.Schedule;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.CommissionOther;
import euclid.lyc_spring.domain.chat.commission.commission_style.*;
import euclid.lyc_spring.domain.chat.commission.commission_info.*;
import euclid.lyc_spring.domain.mapping.MemberChat;
import euclid.lyc_spring.dto.request.CommissionRequestDTO;
import euclid.lyc_spring.dto.request.InfoRequestDTO;
import euclid.lyc_spring.dto.request.StyleRequestDTO;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import euclid.lyc_spring.dto.response.CommissionDTO;
import euclid.lyc_spring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    private final CommissionClotheRepository clotheRepository;

    private final CommissionInfoRepository commissionInfoRepository;
    private final CommissionInfoStyleRepository commissionInfoStyleRepository;
    private final CommissionInfoFitRepository commissionInfoFitRepository;
    private final CommissionInfoBodyTypeRepository commissionInfoBodyTypeRepository;
    private final CommissionInfoMaterialRepository commissionInfoMaterialRepository;

    private final CommissionStyleRepository commissionStyleRepository;
    private final CommissionStyleStyleRepository commissionStyleStyleRepository;
    private final CommissionStyleFitRepository commissionStyleFitRepository;
    private final CommissionStyleMaterialRepository commissionStyleMaterialRepository;
    private final CommissionStyleColorRepository commissionStyleColorRepository;

    private final CommissionOtherRepository commissionOtherRepository;

    @Transactional
    public CommissionDTO.CommissionViewDTO writeCommission(CommissionRequestDTO.CommissionDTO commissionRequestDTO) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member director = memberRepository.findByLoginId(commissionRequestDTO.getDirectorId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        if(member.equals(director)) {
            throw new MemberHandler(ErrorStatus.BAD_REQUEST);
        }

        InfoRequestDTO.BasicInfoDTO basicInfoDTO = commissionRequestDTO.getBasicInfo();
        StyleRequestDTO.StyleDTO styleDTO = commissionRequestDTO.getStyle();
        InfoRequestDTO.OtherMattersDTO otherMattersDTO = commissionRequestDTO.getOtherMatters();

        Commission commission = Commission.builder()
                .status(REQUIRED)
                .createdAt(LocalDateTime.now())
                .member(member)
                .director(director)
                .build();

        commission = commissionRepository.save(commission);

        createInfo(commission, basicInfoDTO);
        createStyle(commission, styleDTO);
        createOther(commission, otherMattersDTO);

        return CommissionDTO.CommissionViewDTO.toDTO(commission);
    }

    @Override
    public CommissionDTO.ClothesViewDTO saveCommissionedClothes(Long chatId, CommissionRequestDTO.ClothesDTO clothesRequestDTO) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Chat chat = chatRepository.findById(chatId).
                orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        if(chat.getSavedClothesCount()==9)
            throw new CommissionHandler(ErrorStatus.COMMISSION_CLOTHES_NOT_SAVED);

        CommissionClothes commissionClothes = CommissionClothes.builder()
                .chat(chat)
                .image(clothesRequestDTO.getImage())
                .url(clothesRequestDTO.getUrl())
                .build();

        clotheRepository.save(commissionClothes);
        chat.reloadSavedClothesCount(chat.getSavedClothesCount()+1);

        return CommissionDTO.ClothesViewDTO.toDTO(commissionClothes);
    }

    @Override
    public CommissionDTO.ClothesViewDTO deleteCommissionedClothes(Long chatId, Long clothesId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        CommissionClothes clothes = clotheRepository.findCommissionClothesByIdAndChat(clothesId,chat)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_CLOTHES_NOT_FOUND));

        clotheRepository.delete(clothes);
        chat.reloadSavedClothesCount(chat.getSavedClothesCount()-1);

        return CommissionDTO.ClothesViewDTO.toDTO(clothes);
    }

    @Override
    public ChatResponseDTO.ShareClothesListDTO changeCommissionedClothesPublic(Long chatId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        if(chat.isShareClothesList())
            throw new CommissionHandler(ErrorStatus.BAD_REQUEST);
        else
            chat.reloadShareClothesList(true);

        return ChatResponseDTO.ShareClothesListDTO.toDTO(chat);
    }

    @Override
    public ChatResponseDTO.ShareClothesListDTO changeCommissionedClothesPrivate(Long chatId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        if(chat.isShareClothesList())
            chat.reloadShareClothesList(false);
        else
            throw new CommissionHandler(ErrorStatus.BAD_REQUEST);

        return ChatResponseDTO.ShareClothesListDTO.toDTO(chat);
    }

    @Override
    public CommissionDTO.CommissionViewDTO acceptCommission(Long commissionId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Commission commission = commissionRepository.findById(commissionId)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        commission.reloadStatus(APPROVED);
        commissionRepository.save(commission);

        Chat chat = Chat.builder()
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .commission(commission)
                .build();
        chat = chatRepository.save(chat);

        MemberChat memberChat1 = MemberChat.builder()
                .chat(chat)
                .member(member)
                .build();

        memberChat1 = memberChatRepository.save(memberChat1);

        MemberChat memberChat2 = MemberChat.builder()
                .chat(chat)
                .member(commission.getDirector())
                .build();
        memberChat2 = memberChatRepository.save(memberChat2);

        Schedule schedule = Schedule.builder()
                .date(commission.getCommissionOther().getDesiredDate())
                .memo("수령 희망 날짜")
                .chat(chat)
                .build();
        schedule = scheduleRepository.save(schedule);

        chat.addSchedule(schedule);
        chat.addMemberChat(memberChat1);
        chat.addMemberChat(memberChat2);

        return CommissionDTO.CommissionViewDTO.toDTO(commission);
    }

    @Override
    public CommissionDTO.CommissionViewDTO declineCommission(Long commissionId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Commission commission = commissionRepository.findById(commissionId)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        commission.reloadStatus(TERMINATED);
        commission.reloadFinishedAt(LocalDateTime.now());
        commissionRepository.save(commission);

        return CommissionDTO.CommissionViewDTO.toDTO(commission);
    }

    @Override
    public CommissionDTO.CommissionViewDTO updateCommission(Long commissionId, CommissionRequestDTO.CommissionDTO commissionRequestDTO) {

        Commission commission = commissionRepository.findById(commissionId)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        InfoRequestDTO.BasicInfoDTO basicInfoDTO = commissionRequestDTO.getBasicInfo();
        StyleRequestDTO.StyleDTO styleDTO = commissionRequestDTO.getStyle();
        InfoRequestDTO.OtherMattersDTO otherMattersDTO = commissionRequestDTO.getOtherMatters();

        updateCommissionInfo(commission, basicInfoDTO);
        updateCommissionOther(commission, otherMattersDTO);
        updateCommissionStyle(commission, styleDTO);
        commission.reloadCreatedAt(LocalDateTime.now());

        return CommissionDTO.CommissionViewDTO.toDTO(commission);
    }

    @Override
    public CommissionDTO.CommissionViewDTO requestCommissionTermination(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));
        Commission commission = commissionRepository.findByChat(chat)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        commission.reloadStatus(WAIT_FOR_TERMINATION);
        commissionRepository.save(commission);

        return CommissionDTO.CommissionViewDTO.toDTO(commission);
    }

    @Override
    public CommissionDTO.CommissionViewDTO declineCommissionTermination(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));
        Commission commission = commissionRepository.findByChat(chat)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        commission.reloadStatus(APPROVED);
        commissionRepository.save(commission);
        return CommissionDTO.CommissionViewDTO.toDTO(commission);
    }

    @Override
    public CommissionDTO.CommissionViewDTO terminateCommission(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));
        Commission commission = commissionRepository.findByChat(chat)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        commission.reloadStatus(TERMINATED);
        commission.reloadFinishedAt(LocalDateTime.now());
        commissionRepository.save(commission);

        return CommissionDTO.CommissionViewDTO.toDTO(commission);
    }


    //=== update ===//
    private void updateCommissionOther(Commission commission, InfoRequestDTO.OtherMattersDTO otherMattersDTO) {
        CommissionOther commissionOther = commissionOtherRepository.findByCommission(commission)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_OTHER_NOT_FOUND));
        commissionOther.reloadDateToUse(otherMattersDTO.getDateToUse());
        commissionOther.reloadDesiredDate(otherMattersDTO.getDesiredDate());
        commissionOther.reloadMaxPrice(otherMattersDTO.getMaxPrice());
        commissionOther.reloadMinPrice(otherMattersDTO.getMinPrice());
        commissionOther.reloadText(otherMattersDTO.getText());
    }

    private void updateCommissionInfo(Commission commission, InfoRequestDTO.BasicInfoDTO basicInfoDTO) {
        CommissionInfo commissionInfo = commissionInfoRepository.findByCommission(commission)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_INFO_NOT_FOUND));

        //update CommissionBasicInfo
        commissionInfo.reloadInfo(basicInfoDTO);

        // update CommissionInfoStyle
        commissionInfoStyleRepository.deleteAllByCommissionInfo(commissionInfo);
        createCommissionInfoStyle(commissionInfo, basicInfoDTO.getInfoStyle());

        // update CommissionInfoBodyType
        commissionInfoBodyTypeRepository.deleteAllByCommissionInfo(commissionInfo);
        createCommissionInfoBodyType(commissionInfo, basicInfoDTO.getInfoBodyType());

        // update CommissionInfoFit
        commissionInfoFitRepository.deleteAllByCommissionInfo(commissionInfo);
        createCommissionInfoFit(commissionInfo, basicInfoDTO.getInfoFit());

        // update CommissionInfoMaterial
        commissionInfoMaterialRepository.deleteAllByCommissionInfo(commissionInfo);
        createCommissionInfoMaterial(commissionInfo, basicInfoDTO.getInfoMaterial());

    }

    private void updateCommissionStyle(Commission commission, StyleRequestDTO.StyleDTO styleDTO){
        CommissionStyle commissionStyle = commissionStyleRepository.findByCommission(commission)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_STYLE_NOT_FOUND));
        commissionStyle.clear();

        // update CommissionStyleStyle
        commissionStyleStyleRepository.deleteAllByCommissionStyle(commissionStyle);
        createCommissionStyleStyle(commissionStyle, styleDTO.getStyleList());

        // update CommissionStyleFit
        commissionStyleFitRepository.deleteAllByCommissionStyle(commissionStyle);
        createCommissionStyleFit(commissionStyle, styleDTO.getFitList());

        // update CommissionStyleMaterial
        commissionStyleMaterialRepository.deleteAllByCommissionStyle(commissionStyle);
        createCommissionStyleMaterial(commissionStyle, styleDTO.getMaterialList());

        // update CommissionStyleColor
        commissionStyleColorRepository.deleteAllByCommissionStyle(commissionStyle);
        createCommissionStyleColor(commissionStyle, styleDTO.getColorList());
    }

    //=== createOther ===//
    private void createOther(Commission commission, InfoRequestDTO.OtherMattersDTO otherMattersDTO) {
        CommissionOther commissionOther = CommissionOther.builder()
                .dateToUse(otherMattersDTO.getDateToUse())
                .desiredDate(otherMattersDTO.getDesiredDate())
                .minPrice(otherMattersDTO.getMinPrice())
                .maxPrice(otherMattersDTO.getMaxPrice())
                .text(otherMattersDTO.getText())
                .build();

        commission.setCommissionOther(commissionOther);
        commissionOtherRepository.save(commissionOther);
    }


    //=== createStyle ===//
    private void createStyle(Commission commission, StyleRequestDTO.StyleDTO styleDTO){
        CommissionStyle commissionStyle = CommissionStyle.builder().build();

        commission.setCommissionStyle(commissionStyle);
        commissionStyle = commissionStyleRepository.save(commissionStyle);
        
        createCommissionStyleStyle(commissionStyle, styleDTO.getStyleList());
        createCommissionStyleFit(commissionStyle, styleDTO.getFitList());
        createCommissionStyleMaterial(commissionStyle, styleDTO.getMaterialList());
        createCommissionStyleColor(commissionStyle, styleDTO.getColorList());

        commissionStyleRepository.save(commissionStyle);
    }

    private void createCommissionStyleColor(CommissionStyle commissionStyle, StyleRequestDTO.ColorListDTO colorList) {
        if (colorList == null || colorList.getColorList() == null) {return;}

        colorList.getColorList()
                .forEach(color -> {
                    CommissionStyleColor styleColor = CommissionStyleColor.builder()
                            .color(color)
                            .isPrefer(true)
                            .build();
                    commissionStyle.addCommissionStyleColor(styleColor);
                    commissionStyleColorRepository.save(styleColor);
                });
    }

    private void createCommissionStyleMaterial(CommissionStyle commissionStyle, StyleRequestDTO.MaterialListDTO materialList) {
        if (materialList == null || materialList.getMaterialList() == null) {return;}

        materialList.getMaterialList()
                .forEach(material -> {
                    CommissionStyleMaterial styleMaterial = CommissionStyleMaterial.builder()
                            .material(material)
                            .isPrefer(true)
                            .build();
                    commissionStyle.addCommissionStyleMaterial(styleMaterial);
                    commissionStyleMaterialRepository.save(styleMaterial);
                });
    }

    private void createCommissionStyleFit(CommissionStyle commissionStyle, StyleRequestDTO.FitListDTO fitList) {
        if (fitList == null || fitList.getFitList() == null) {return;}

        fitList.getFitList()
                .forEach(fit -> {
                    CommissionStyleFit styleFit = CommissionStyleFit.builder()
                            .fit(fit)
                            .isPrefer(true)
                            .build();
                    commissionStyle.addCommissionStyleFit(styleFit);
                    commissionStyleFitRepository.save(styleFit);
                });
    }

    private void createCommissionStyleStyle(CommissionStyle commissionStyle, StyleRequestDTO.StyleListDTO styleList) {
        if (styleList == null || styleList.getStyleList() == null) {return;}

        styleList.getStyleList()
                .forEach(style -> {
                    CommissionStyleStyle infoStyle = CommissionStyleStyle.builder()
                            .style(style)
                            .isPrefer(true)
                            .build();
                    commissionStyle.addCommissionStyleStyle(infoStyle);
                    commissionStyleStyleRepository.save(infoStyle);
                });
    }



    //=== createInfo ===///
    private void createInfo(Commission commission, InfoRequestDTO.BasicInfoDTO basicInfoDTO) {
        CommissionInfo commissionInfo = CommissionInfo.builder()
                .height(basicInfoDTO.getHeight())
                .weight(basicInfoDTO.getWeight())
                .topSize(basicInfoDTO.getTopSize())
                .bottomSize(basicInfoDTO.getBottomSize())
                .text(basicInfoDTO.getText())
                .build();

        commission.setCommissionInfo(commissionInfo);
        commissionInfo = commissionInfoRepository.save(commissionInfo);

        createCommissionInfoStyle(commissionInfo, basicInfoDTO.getInfoStyle());
        createCommissionInfoFit(commissionInfo, basicInfoDTO.getInfoFit());
        createCommissionInfoBodyType(commissionInfo, basicInfoDTO.getInfoBodyType());
        createCommissionInfoMaterial(commissionInfo, basicInfoDTO.getInfoMaterial());
    }


    private void createCommissionInfoStyle(CommissionInfo commissionInfo, InfoRequestDTO.InfoStyleListDTO infoStyleListDTO){
        if (infoStyleListDTO == null || infoStyleListDTO.getPreferredStyleList() == null) {return;}

        infoStyleListDTO.getPreferredStyleList()
                .forEach(style -> {
                    CommissionInfoStyle infoStyle = CommissionInfoStyle.builder()
                            .style(style)
                            .isPrefer(true)
                            .build();
                    commissionInfo.addCommissionInfoStyle(infoStyle);
                    commissionInfoStyleRepository.save(infoStyle);
                });

        infoStyleListDTO.getNonPreferredStyleList()
                .forEach(style -> {
                    CommissionInfoStyle infoStyle = CommissionInfoStyle.builder()
                            .style(style)
                            .isPrefer(false)
                            .build();
                    commissionInfo.addCommissionInfoStyle(infoStyle);
                    commissionInfoStyleRepository.save(infoStyle);
                });
    }

    private void createCommissionInfoFit(CommissionInfo commissionInfo, InfoRequestDTO.InfoFitListDTO infoFitListDTO){
        if (infoFitListDTO == null || infoFitListDTO.getPreferredFitList() == null) {return;}
        infoFitListDTO.getPreferredFitList()
                .forEach(fit -> {
                    CommissionInfoFit infoFit = CommissionInfoFit.builder()
                            .fit(fit)
                            .isPrefer(true)
                            .build();
                    commissionInfo.addCommissionInfoFit(infoFit);
                    commissionInfoFitRepository.save(infoFit);
                });

        infoFitListDTO.getNonPreferredFitList()
                .forEach(fit -> {
                    CommissionInfoFit infoFit = CommissionInfoFit.builder()
                            .fit(fit)
                            .isPrefer(false)
                            .build();
                    commissionInfo.addCommissionInfoFit(infoFit);
                    commissionInfoFitRepository.save(infoFit);
                });
    }

    private void createCommissionInfoBodyType(CommissionInfo commissionInfo, InfoRequestDTO.InfoBodyTypeListDTO infoBodyTypeListDTO) {
        if (infoBodyTypeListDTO == null || infoBodyTypeListDTO.getGoodBodyTypeList() == null) {return;}

        infoBodyTypeListDTO.getGoodBodyTypeList()
                .forEach(bodyType -> {
                    CommissionInfoBodyType infoBodyType = CommissionInfoBodyType.builder()
                            .bodyType(bodyType)
                            .isGood(true)
                            .build();
                    commissionInfo.addCommissionInfoBodyType(infoBodyType);
                    commissionInfoBodyTypeRepository.save(infoBodyType);
                });

        infoBodyTypeListDTO.getBadBodyTypeList()
                .forEach(bodyType -> {
                    CommissionInfoBodyType infoBodyType = CommissionInfoBodyType.builder()
                            .bodyType(bodyType)
                            .isGood(false)
                            .build();
                    commissionInfo.addCommissionInfoBodyType(infoBodyType);
                    commissionInfoBodyTypeRepository.save(infoBodyType);
                });
    }

    private void createCommissionInfoMaterial(CommissionInfo commissionInfo, InfoRequestDTO.InfoMaterialListDTO infoMaterialListDTO) {
        if (infoMaterialListDTO == null || infoMaterialListDTO.getPreferredMaterialList() == null) {return;}

        infoMaterialListDTO.getPreferredMaterialList()
                .forEach(material -> {
                    CommissionInfoMaterial infoMaterial = CommissionInfoMaterial.builder()
                            .material(material)
                            .isPrefer(true)
                            .build();
                    commissionInfo.addCommissionInfoMaterial(infoMaterial);
                    commissionInfoMaterialRepository.save(infoMaterial);
                });

        infoMaterialListDTO.getNonPreferredMaterialList()
                .forEach(material -> {
                    CommissionInfoMaterial infoMaterial = CommissionInfoMaterial.builder()
                            .material(material)
                            .isPrefer(false)
                            .build();
                    commissionInfo.addCommissionInfoMaterial(infoMaterial);
                    commissionInfoMaterialRepository.save(infoMaterial);
                });
    }
}
