package euclid.lyc_spring.service.admin;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ChatHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.enums.Role;
import euclid.lyc_spring.domain.mapping.MemberChat;
import euclid.lyc_spring.repository.*;
import euclid.lyc_spring.repository.commission.*;
import euclid.lyc_spring.service.s3.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final InfoRepository infoRepository;
    private final InfoBodyTypeRepository infoBodyTypeRepository;
    private final InfoStyleRepository infoStyleRepository;
    private final InfoFitRepository infoFitRepository;
    private final InfoMaterialRepository infoMaterialRepository;

    private final ChatRepository chatRepository;
    private final MemberChatRepository memberChatRepository;
    private final MessageRepository messageRepository;
    private final ScheduleRepository scheduleRepository;

    private final CommissionRepository commissionRepository;

    private final CommissionFitRepository commissionFitRepository;
    private final CommissionMaterialRepository commissionMaterialRepository;
    private final CommissionBodyTypeRepository commissionBodyTypeRepository;

    private final CommissionStyleRepository commissionStyleRepository;
    private final CommissionHopeColorRepository commissionHopeColorRepository;
    private final CommissionHopeFitRepository commissionHopeFitRepository;
    private final CommissionHopeMaterialRepository commissionHopeMaterialRepository;
    private final CommissionHopeStyleRepository commissionHopeStyleRepository;

    private final S3ImageService s3ImageService;

/*-------------------------------------------------- Auth --------------------------------------------------*/

    @Override
    public void deleteMembers() {

        // 관리자 확인
        checkAdmin();

        LocalDateTime maxInactive = LocalDateTime.now().minusDays(30);
        List<Member> memberList = memberRepository.findAllByInactiveBefore(maxInactive);

        for (Member member : memberList) {

            // S3 이미지 삭제
            String imageUrl = member.getProfileImage();
            s3ImageService.deleteImageFromS3(imageUrl);

            // info 정보 삭제
            Long infoId = member.getInfo().getId();
            infoBodyTypeRepository.deleteAllByInfoId(infoId);
            infoFitRepository.deleteAllByInfoId(infoId);
            infoMaterialRepository.deleteAllByInfoId(infoId);
            infoStyleRepository.deleteAllByInfoId(infoId);
            infoRepository.deleteById(infoId);

            // member 정보 삭제
            memberRepository.deleteById(member.getId());
        }

    }


    @Transactional
    public void resetAllPopularity() {
        //checkAdmin();
        memberRepository.resetAllPopularity();
    }

/*-------------------------------------------------- 관리자 인증 --------------------------------------------------*/

    @Override
    @Transactional
    public void deleteChats() {

        // 관리자 확인
        checkAdmin();

        LocalDateTime maxInactive = LocalDateTime.now().minusDays(30);
        chatRepository.findAllByInactiveBefore(maxInactive)
                .forEach(chat -> {
                    Commission commission = deleteCommissionDetails(chat);
                    deleteMessages(chat);
                    scheduleRepository.deleteAllByChatId(chat.getId());
                    chatRepository.delete(chat);
                    commissionRepository.delete(commission);
                });

    }

    private Commission deleteCommissionDetails(Chat chat) {
        Commission commission = commissionRepository.findByChat(chat)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.COMMISSION_NOT_FOUND));
        deleteCommissionStyle(commission);
        deleteCommissionInfo(commission);
        return commission;
    }

    private void deleteMessages(Chat chat) {
        List<MemberChat> memberChats = memberChatRepository.findAllByChatId(chat.getId());
        for (MemberChat memberChat : memberChats) {
            messageRepository.deleteAllByMemberChatId(memberChat.getId());
            memberChatRepository.deleteById(memberChat.getId());
        }
    }

    private void deleteCommissionInfo(Commission commission) {
        commissionFitRepository.deleteAllByCommission(commission);
        commissionMaterialRepository.deleteAllByCommission(commission);
        commissionStyleRepository.deleteAllByCommission(commission);
        commissionBodyTypeRepository.deleteAllByCommission(commission);
    }

    private void deleteCommissionStyle(Commission commission) {
        commissionHopeColorRepository.deleteAllByCommission(commission);
        commissionHopeFitRepository.deleteAllByCommission(commission);
        commissionHopeMaterialRepository.deleteAllByCommission(commission);
        commissionHopeStyleRepository.deleteAllByCommission(commission);
    }

/*-------------------------------------------------- 관리자 인증 --------------------------------------------------*/

    private void checkAdmin() {
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginIdAndRole(loginId, Role.ADMIN)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_ADMIN));
    }
}
