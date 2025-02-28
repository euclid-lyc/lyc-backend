package euclid.lyc_spring.service.auth;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.JwtGenerator;
import euclid.lyc_spring.auth.JwtProvider;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.PushSet;
import euclid.lyc_spring.domain.RefreshToken;
import euclid.lyc_spring.domain.enums.Role;
import euclid.lyc_spring.domain.info.*;
import euclid.lyc_spring.dto.request.*;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.dto.response.SignDTO;
import euclid.lyc_spring.dto.token.JwtTokenDTO;
import euclid.lyc_spring.repository.*;
import euclid.lyc_spring.repository.mail.VerificationCodeRepository;
import euclid.lyc_spring.repository.token.RefreshTokenRepository;
import euclid.lyc_spring.repository.token.TokenBlackListRepository;
import euclid.lyc_spring.service.s3.S3ImageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    @Value("${cloud.aws.s3.default-profile}")
    private String defaultProfile;

    private final InfoRepository infoRepository;
    private final InfoStyleRepository infoStyleRepository;
    private final InfoFitRepository infoFitRepository;
    private final InfoMaterialRepository infoMaterialRepository;
    private final InfoBodyTypeRepository infoBodyTypeRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PushSetRepository pushSetRepository;

    private final JwtGenerator jwtGenerator;
    private final JwtProvider jwtProvider;
    private final TokenBlackListRepository tokenBlackListRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final S3ImageService s3ImageService;

/*-------------------------------------------------- 로그인 및 로그아웃 --------------------------------------------------*/

    @Override
    public void verifyCode(HttpServletRequest request, String code) {

        String tempToken = jwtProvider.resolveToken(request);
        SecurityUtils.checkTempAuthorization();

        // 인증 코드 확인
        if (!code.equals(verificationCodeRepository.getVerificationCode(tempToken))) {
            throw new MemberHandler(ErrorStatus.MAIL_NOT_VERIFIED);
        }
    }

    @Override
    @Transactional
    public MemberDTO.MemberInfoDTO join(HttpServletRequest request, RegisterDTO.RegisterMemberDTO registerMemberDTO, String imageUrl) {

        String tempToken = jwtProvider.resolveToken(request);
        SecurityUtils.checkTempAuthorization();

        // 인증 코드 확인
        if (!registerMemberDTO.getVerificationCode().equals(verificationCodeRepository.getVerificationCode(tempToken))) {
            throw new MemberHandler(ErrorStatus.MAIL_NOT_VERIFIED);
        }

        MemberRequestDTO.MemberDTO memberDTO = registerMemberDTO.getMember();
        InfoDTO.BasicInfoDTO basicInfoDTO = registerMemberDTO.getInfo();
        MemberRequestDTO.PushSetDTO pushSetDTO = registerMemberDTO.getPushSet();

        if(imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = defaultProfile;
        }

        // 중복된 아이디가 있음
        if (memberRepository.findByLoginId(memberDTO.getLoginId()).isPresent()) {
            handleMemberAndS3Bucket(imageUrl, ErrorStatus.MEMBER_DUPLICATED_LOGIN_ID);
        }
        // 중복된 이메일이 있음
        if (memberRepository.findByLoginId(memberDTO.getEmail()).isPresent()) {
            handleMemberAndS3Bucket(imageUrl, ErrorStatus.MEMBER_DUPLICATED_EMAIL);
        }
        // 비밀번호와 비밀번호 확인이 일치하지 않음
        if (!memberDTO.getLoginPw().equals(memberDTO.getLoginPwCheck())) {
            handleMemberAndS3Bucket(imageUrl, ErrorStatus.MEMBER_INVALID_LOGIN_PW);
        }

        Member member = Member.builder()
                .name(memberDTO.getName())
                .loginId(memberDTO.getLoginId())
                .loginPw(bCryptPasswordEncoder.encode(memberDTO.getLoginPw()))
                .email(memberDTO.getEmail())
                .phone(memberDTO.getPhone())
                .nickname(memberDTO.getNickname())
                .introduction(memberDTO.getIntroduction())
                .profileImage(imageUrl)
                .role(Role.MEMBER)
                .build();

        member = memberRepository.save(member);

        createInfo(member, basicInfoDTO);
        createPushSet(member, pushSetDTO);

        return euclid.lyc_spring.dto.response.MemberDTO.MemberInfoDTO.toDTO(member);

    }

    private void handleMemberAndS3Bucket(String imageUrl, ErrorStatus errorStatus) {
        s3ImageService.deleteImageFromS3(imageUrl);
        throw new MemberHandler(errorStatus);
    }

    private void createPushSet(Member member, MemberRequestDTO.PushSetDTO pushSetDTO) {

        PushSet pushSet = PushSet.builder()
                .ad(pushSetDTO.getAd())
                .feed(pushSetDTO.getFeed())
                .event(pushSetDTO.getEvent())
                .dm(pushSetDTO.getDm())
                .likeMark(pushSetDTO.getLikeMark())
                .schedule(pushSetDTO.getSchedule())
                .member(member)
                .build();

        pushSet = pushSetRepository.save(pushSet);
        member.addPushSet(pushSet);
    }

    private void createInfo(Member member, InfoDTO.BasicInfoDTO infoDto) {

        Info info = Info.builder()
                .member(member)
                .height(infoDto.getHeight())
                .weight(infoDto.getWeight())
                .topSize(infoDto.getTopSize())
                .bottomSize(infoDto.getBottomSize())
                .postalCode(infoDto.getPostalCode())
                .address(infoDto.getAddress())
                .detailAddress(infoDto.getDetailAddress())
                .text(infoDto.getText())
                .build();

        info = infoRepository.save(info);

        createInfoStyle(info, infoDto.getInfoStyle());
        createInfoFit(info, infoDto.getInfoFit());
        createInfoMaterial(info, infoDto.getInfoMaterial());
        createInfoBodyType(info, infoDto.getInfoBodyType());

    }

    private void createInfoStyle(Info info, InfoDTO.InfoStyleListDTO infoStyleListDTO) {

        infoStyleListDTO.getPreferredStyleList()
                .forEach(style -> {
                    InfoStyle infoStyle = InfoStyle.builder()
                            .info(info)
                            .style(style)
                            .isPrefer(true)
                            .build();
                    infoStyle = infoStyleRepository.save(infoStyle);
                    info.addInfoStyle(infoStyle);

                });

        infoStyleListDTO.getNonPreferredStyleList()
                .forEach(style -> {
                    InfoStyle infoStyle = InfoStyle.builder()
                            .info(info)
                            .style(style)
                            .isPrefer(false)
                            .build();
                    infoStyle = infoStyleRepository.save(infoStyle);
                    info.addInfoStyle(infoStyle);

                });
    }

    private void createInfoFit(Info info, InfoDTO.InfoFitListDTO infoFitListDTO) {

        infoFitListDTO.getPreferredFitList()
                .forEach(style -> {
                    InfoFit infoFit = InfoFit.builder()
                            .info(info)
                            .fit(style)
                            .isPrefer(true)
                            .build();
                    infoFit = infoFitRepository.save(infoFit);
                    info.addInfoFit(infoFit);
                });

        infoFitListDTO.getNonPreferredFitList()
                .forEach(style -> {
                    InfoFit infoFit = InfoFit.builder()
                            .info(info)
                            .fit(style)
                            .isPrefer(false)
                            .build();
                    infoFit = infoFitRepository.save(infoFit);
                    info.addInfoFit(infoFit);
                });
    }

    private void createInfoMaterial(Info info, InfoDTO.InfoMaterialListDTO infoMaterialListDTO) {

        infoMaterialListDTO.getPreferredMaterialList()
                .forEach(material -> {
                    InfoMaterial infoMaterial = InfoMaterial.builder()
                            .info(info)
                            .material(material)
                            .isPrefer(true)
                            .build();
                    infoMaterial = infoMaterialRepository.save(infoMaterial);
                    info.addInfoMaterial(infoMaterial);
                });

        infoMaterialListDTO.getNonPreferredMaterialList()
                .forEach(material -> {
                    InfoMaterial infoMaterial = InfoMaterial.builder()
                            .info(info)
                            .material(material)
                            .isPrefer(false)
                            .build();
                    infoMaterial = infoMaterialRepository.save(infoMaterial);
                    info.addInfoMaterial(infoMaterial);
                });
    }

    private void createInfoBodyType(Info info, InfoDTO.InfoBodyTypeListDTO infoBodyTypeListDTO) {

        infoBodyTypeListDTO.getGoodBodyTypeList()
                .forEach(bodyType -> {
                    InfoBodyType infoBodyType = InfoBodyType.builder()
                            .info(info)
                            .bodyType(bodyType)
                            .isGood(true)
                            .build();
                    infoBodyType = infoBodyTypeRepository.save(infoBodyType);
                    info.addInfoBodyType(infoBodyType);
                });

        infoBodyTypeListDTO.getBadBodyTypeList()
                .forEach(bodyType -> {
                    InfoBodyType infoBodyType = InfoBodyType.builder()
                            .info(info)
                            .bodyType(bodyType)
                            .isGood(false)
                            .build();
                    infoBodyType = infoBodyTypeRepository.save(infoBodyType);
                    info.addInfoBodyType(infoBodyType);
                });
    }

    @Override
    public MemberDTO.MemberPreviewDTO withdraw() {

        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        member.setInactive(now);
        memberRepository.save(member);

        return MemberDTO.MemberPreviewDTO.toDTO(member);
    }

/*-------------------------------------------------- 회원가입 및 탈퇴 --------------------------------------------------*/

    @Override
    public SignDTO.SignInDTO signIn(SignRequestDTO.SignInDTO signInRequestDTO, HttpServletResponse response) {

        // 로그인 아이디가 일치하지 않으면 에러 발생
        Member member = memberRepository.findByLoginId(signInRequestDTO.getLoginId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.LOGIN_ID_NOT_MATCHED));

        // 로그인 비밀번호가 일치하지 않으면 에러 발생
        if (!bCryptPasswordEncoder.matches(signInRequestDTO.getLoginPw(), member.getLoginPw())) {
            throw new MemberHandler(ErrorStatus.LOGIN_PW_NOT_MATCHED);
        }
        // 비활성화된 회원이면 에러 발생
        if (memberRepository.existsByIdAndInactiveIsNotNull(member.getId())) {
            throw new MemberHandler(ErrorStatus.MEMBER_IS_INACTIVE);
        }

        // 토큰 생성
        JwtTokenDTO jwtTokenDTO = jwtGenerator.generateToken(member.getLoginId());

        // Refresh Token이 DB에 존재하면 업데이트, 존재하지 않으면 새로 생성
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByLoginId(member.getLoginId());
        if (optionalRefreshToken.isPresent()) {
            RefreshToken refreshToken = optionalRefreshToken.get().updateToken(jwtTokenDTO.getRefreshToken());
            refreshTokenRepository.save(refreshToken);
        } else {
            refreshTokenRepository.save(RefreshToken.builder()
                    .refreshToken(jwtTokenDTO.getRefreshToken())
                    .loginId(member.getLoginId())
                    .build());
        }

        // 헤더에 토큰 삽입
        setHeader(jwtTokenDTO, response);

        // 최근활동순은 아무리 생각해도 최근 로그인이 맞지않나.. 싶습니다
        member.setLastLoginAt(LocalDateTime.now());

        return SignDTO.SignInDTO.toDTO(member);
    }

    private void setHeader(JwtTokenDTO jwtTokenDTO, HttpServletResponse response) {
        response.addHeader("Access-Token", jwtTokenDTO.getAccessToken());
        response.addHeader("Refresh-Token", jwtTokenDTO.getRefreshToken());
    }

    @Override
    public SignDTO.SignOutDTO signOut(HttpServletRequest request) {

        String accessToken = jwtProvider.resolveToken(request);

        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // SecurityContextHolder에 저장된 로그인 정보 삭제
        SecurityContextHolder.clearContext();

        // accessToken을 블랙리스트에 추가
        tokenBlackListRepository.addTokenToBlackList(accessToken);

        // DB에 저장된 Refresh Token 삭제
        RefreshToken refreshToken = refreshTokenRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.LOGIN_ID_NOT_MATCHED));
        refreshTokenRepository.delete(refreshToken);

        return SignDTO.SignOutDTO.toDTO(member);
    }

    @Override
    public MemberDTO.MemberPreviewDTO findId(HttpServletRequest request, VerificationRequestDTO.IdVerificationDTO idVerificationDTO) {

        String tempToken = jwtProvider.resolveToken(request);
        SecurityUtils.checkTempAuthorization();

        // 인증 코드 확인
        if (!idVerificationDTO.getVerificationCode().equals(verificationCodeRepository.getVerificationCode(tempToken))) {
            throw new MemberHandler(ErrorStatus.MAIL_NOT_VERIFIED);
        }

        Member member = memberRepository.findByNameAndEmail(idVerificationDTO.getName(), idVerificationDTO.getEmail())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 임시 인증 정보 삭제
        verificationCodeRepository.removeVerificationCode(tempToken);

        return MemberDTO.MemberPreviewDTO.toDTO(member);

    }

    @Override
    public MemberDTO.MemberPreviewDTO updatePw(HttpServletRequest request, VerificationRequestDTO.PwVerificationDTO pwVerificationDTO) {

        String tempToken = jwtProvider.resolveToken(request);
        SecurityUtils.checkTempAuthorization();

        // 인증 코드 확인
        if (!pwVerificationDTO.getVerificationCode().equals(verificationCodeRepository.getVerificationCode(tempToken))) {
            throw new MemberHandler(ErrorStatus.MAIL_NOT_VERIFIED);
        }

        // 비밀번호 확인
        if (!pwVerificationDTO.getPassword().equals(pwVerificationDTO.getPasswordConfirmation())) {
            throw new MemberHandler(ErrorStatus.MEMBER_PW_NOT_MATCHED);
        }

        Member member = memberRepository.findByLoginId(pwVerificationDTO.getLoginId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.changeLoginPw(pwVerificationDTO.getPassword(), bCryptPasswordEncoder);

        memberRepository.save(member);

        // 임시 인증 정보 삭제
        verificationCodeRepository.removeVerificationCode(tempToken);

        return MemberDTO.MemberPreviewDTO.toDTO(member);
    }

}
