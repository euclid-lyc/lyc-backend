package euclid.lyc_spring.service.auth;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.JwtGenerator;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.RefreshToken;
import euclid.lyc_spring.domain.enums.Role;
import euclid.lyc_spring.domain.info.*;
import euclid.lyc_spring.dto.request.InfoRequestDTO;
import euclid.lyc_spring.dto.request.MemberRequestDTO;
import euclid.lyc_spring.dto.request.RegisterDTO;
import euclid.lyc_spring.dto.request.SignRequestDTO;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.dto.response.SignDTO;
import euclid.lyc_spring.dto.token.JwtTokenDTO;
import euclid.lyc_spring.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    private final InfoRepository infoRepository;
    private final InfoStyleRepository infoStyleRepository;
    private final InfoFitRepository infoFitRepository;
    private final InfoMaterialRepository infoMaterialRepository;
    private final InfoBodyTypeRepository infoBodyTypeRepository;

    private final JwtGenerator jwtGenerator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

/*-------------------------------------------------- 로그인 및 로그아웃 --------------------------------------------------*/

    @Override
    @Transactional
    public MemberDTO.MemberInfoDTO join(RegisterDTO.RegisterMemberDTO registerMemberDTO) {

        MemberRequestDTO.MemberInfoDTO memberInfoDTO = registerMemberDTO.getMemberInfo();
        InfoRequestDTO.BasicInfoDTO basicInfoDTO = registerMemberDTO.getBasicInfo();

        String image = memberInfoDTO.getProfileImage();

        if(memberInfoDTO.getProfileImage().isEmpty())
            image = "default url";

        // 이미 회원가입이 되어있음
        if (memberRepository.findByEmail(memberInfoDTO.getEmail()).isPresent()) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_EXIST);
        }
        // 중복된 아이디가 있음
        if (memberRepository.findByLoginId(memberInfoDTO.getLoginId()).isPresent()) {
            throw new MemberHandler(ErrorStatus.MEMBER_DUPLICATED_LOGIN_ID);
        }
        // 비밀번호와 비밀번호 확인이 일치하지 않음
        if (!memberInfoDTO.getLoginPw().equals(memberInfoDTO.getLoginPwCheck())) {
            throw new MemberHandler(ErrorStatus.MEMBER_INVALID_LOGIN_PW);
        }

        Member member = Member.builder()
                .name(memberInfoDTO.getName())
                .loginId(memberInfoDTO.getLoginId())
                .loginPw(bCryptPasswordEncoder.encode(memberInfoDTO.getLoginPw()))
                .email(memberInfoDTO.getEmail())
                .nickname(memberInfoDTO.getNickname())
                .introduction(memberInfoDTO.getIntroduction())
                .profileImage(image)
                .role(Role.MEMBER)
                .build();

        member = memberRepository.save(member);

        createInfo(member, basicInfoDTO);

        return MemberDTO.MemberInfoDTO.toDTO(member);

    }

    private void createInfo(Member member, InfoRequestDTO.BasicInfoDTO infoDto) {

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

        member.setInfo(info);
        info = infoRepository.save(info);

        createInfoStyle(info, infoDto.getInfoStyle());
        createInfoFit(info, infoDto.getInfoFit());
        createInfoMaterial(info, infoDto.getInfoMaterial());
        createInfoBodyType(info, infoDto.getInfoBodyType());

    }

    private void createInfoStyle(Info info, InfoRequestDTO.InfoStyleListDTO infoStyleListDTO) {

        infoStyleListDTO.getPreferredStyleList()
                .forEach(style -> {
                    InfoStyle infoStyle = InfoStyle.builder()
                            .style(style)
                            .isPrefer(true)
                            .build();
                    info.addInfoStyle(infoStyle);
                    infoStyleRepository.save(infoStyle);
                });

        infoStyleListDTO.getNonPreferredStyleList()
                .forEach(style -> {
                    InfoStyle infoStyle = InfoStyle.builder()
                            .style(style)
                            .isPrefer(false)
                            .build();
                    info.addInfoStyle(infoStyle);
                    infoStyleRepository.save(infoStyle);
                });
    }

    private void createInfoFit(Info info, InfoRequestDTO.InfoFitListDTO infoFitListDTO) {

        infoFitListDTO.getPreferredFitList()
                .forEach(style -> {
                    InfoFit infoFit = InfoFit.builder()
                            .fit(style)
                            .isPrefer(true)
                            .build();
                    info.addInfoFit(infoFit);
                    infoFitRepository.save(infoFit);
                });

        infoFitListDTO.getNonPreferredFitList()
                .forEach(style -> {
                    InfoFit infoFit = InfoFit.builder()
                            .fit(style)
                            .isPrefer(false)
                            .build();
                    info.addInfoFit(infoFit);
                    infoFitRepository.save(infoFit);
                });
    }

    private void createInfoMaterial(Info info, InfoRequestDTO.InfoMaterialListDTO infoMaterialListDTO) {

        infoMaterialListDTO.getPreferredMaterialList()
                .forEach(material -> {
                    InfoMaterial infoMaterial = InfoMaterial.builder()
                            .material(material)
                            .isPrefer(true)
                            .build();
                    info.addInfoMaterial(infoMaterial);
                    infoMaterialRepository.save(infoMaterial);
                });

        infoMaterialListDTO.getNonPreferredMaterialList()
                .forEach(material -> {
                    InfoMaterial infoMaterial = InfoMaterial.builder()
                            .material(material)
                            .isPrefer(false)
                            .build();
                    info.addInfoMaterial(infoMaterial);
                    infoMaterialRepository.save(infoMaterial);
                });
    }

    private void createInfoBodyType(Info info, InfoRequestDTO.InfoBodyTypeListDTO infoBodyTypeListDTO) {

        infoBodyTypeListDTO.getGoodBodyTypeList()
                .forEach(bodyType -> {
                    InfoBodyType infoBodyType = InfoBodyType.builder()
                            .bodyType(bodyType)
                            .isGood(true)
                            .build();
                    info.addInfoBodyType(infoBodyType);
                    infoBodyTypeRepository.save(infoBodyType);
                });

        infoBodyTypeListDTO.getBadBodyTypeList()
                .forEach(bodyType -> {
                    InfoBodyType infoBodyType = InfoBodyType.builder()
                            .bodyType(bodyType)
                            .isGood(false)
                            .build();
                    info.addInfoBodyType(infoBodyType);
                    infoBodyTypeRepository.save(infoBodyType);
                });
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

        return SignDTO.SignInDTO.toDTO(member);
    }

    private void setHeader(JwtTokenDTO jwtTokenDTO, HttpServletResponse response) {
        response.addHeader("Access-Token", jwtTokenDTO.getAccessToken());
        response.addHeader("Refresh-Token", jwtTokenDTO.getRefreshToken());
    }

}
