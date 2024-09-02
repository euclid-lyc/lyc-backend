package euclid.lyc_spring.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class VerificationRequestDTO {

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class FindIdDTO {
        private final String name;
        private final String email;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class FindPwDTO {
        private final String name;
        private final String email;
        private final String loginId;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class IdVerificationDTO {
        private final String name;
        private final String email;
        private final String verificationCode;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class PwVerificationDTO {
        private final String loginId;
        private final String password;
        private final String passwordConfirmation;
        private final String verificationCode;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class ChangePasswordDTO {
        private final String oldPassword;
        private final String newPassword;
        private final String confirmPassword;
    }
}
