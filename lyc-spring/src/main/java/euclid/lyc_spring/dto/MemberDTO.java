package euclid.lyc_spring.dto;

import lombok.Getter;
import org.springframework.web.bind.annotation.RequestBody;


@Getter
public class MemberDTO {
    private String name;
    private String loginId;
    private String loginPw;
    private String email;
    private String nickname;
    private String introduction;
    private String profileImage;
}
