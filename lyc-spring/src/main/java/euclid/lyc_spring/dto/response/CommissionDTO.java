package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.CommissionClothes;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.commission_info.*;
import euclid.lyc_spring.domain.chat.commission.commission_style.*;
import euclid.lyc_spring.domain.enums.*;
import lombok.*;

import java.time.LocalDate;
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
    public static class CommissionInfoDTO{
        private final Long commissionId;
        private final CommissionStatus status;
        private final LocalDateTime createdDate;
        private final CommissionInfoDetailDTO commissionInfo;
        private final CommissionStyleDTO commissionStyle;
        private final CommissionOtherDTO commissionOther;

        @Builder(access = AccessLevel.PRIVATE)
        public CommissionInfoDTO(Long commissionId, CommissionStatus status, LocalDateTime createdDate, CommissionInfoDetailDTO commissionInfo, CommissionStyleDTO commissionStyle, CommissionOtherDTO commissionOther) {
            this.commissionId = commissionId;
            this.status = status;
            this.createdDate = createdDate;
            this.commissionInfo = commissionInfo;
            this.commissionStyle = commissionStyle;
            this.commissionOther = commissionOther;
        }

        public static CommissionInfoDTO toDTO(Commission commission) {
            return CommissionInfoDTO.builder()
                    .commissionId(commission.getId())
                    .createdDate(commission.getCreatedAt())
                    .status(commission.getStatus())
                    .commissionInfo(CommissionInfoDetailDTO.toDTO(commission))
                    .commissionStyle(CommissionStyleDTO.toDTO(commission))
                    .commissionOther(CommissionOtherDTO.toDTO(commission))
                    .build();
        }
    }

    @Getter
    public static class CommissionOtherDTO {
        private final LocalDate dateToUse;
        private final LocalDate desiredDate;
        private final Integer minPrice;
        private final Integer maxPrice;
        private final String text;

        @Builder(access = AccessLevel.PRIVATE)
        public CommissionOtherDTO(LocalDate dateToUse, LocalDate desiredDate, Integer minPrice, Integer maxPrice, String text) {
            this.dateToUse = dateToUse;
            this.desiredDate = desiredDate;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            this.text = text;
        }

        public static CommissionOtherDTO toDTO(Commission commission) {
            return CommissionOtherDTO.builder()
                    .dateToUse(commission.getDateToUse())
                    .desiredDate(commission.getDesiredDate())
                    .maxPrice(commission.getMaxPrice())
                    .minPrice(commission.getMinPrice())
                    .text(commission.getText())
                    .build();
        }
    }

    @Getter
    public static class CommissionInfoDetailDTO{
        private final Short height;
        private final Short weight;
        private final TopSize topSize;
        private final BottomSize bottomSize;
        private final String text;
        private final List<StyleDTO> commissionInfoStyleList;
        private final List<BodyTypeDTO> commissionInfoBodyTypeList;
        private final List<FitDTO> commissionInfoFitList;
        private final List<MaterialDTO> commissionInfoMaterialList;

        @Builder(access = AccessLevel.PRIVATE)
        public CommissionInfoDetailDTO(Short height, Short weight, TopSize topSize, BottomSize bottomSize, String text
                , List<StyleDTO> commissionInfoStyleList, List<BodyTypeDTO> commissionInfoBodyTypeList, List<FitDTO> commissionInfoFitList, List<MaterialDTO> commissionInfoMaterialList) {
            this.height = height;
            this.weight = weight;
            this.topSize = topSize;
            this.bottomSize = bottomSize;
            this.text = text;
            this.commissionInfoStyleList = commissionInfoStyleList;
            this.commissionInfoBodyTypeList = commissionInfoBodyTypeList;
            this.commissionInfoFitList = commissionInfoFitList;
            this.commissionInfoMaterialList = commissionInfoMaterialList;
        }

        public static CommissionInfoDetailDTO toDTO(Commission commission) {
            return CommissionInfoDetailDTO.builder()
                    .height(commission.getHeight())
                    .weight(commission.getWeight())
                    .topSize(commission.getTopSize())
                    .bottomSize(commission.getBottomSize())
                    .text(commission.getText())
                    .commissionInfoStyleList(commission.getStyles()
                            .stream().map(StyleDTO::toDTO).toList())
                    .commissionInfoBodyTypeList(commission.getBodyTypes()
                            .stream().map(BodyTypeDTO::toDTO).toList())
                    .commissionInfoMaterialList(commission.getMaterials()
                            .stream().map(MaterialDTO::toDTO).toList())
                    .commissionInfoFitList(commission.getFits()
                            .stream().map(FitDTO::toDTO).toList())
                    .build();
        }
    }


    @Getter
    public static class CommissionStyleDTO {
        private final List<StyleDTO> style;
        private final List<MaterialDTO> material;
        private final List<FitDTO> fit;
        private final List<ColorDTO> color;

        @Builder(access = AccessLevel.PRIVATE)
        public CommissionStyleDTO(List<StyleDTO> style, List<MaterialDTO> material, List<FitDTO> fit, List<ColorDTO> color) {
            this.style = style;
            this.material = material;
            this.fit = fit;
            this.color = color;
        }

        public static CommissionStyleDTO toDTO(Commission commission) {
            return CommissionStyleDTO.builder()
                    .style(commission.getHopeStyles().stream().map(StyleDTO:: toDTO).toList())
                    .fit(commission.getHopeFits().stream().map(FitDTO :: toDTO).toList())
                    .color(commission.getHopeColors().stream().map(ColorDTO :: toDTO).toList())
                    .material(commission.getHopeMaterials().stream().map(MaterialDTO::toDTO).toList())
                    .build();
        }
    }

    @Getter
    public static class StyleDTO{
        private final Style style;
        private final Boolean isPrefer;

        @Builder(access = AccessLevel.PRIVATE)
        public StyleDTO(Style style, Boolean isPrefer) {
            this.style = style;
            this.isPrefer = isPrefer;
        }

        public static StyleDTO toDTO(CommissionHopeStyle style) {
            return StyleDTO.builder()
                    .style(style.getStyle())
                    .isPrefer(style.getIsPrefer())
                    .build();
        }

        public static StyleDTO toDTO(CommissionStyle style){
            return StyleDTO.builder()
                    .style(style.getStyle())
                    .isPrefer(style.getIsPrefer())
                    .build();
        }
    }

    @Getter
    public static class MaterialDTO{
        private final Material material;
        private final Boolean isPrefer;

        @Builder(access = AccessLevel.PRIVATE)
        public MaterialDTO(Material material, Boolean isPrefer) {
            this.material = material;
            this.isPrefer = isPrefer;
        }

        public static MaterialDTO toDTO(CommissionHopeMaterial material) {
            return MaterialDTO.builder()
                    .material(material.getMaterial())
                    .isPrefer(material.getIsPrefer())
                    .build();
        }

        public static MaterialDTO toDTO(CommissionMaterial material){
            return MaterialDTO.builder()
                    .material(material.getMaterial())
                    .isPrefer(material.getIsPrefer())
                    .build();
        }
    }

    @Getter
    public static class FitDTO{
        private final Fit fit;
        private final Boolean isPrefer;

        @Builder(access = AccessLevel.PRIVATE)
        public FitDTO(Fit fit, Boolean isPrefer) {
            this.fit = fit;
            this.isPrefer = isPrefer;
        }

        public static FitDTO toDTO(CommissionHopeFit fit) {
            return FitDTO.builder()
                    .fit(fit.getFit())
                    .isPrefer(fit.getIsPrefer())
                    .build();
        }

        public static FitDTO toDTO(CommissionFit fit) {
            return FitDTO.builder()
                    .fit(fit.getFit())
                    .isPrefer(fit.getIsPrefer())
                    .build();
        }

    }

    @Getter
    public static class ColorDTO{
        private final Color color;
        private final Boolean isPrefer;

        @Builder(access = AccessLevel.PRIVATE)
        public ColorDTO(Color color, Boolean isPrefer) {
            this.color = color;
            this.isPrefer = isPrefer;
        }

        public static ColorDTO toDTO(CommissionHopeColor color) {
            return ColorDTO.builder()
                    .color(color.getColor())
                    .isPrefer(color.getIsPrefer())
                    .build();
        }

    }

    @Getter
    public static class BodyTypeDTO{
        private final BodyType bodyType;
        private final Boolean isGood;

        @Builder(access = AccessLevel.PRIVATE)
        public BodyTypeDTO(BodyType bodyType, Boolean isGood) {
            this.bodyType = bodyType;
            this.isGood = isGood;
        }

        public static BodyTypeDTO toDTO(CommissionBodyType bodyType) {
            return BodyTypeDTO.builder()
                    .bodyType(bodyType.getBodyType())
                    .isGood(bodyType.getIsGood())
                    .build();
        }
    }
}
