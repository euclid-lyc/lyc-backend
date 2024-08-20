package euclid.lyc_spring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SignRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInDTO {
        private String loginId;
        private String loginPw;
    }
}
