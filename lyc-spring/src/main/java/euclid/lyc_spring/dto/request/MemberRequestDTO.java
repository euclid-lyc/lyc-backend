package euclid.lyc_spring.dto.request;

import lombok.*;


@Getter
public class MemberRequestDTO {

    @Getter
    @RequiredArgsConstructor
    public static class MemberDTO {

        private final String name;
        private final String loginId;
        private final String loginPw;
        private final String loginPwCheck;
        private final String email;
        private final String phone;
        private final String nickname;
        private final String introduction;
    }

    @Getter
    @RequiredArgsConstructor
    public static class MemberSettingInfoDTO{
        private final String nickname;
        private final String introduction;
        private final String loginId;
    }

    @Getter
    @RequiredArgsConstructor
    public static class AddressReqDTO {
        private final Integer postalCode;
        private final String address;
        private final String detailAddress;
    }

    @Getter
    @RequiredArgsConstructor
    public static class PushSetDTO{
        private final Boolean dm;
        private final Boolean feed;
        private final Boolean schedule;
        private final Boolean likeMark;
        private final Boolean event;
        private final Boolean ad;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ReportDTO {
        private final Boolean abuse;
        private final Boolean obsceneContent;
        private final Boolean privacy;
        private final Boolean spam;
        private final Boolean infringement;
        private final String description;
    }
}