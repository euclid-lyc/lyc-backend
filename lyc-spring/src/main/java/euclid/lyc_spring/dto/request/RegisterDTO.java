package euclid.lyc_spring.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
public class RegisterDTO {

    @Getter
    public static class RegisterMemberDTO {
        private final MemberRequestDTO.MemberDTO member;
        private final InfoRequestDTO.BasicInfoDTO info;
        private final MemberRequestDTO.PushSetDTO pushSet;

        public RegisterMemberDTO(MemberRequestDTO.MemberDTO member, InfoRequestDTO.BasicInfoDTO info, MemberRequestDTO.PushSetDTO pushSet) {
            this.member = member;
            this.info = info;
            this.pushSet = pushSet;
        }
    }
}