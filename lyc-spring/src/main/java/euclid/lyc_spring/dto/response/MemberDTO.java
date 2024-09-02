package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class MemberDTO {

    @Getter
    public static class TodayDirectorDTO {

        private final Long memberId;
        private final String nickname;
        private final String profileImage;
        private final Long follower;

        @Builder(access = AccessLevel.PRIVATE)
        private TodayDirectorDTO(Long memberId, String nickname, String profileImage, Long follower) {
            this.memberId =memberId;
            this.nickname = nickname;
            this.profileImage = profileImage;
            this.follower = follower;
        }

        public static TodayDirectorDTO toDTO(Member member) {
            return TodayDirectorDTO.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .profileImage(member.getProfileImage())
                    .follower(member.getFollower())
                    .build();
        }
    }

    @Getter
    public static class FollowDTO {

        private final Long memberId;
        private final String nickname;
        private final String profileImage;
        private final String introduction;

        @Builder(access = AccessLevel.PRIVATE)
        private FollowDTO(Long memberId, String nickname, String profileImage, String introduction) {
            this.memberId = memberId;
            this.nickname = nickname;
            this.profileImage = profileImage;
            this.introduction = introduction;
        }

        public static FollowDTO toDTO(Member member) {
            return FollowDTO.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .profileImage(member.getProfileImage())
                    .introduction(member.getIntroduction())
                    .build();
        }
    }

    @Getter
    public static class MemberProfileDTO {

        private final Long memberId;
        private final String profileImage;

        @Builder(access = AccessLevel.PRIVATE)
        private MemberProfileDTO(Long memberId, String profileImage) {
            this.memberId = memberId;
            this.profileImage = profileImage;
        }

        public static MemberProfileDTO toDTO(Member member) {
            return MemberProfileDTO.builder()
                    .memberId(member.getId())
                    .profileImage(member.getProfileImage())
                    .build();
        }
    }

    @Getter
    public static class MemberInfoDTO {

        private final Long memberId;
        private final String nickname;
        private final String loginId;
        private final Long follower;
        private final Long following;
        private final String profileImage;

        @Builder(access = AccessLevel.PRIVATE)
        private MemberInfoDTO(Long memberId, String nickname, String loginId,
                              Long follower, Long following, String profileImage) {
            this.memberId = memberId;
            this.nickname = nickname;
            this.loginId = loginId;
            this.follower = follower;
            this.following = following;
            this.profileImage = profileImage;
        }

        public static MemberInfoDTO toDTO(Member member) {
            return MemberInfoDTO.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .loginId(member.getLoginId())
                    .follower(member.getFollower())
                    .following(member.getFollowing())
                    .profileImage(member.getProfileImage())
                    .build();
        }
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MemberPreviewDTO {

        private final Long memberId;
        private final String loginId;
        private final String profileImage;
        private final String nickname;

        public static MemberPreviewDTO toDTO(Member member) {
            return MemberPreviewDTO.builder()
                    .memberId(member.getId())
                    .loginId(member.getLoginId())
                    .profileImage(member.getProfileImage())
                    .nickname(member.getNickname())
                    .build();
        }
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MemberSettingInfoDTO {
        private final Long memberId;
        private final String loginId;
        private final String profileImage;
        private final String nickname;
        private final String introduction;

        public static MemberSettingInfoDTO toDTO(Member member) {
            return MemberSettingInfoDTO.builder()
                    .memberId(member.getId())
                    .loginId(member.getLoginId())
                    .nickname(member.getNickname())
                    .profileImage(member.getProfileImage())
                    .introduction(member.getIntroduction())
                    .build();
        }
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AdrressDTO{
        private final Integer postalCode;
        private final String address;
        private final String detailAddress;

        public static AdrressDTO toDTO(Member member) {
            return AdrressDTO.builder()
                    .postalCode(member.getInfo().getPostalCode())
                    .address(member.getInfo().getAddress())
                    .detailAddress(member.getInfo().getDetailAddress())
                    .build();
        }
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PushSetDTO{
        private final Boolean dm;
        private final Boolean feed;
        private final Boolean schedule;
        private final Boolean likeMark;
        private final Boolean event;
        private final Boolean ad;

        public static PushSetDTO toDTO(Member member) {
            return PushSetDTO.builder()
                    .dm(member.getPushSet().getDm())
                    .feed(member.getPushSet().getFeed())
                    .schedule(member.getPushSet().getSchedule())
                    .likeMark(member.getPushSet().getLikeMark())
                    .event(member.getPushSet().getEvent())
                    .ad(member.getPushSet().getAd())
                    .build();
        }
    }
}
