package euclid.lyc_spring.service.commission;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ChatHandler;
import euclid.lyc_spring.apiPayload.exception.handler.CommissionHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.CommissionClothes;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.dto.response.CommissionDTO;
import euclid.lyc_spring.repository.ChatRepository;
import euclid.lyc_spring.repository.CommissionRepository;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommissionQueryServiceImpl implements CommissionQueryService {


    private final MemberRepository memberRepository;
    private final CommissionRepository commissionRepository;
    private final ChatRepository chatRepository;

    @Override
    public List<CommissionDTO.CommissionViewDTO> getAllCommissionList(Integer pageSize, LocalDateTime cursorDateTime) {

        Member member = Authorization();

        List<Commission> commissionList = commissionRepository.findCommissionsByDirectorId(member.getId(), pageSize, cursorDateTime);
        return commissionList.stream()
                .map(CommissionDTO.CommissionViewDTO::toDTO)
                .toList();
    }

    @Override
    public CommissionDTO.CommissionInfoDTO getCommission(Long commissionId) {

        Member member = Authorization();

        Commission commission = commissionRepository.findById(commissionId)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        if(commission.getMember().getId().equals(member.getId())
                || commission.getDirector().getId().equals(member.getId())) {
            return CommissionDTO.CommissionInfoDTO.toDTO(commission);
        }

        throw new CommissionHandler(ErrorStatus.BAD_REQUEST);
    }

    @Override
    public List<CommissionDTO.ClothesViewDTO> getAllCommissionedClothes(Long chatId) {

        Member member = Authorization();
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() ->  new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));
        Commission commission = commissionRepository.findByChat(chat)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        if(commission.getDirector().getId().equals(member.getId()))
            return chat.getCommissionClothesList().stream()
                    .map(CommissionDTO.ClothesViewDTO::toDTO)
                    .toList();
        else if (commission.getMember().getId().equals(member.getId())) {
            if(chat.isShareClothesList()){
                List<CommissionClothes> clotheList = chat.getCommissionClothesList();

                return clotheList.stream()
                        .map(CommissionDTO.ClothesViewDTO::toDTO)
                        .toList();
            }

            throw new CommissionHandler(ErrorStatus.COMMISSION_CLOTHES_LIST_IS_PRIVATE);
        }

        throw new ChatHandler(ErrorStatus.BAD_REQUEST);
    }

    //== Authorization ==//
    private Member Authorization(){
        String loginId = SecurityUtils.getAuthorizedLoginId();
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
