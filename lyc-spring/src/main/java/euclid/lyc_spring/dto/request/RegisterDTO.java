package euclid.lyc_spring.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
public class RegisterDTO {

    @Getter
    @RequiredArgsConstructor
    public static class RegisterMemberDTO {
        private final String verificationCode;
        private final MemberRequestDTO.MemberDTO member;
        private final InfoRequestDTO.BasicInfoDTO info;
        private final MemberRequestDTO.PushSetDTO pushSet;
    }
}