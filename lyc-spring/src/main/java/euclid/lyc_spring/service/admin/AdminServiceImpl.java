package euclid.lyc_spring.service.admin;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.Role;
import euclid.lyc_spring.domain.info.Info;
import euclid.lyc_spring.domain.info.InfoBodyType;
import euclid.lyc_spring.repository.*;
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

    private final S3ImageService s3ImageService;

/*-------------------------------------------------- Auth --------------------------------------------------*/

    @Override
    public void deleteMember() {

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

    private void checkAdmin() {
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginIdAndRole(loginId, Role.ADMIN)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_ADMIN));
    }
}
