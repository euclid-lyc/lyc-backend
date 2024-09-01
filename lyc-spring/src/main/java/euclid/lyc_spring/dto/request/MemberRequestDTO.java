package euclid.lyc_spring.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Getter
public class MemberRequestDTO {

    @Getter
    public static class MemberDTO {

        private final String name;
        private final String loginId;
        private final String loginPw;
        private final String loginPwCheck;
        private final String email;
        private final String phone;
        private final String nickname;
        private final String introduction;

        public MemberDTO(
                String name, String loginId, String loginPw, String loginPwCheck,
                String email, String phone, String nickname, String introduction) {
            this.name = name;
            this.loginId = loginId;
            this.loginPw = loginPw;
            this.loginPwCheck = loginPwCheck;
            this.email = email;
            this.phone = phone;
            this.nickname = nickname;
            this.introduction = introduction;
        }
    }
}