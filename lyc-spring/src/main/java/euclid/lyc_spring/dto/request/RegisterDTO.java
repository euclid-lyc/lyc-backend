package euclid.lyc_spring.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class RegisterDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterMemberDTO {

        private MemberRequestDTO.MemberDTO member;
        private InfoRequestDTO.BasicInfoDTO info;
    }
}