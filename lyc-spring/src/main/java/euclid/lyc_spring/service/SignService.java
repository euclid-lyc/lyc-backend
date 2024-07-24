package euclid.lyc_spring.service;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.JwtGenerator;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.RefreshToken;
import euclid.lyc_spring.dto.request.SignRequestDTO;
import euclid.lyc_spring.dto.response.SignDTO;
import euclid.lyc_spring.dto.token.JwtTokenDTO;
import euclid.lyc_spring.repository.MemberRepository;
import euclid.lyc_spring.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignService {

    private final JwtGenerator jwtGenerator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private void setHeader(JwtTokenDTO jwtTokenDTO, HttpServletResponse response) {
        response.addHeader("Access-Token", jwtTokenDTO.getAccessToken());
        response.addHeader("Refresh-Token", jwtTokenDTO.getRefreshToken());
    }

    @Transactional
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


}
