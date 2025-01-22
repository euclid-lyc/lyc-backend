package euclid.lyc_spring.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class RegisterDTO {

    @Getter
    @RequiredArgsConstructor
    public static class RegisterMemberDTO {
        private final String verificationCode;
        private final MemberRequestDTO.MemberDTO member;
        private final InfoDTO.BasicInfoDTO info;
        private final MemberRequestDTO.PushSetDTO pushSet;
    }
}