package euclid.lyc_spring.service.admin;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.Role;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;

/*-------------------------------------------------- Auth --------------------------------------------------*/

    @Override
    public void deleteMember() {

        // 관리자 확인
        checkAdmin();

        LocalDateTime maxInactive = LocalDateTime.now().minusDays(30);
        List<Member> memberList = memberRepository.findAllByInactiveBefore(maxInactive);

        memberRepository.deleteAll(memberList);
    }

/*-------------------------------------------------- 관리자 인증 --------------------------------------------------*/

    private void checkAdmin() {
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginIdAndRole(loginId, Role.ADMIN)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_ADMIN));
    }
}
