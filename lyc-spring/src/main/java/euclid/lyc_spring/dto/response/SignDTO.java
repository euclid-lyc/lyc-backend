package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.Role;
import euclid.lyc_spring.dto.token.JwtTokenDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class SignDTO {

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @RequiredArgsConstructor
    public static class SignInDTO {

        private final Long memberId;
        private final String loginId;
        private final String nickname;
        private final Role role;
        private final String token;

        public static SignInDTO toDTO(Member member, String token) {
            return SignInDTO.builder()
                    .memberId(member.getId())
                    .loginId(member.getLoginId())
                    .nickname(member.getNickname())
                    .role(member.getRole())
                    .token(token)
                    .build();
        }
    }
}
