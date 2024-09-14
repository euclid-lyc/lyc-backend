package euclid.lyc_spring.dto.request;

import lombok.*;


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

    @Getter
    public static class MemberSettingInfoDTO{
        private final String profileImage;
        private final String nickname;
        private final String introduction;
        private final String loginId;


        public MemberSettingInfoDTO(String profileImage, String nickname,
                                    String introduction, String loginId) {
            this.profileImage = profileImage;
            this.nickname = nickname;
            this.introduction = introduction;
            this.loginId = loginId;
        }
    }

    @Getter
    public static class AddressReqDTO {
        private final Integer postalCode;
        private final String address;
        private final String detailAddress;

        public AddressReqDTO(Integer postalCode, String address, String detailAddress) {
            this.postalCode = postalCode;
            this.address = address;
            this.detailAddress = detailAddress;
        }
    }

    @Getter
    public static class PushSetDTO{
        private final Boolean dm;
        private final Boolean feed;
        private final Boolean schedule;
        private final Boolean likeMark;
        private final Boolean event;
        private final Boolean ad;


        public PushSetDTO(Boolean dm, Boolean feed, Boolean schedule, Boolean likeMark, Boolean event, Boolean ad) {
            this.dm = dm;
            this.feed = feed;
            this.schedule = schedule;
            this.likeMark = likeMark;
            this.event = event;
            this.ad = ad;
        }
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