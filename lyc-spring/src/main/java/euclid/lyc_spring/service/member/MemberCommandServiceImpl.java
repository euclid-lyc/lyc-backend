package euclid.lyc_spring.service.member;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.JwtProvider;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.PushSet;
import euclid.lyc_spring.domain.info.Info;
import euclid.lyc_spring.dto.request.MemberRequestDTO;
import euclid.lyc_spring.dto.request.VerificationRequestDTO;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.repository.InfoRepository;
import euclid.lyc_spring.repository.MemberRepository;
import euclid.lyc_spring.repository.PushSetRepository;
import euclid.lyc_spring.repository.mail.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final InfoRepository infoRepository;
    private final PushSetRepository pushSetRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;

    /*-------------------------------------------------- 회원정보 설정 --------------------------------------------------*/

    @Override
    public MemberDTO.MemberSettingInfoDTO updateMemberInfo(MemberRequestDTO.MemberSettingInfoDTO infoDTO, String imageUrl) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 아이디 중복 확인
        if (memberRepository.existsByLoginId(infoDTO.getLoginId())) {
            throw new MemberHandler(ErrorStatus.MEMBER_DUPLICATED_LOGIN_ID);
        }

        loginMember.setLoginId(infoDTO.getLoginId());
        loginMember.setIntroduction(infoDTO.getIntroduction());
        loginMember.setNickname(infoDTO.getNickname());

        if (!imageUrl.isEmpty()) {
            loginMember.setProfileImage(imageUrl);
        }

        memberRepository.save(loginMember);

        return MemberDTO.MemberSettingInfoDTO.toDTO(loginMember);
    }

    @Override
    public MemberDTO.AddressDTO updateAddress(MemberRequestDTO.AddressReqDTO addressReqDTO) {
        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Info info = loginMember.getInfo();

        info.updateAddress(addressReqDTO);
        infoRepository.save(info);
        return MemberDTO.AddressDTO.toDTO(loginMember);
    }

    @Override
    public MemberDTO.MemberPreviewDTO updateLoginPw(VerificationRequestDTO.ChangePasswordDTO passwordDTO) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (!bCryptPasswordEncoder.matches(passwordDTO.getOldPassword(), member.getLoginPw())) {
            throw new MemberHandler(ErrorStatus.MEMBER_OLD_PW_NOT_MATCHED);
        }

        // 기존 비밀번호와 중복여부 확인
        if (passwordDTO.getNewPassword().equals(passwordDTO.getOldPassword())) {
            throw new MemberHandler(ErrorStatus.MEMBER_NEW_PW_NOT_CHANGED);
        }
        // 비밀번호 확인
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new MemberHandler(ErrorStatus.MEMBER_PW_NOT_MATCHED);
        }

        member.changeLoginPw(passwordDTO.getNewPassword(), bCryptPasswordEncoder);
        memberRepository.save(member);

        return MemberDTO.MemberPreviewDTO.toDTO(member);
    }

    /*-------------------------------------------------- 푸시알림 설정 --------------------------------------------------*/

    @Override
    public MemberDTO.PushSetDTO updatePushSet(MemberRequestDTO.PushSetDTO pushSetDTO) {
        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        PushSet pushSet = loginMember.getPushSet();
        pushSet.updatePushSet(pushSetDTO);

        pushSetRepository.save(pushSet);
        return null;
    }
}
