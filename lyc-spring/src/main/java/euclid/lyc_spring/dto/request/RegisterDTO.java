package euclid.lyc_spring.dto.request;

import euclid.lyc_spring.dto.request.InfoRequestDTO.*;
import euclid.lyc_spring.dto.request.MemberRequestDTO.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class RegisterDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterMemberDTO {

        private MemberInfoDTO memberInfo;
        private BasicInfoDTO basicInfo;
    }

}
