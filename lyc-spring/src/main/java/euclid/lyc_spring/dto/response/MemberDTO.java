package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

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
    public static class MemberInfoDTO {

        private final Long memberId;
        private final String profileImage;

        @Builder(access = AccessLevel.PRIVATE)
        private MemberInfoDTO(Long memberId, String profileImage) {
            this.memberId = memberId;
            this.profileImage = profileImage;
        }

        public static MemberInfoDTO toDTO(Member member) {
            return MemberInfoDTO.builder()
                    .memberId(member.getId())
                    .profileImage(member.getProfileImage())
                    .build();
        }
    }
}

