package euclid.lyc_spring.service.admin;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ChatHandler;
import euclid.lyc_spring.apiPayload.exception.handler.CommissionHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionInfo;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionStyle;
import euclid.lyc_spring.domain.enums.Role;
import euclid.lyc_spring.domain.mapping.MemberChat;
import euclid.lyc_spring.repository.*;
import euclid.lyc_spring.service.s3.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private final CommissionOtherRepository commissionOtherRepository;

    private final CommissionInfoRepository commissionInfoRepository;
    private final CommissionInfoFitRepository commissionInfoFitRepository;
    private final CommissionInfoMaterialRepository commissionInfoMaterialRepository;
    private final CommissionInfoBodyTypeRepository commissionInfoBodyTypeRepository;
    private final CommissionInfoStyleRepository commissionInfoStyleRepository;

    private final CommissionStyleRepository commissionStyleRepository;
    private final CommissionStyleColorRepository commissionStyleColorRepository;
    private final CommissionStyleFitRepository commissionStyleFitRepository;
    private final CommissionStyleMaterialRepository commissionStyleMaterialRepository;
    private final CommissionStyleStyleRepository commissionStyleStyleRepository;

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

    @Override
    @Transactional
    public void deleteChats() {

        // 관리자 확인
        checkAdmin();

        LocalDateTime maxInactive = LocalDateTime.now().minusDays(30);
        chatRepository.findAllByInactiveBefore(maxInactive)
                .forEach(chat -> {
                    Commission commission = deleteCommission(chat);
                    deleteMessages(chat);
                    scheduleRepository.deleteAllByChatId(chat.getId());
                    chatRepository.delete(chat);
                    commissionRepository.delete(commission);
                });

    }

    private Commission deleteCommission(Chat chat) {
        Commission commission = commissionRepository.findByChat(chat)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.COMMISSION_NOT_FOUND));
        deleteCommissionStyle(commission);
        deleteCommissionInfo(commission);
        deleteCommissionOther(commission);
        return commission;
    }

    private void deleteMessages(Chat chat) {
        List<MemberChat> memberChats = memberChatRepository.findAllByChatId(chat.getId());
        for (MemberChat memberChat : memberChats) {
            messageRepository.deleteAllByMemberChatId(memberChat.getId());
            memberChatRepository.deleteById(memberChat.getId());
        }
    }

    private void deleteCommissionOther(Commission commission) {
        commissionOtherRepository.deleteByCommissionId(commission.getId());
    }

    private void deleteCommissionInfo(Commission commission) {
        CommissionInfo commissionInfo = commissionInfoRepository.findByCommission(commission)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_INFO_NOT_FOUND));
        commissionInfoFitRepository.deleteAllByCommissionInfo(commissionInfo);
        commissionInfoMaterialRepository.deleteAllByCommissionInfo(commissionInfo);
        commissionInfoStyleRepository.deleteAllByCommissionInfo(commissionInfo);
        commissionInfoBodyTypeRepository.deleteAllByCommissionInfo(commissionInfo);
    }

    private void deleteCommissionStyle(Commission commission) {
        CommissionStyle commissionStyle = commissionStyleRepository.findByCommission(commission)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_STYLE_NOT_FOUND));
        commissionStyleColorRepository.deleteAllByCommissionStyle(commissionStyle);
        commissionStyleFitRepository.deleteAllByCommissionStyle(commissionStyle);
        commissionStyleMaterialRepository.deleteAllByCommissionStyle(commissionStyle);
        commissionStyleStyleRepository.deleteAllByCommissionStyle(commissionStyle);
    }

    /*-------------------------------------------------- 관리자 인증 --------------------------------------------------*/

    private void checkAdmin() {
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginIdAndRole(loginId, Role.ADMIN)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_ADMIN));
    }
}
