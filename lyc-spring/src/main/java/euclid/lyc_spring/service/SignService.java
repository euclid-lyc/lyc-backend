package euclid.lyc_spring.service;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.JwtGenerator;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.dto.request.SignRequestDTO;
import euclid.lyc_spring.dto.response.SignDTO;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignService {

    private final JwtGenerator jwtGenerator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    public SignDTO.SignInDTO signIn(SignRequestDTO.SignInDTO signInRequestDTO) {

        Member member = memberRepository.findByLoginId(signInRequestDTO.getLoginId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.LOGIN_ID_NOT_MATCHED));

        if (!bCryptPasswordEncoder.matches(signInRequestDTO.getLoginPw(), member.getLoginPw())) {
            throw new MemberHandler(ErrorStatus.LOGIN_PW_NOT_MATCHED);
        }

        return SignDTO.SignInDTO.toDTO(member, jwtGenerator.generateToken(signInRequestDTO.getLoginId(), member.getRole()).getAccessToken());
    }
}
