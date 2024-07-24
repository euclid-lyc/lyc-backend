package euclid.lyc_spring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
public class MemberRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfoDTO {

        private String name;
        private String loginId;
        private String loginPw;
        private String loginPwCheck;
        private String email;
        private String nickname;
        private String introduction;
        private String profileImage;
    }

}
