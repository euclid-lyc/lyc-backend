package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.PointUsage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PointResDTO {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class MemberPointDTO {
        private final String nickname;
        private final String loginId;
        private final Integer point;

        public static MemberPointDTO toDTO (Member member) {
            return MemberPointDTO.builder()
                    .nickname(member.getNickname())
                    .loginId(member.getLoginId())
                    .point(member.getPoint())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class UsageListDTO {
        private final String nickname;
        private final String loginId;
        private final List<UsageDTO> usages;

        public static UsageListDTO toDTO (Member member) {
            return UsageListDTO.builder()
                    .nickname(member.getNickname())
                    .loginId(member.getLoginId())
                    .usages(member.getPointUsageList().stream()
                            .map(UsageDTO::toDTO)
                            .toList())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class UsageDTO {
        private final String name;
        private final Integer amount;
        private final String description;
        private final LocalDateTime usedAt;

        public static UsageDTO toDTO (PointUsage pointUsage) {
            return UsageDTO.builder()
                    .name(pointUsage.getName())
                    .amount(pointUsage.getAmount())
                    .description(pointUsage.getDescription())
                    .usedAt(pointUsage.getCreatedAt())
                    .build();
        }
    }
}
