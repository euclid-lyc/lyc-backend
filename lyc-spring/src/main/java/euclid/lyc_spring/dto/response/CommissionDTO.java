package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.CommissionClothes;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.enums.*;
import euclid.lyc_spring.dto.request.InfoDTO;
import euclid.lyc_spring.dto.request.StyleDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class CommissionDTO {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class TerminatedCommissionListDTO {
        private final List<TerminatedCommissionDTO> commissions;

        public static TerminatedCommissionListDTO toDTO(List<Commission> commissions) {
            return TerminatedCommissionListDTO.builder()
                    .commissions(commissions.stream()
                            .map(TerminatedCommissionDTO::toDTO)
                            .toList())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class TerminatedCommissionDTO {

        private final Long commissionId;
        private final String profileImage;
        private final String nickname;
        private final LocalDateTime finishedAt;

        public static TerminatedCommissionDTO toDTO(Commission commission) {
            return TerminatedCommissionDTO.builder()
                    .commissionId(commission.getId())
                    .profileImage(commission.getDirector().getProfileImage())
                    .nickname(commission.getDirector().getNickname())
                    .finishedAt(commission.getFinishedAt())
                    .build();
        }
    }

    @Getter
    public static class CommissionViewDTO {

        private final Long commissionId;
        private final String profileImage;
        private final String nickname;
        private final String loginId;
        private final LocalDateTime createdAt;


        @Builder(access = AccessLevel.PRIVATE)
        private CommissionViewDTO(Long commissionId, String profileImage,
                                  String nickname, String loginId, LocalDateTime createdAt) {
            this.profileImage = profileImage;
            this.nickname = nickname;
            this.loginId = loginId;
            this.commissionId = commissionId;
            this.createdAt = createdAt;
        }

        public static CommissionViewDTO toDTO(Commission commission) {
            Member member = commission.getMember();
            return CommissionViewDTO.builder()
                    .profileImage(member.getProfileImage())
                    .nickname(member.getNickname())
                    .loginId(member.getLoginId())
                    .commissionId(commission.getId())
                    .createdAt(commission.getCreatedAt())
                    .build();
        }
    }

    @Getter
    public static class ClothesViewDTO{
        private final Long clothesId;
        private final String imageUrl;
        private final String clothesUrl;

        @Builder(access = AccessLevel.PRIVATE)
        public ClothesViewDTO(Long clothesId, String imageUrl, String clothesUrl) {
            this.clothesId = clothesId;
            this.imageUrl = imageUrl;
            this.clothesUrl = clothesUrl;
        }

        public static ClothesViewDTO toDTO(CommissionClothes commissionClothes) {
            return ClothesViewDTO.builder()
                    .clothesId(commissionClothes.getId())
                    .imageUrl(commissionClothes.getImage())
                    .clothesUrl(commissionClothes.getUrl())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class CommissionInfoDTO{
        private final Long commissionId;
        private final LocalDateTime createdDate;
        private final CommissionStatus status;
        private final CommissionDetailDTO commission;

        public static CommissionInfoDTO toDTO(Commission commission) {
            return CommissionInfoDTO.builder()
                    .commissionId(commission.getId())
                    .createdDate(commission.getCreatedAt())
                    .status(commission.getStatus())
                    .commission(CommissionDetailDTO.toDTO(commission))
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class CommissionDetailDTO {
        private final Long directorId;
        private final InfoDTO.BasicInfoDTO basicInfo;
        private final StyleDTO.StyleInfoDTO style;
        private final InfoDTO.OtherMattersDTO otherMatters;

        public static CommissionDetailDTO toDTO(Commission commission) {
            return CommissionDetailDTO.builder()
                    .directorId(commission.getDirector().getId())
                    .basicInfo(InfoDTO.BasicInfoDTO.basicInfoDTO(commission))
                    .style(StyleDTO.StyleInfoDTO.toDTO(commission))
                    .otherMatters(InfoDTO.OtherMattersDTO.toDTO(commission))
                    .build();
        }
    }
}
