package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class SignDTO {

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SignInDTO {

        private final Long memberId;
        private final String loginId;
        private final String nickname;
        private final Role role;

        public static SignInDTO toDTO(Member member) {
            return SignInDTO.builder()
                    .memberId(member.getId())
                    .loginId(member.getLoginId())
                    .nickname(member.getNickname())
                    .role(member.getRole())
                    .build();
        }
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SignOutDTO {

        private final Long memberId;
        private final String nickname;

        public static SignOutDTO toDTO(Member member) {
            return SignOutDTO.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .build();
        }
    }
}
