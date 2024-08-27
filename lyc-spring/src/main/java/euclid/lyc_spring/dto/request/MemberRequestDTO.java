package euclid.lyc_spring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
public class MemberRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDTO {

        private String name;
        private String loginId;
        private String loginPw;
        private String loginPwCheck;
        private String email;
        private String phone;
        private String nickname;
        private String introduction;
        private String profileImage;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberAuthDTO {

        private String name;
        private AuthMethod method;
        private String authInfo;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPwAuthDTO {

        private String name;
        private String loginId;
        private AuthMethod method;
        private String authInfo;
    }

    public enum AuthMethod {
        EMAIL, PHONE
    }

}