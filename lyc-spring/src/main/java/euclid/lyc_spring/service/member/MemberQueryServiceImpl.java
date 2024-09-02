package euclid.lyc_spring.service.member;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

/*-------------------------------------------------- 회원정보 설정 --------------------------------------------------*/

    @Override
    public MemberDTO.MemberSettingInfoDTO getMemberSettingInfo() {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return MemberDTO.MemberSettingInfoDTO.toDTO(loginMember);
    }

    @Override
    public MemberDTO.AdrressDTO getAdrress() {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return MemberDTO.AdrressDTO.toDTO(loginMember);
    }

    /*-------------------------------------------------- 푸시알림 설정 --------------------------------------------------*/

    @Override
    public MemberDTO.PushSetDTO getPushSet() {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return MemberDTO.PushSetDTO.toDTO(loginMember);
    }
}
