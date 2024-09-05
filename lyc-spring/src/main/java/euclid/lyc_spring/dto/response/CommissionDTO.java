package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.CommissionOther;
import euclid.lyc_spring.domain.chat.commission.commission_info.*;
import euclid.lyc_spring.domain.chat.commission.commission_style.*;
import euclid.lyc_spring.domain.enums.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CommissionDTO {

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
                    .commissionInfo(CommissionInfoDetailDTO.toDTO(commission.getCommissionInfo()))
                    .commissionStyle(CommissionStyleDTO.toDTO(commission.getCommissionStyle()))
                    .commissionOther(CommissionOtherDTO.toDTO(commission.getCommissionOther()))
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

        public static CommissionOtherDTO toDTO(CommissionOther commissionOther) {
            return CommissionOtherDTO.builder()
                    .dateToUse(commissionOther.getDateToUse())
                    .desiredDate(commissionOther.getDesiredDate())
                    .maxPrice(commissionOther.getMaxPrice())
                    .minPrice(commissionOther.getMinPrice())
                    .text(commissionOther.getText())
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

        public static CommissionInfoDetailDTO toDTO(CommissionInfo commissionInfo) {
            return CommissionInfoDetailDTO.builder()
                    .height(commissionInfo.getHeight())
                    .weight(commissionInfo.getWeight())
                    .topSize(commissionInfo.getTopSize())
                    .bottomSize(commissionInfo.getBottomSize())
                    .text(commissionInfo.getText())
                    .commissionInfoStyleList(commissionInfo.getCommissionInfoStyleList()
                            .stream().map(StyleDTO::toDTO).toList())
                    .commissionInfoBodyTypeList(commissionInfo.getCommissionInfoBodyTypeList()
                            .stream().map(BodyTypeDTO::toDTO).toList())
                    .commissionInfoMaterialList(commissionInfo.getCommissionInfoMaterialList()
                            .stream().map(MaterialDTO::toDTO).toList())
                    .commissionInfoFitList(commissionInfo.getCommissionInfoFitList()
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

        public static CommissionStyleDTO toDTO(CommissionStyle commissionStyle) {
            return CommissionStyleDTO.builder()
                    .style(commissionStyle.getStyle().stream().map(StyleDTO:: toDTO).toList())
                    .fit(commissionStyle.getFit().stream().map(FitDTO :: toDTO).toList())
                    .color(commissionStyle.getColor().stream().map(ColorDTO :: toDTO).toList())
                    .material(commissionStyle.getMaterial().stream().map(MaterialDTO::toDTO).toList())
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

        public static StyleDTO toDTO(CommissionStyleStyle style) {
            return StyleDTO.builder()
                    .style(style.getStyle())
                    .isPrefer(style.getIsPrefer())
                    .build();
        }

        public static StyleDTO toDTO(CommissionInfoStyle style){
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

        public static MaterialDTO toDTO(CommissionStyleMaterial material) {
            return MaterialDTO.builder()
                    .material(material.getMaterial())
                    .isPrefer(material.getIsPrefer())
                    .build();
        }

        public static MaterialDTO toDTO(CommissionInfoMaterial material){
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

        public static FitDTO toDTO(CommissionStyleFit fit) {
            return FitDTO.builder()
                    .fit(fit.getFit())
                    .isPrefer(fit.getIsPrefer())
                    .build();
        }

        public static FitDTO toDTO(CommissionInfoFit fit) {
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

        public static ColorDTO toDTO(CommissionStyleColor color) {
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

        public static BodyTypeDTO toDTO(CommissionInfoBodyType bodyType) {
            return BodyTypeDTO.builder()
                    .bodyType(bodyType.getBodyType())
                    .isGood(bodyType.getIsGood())
                    .build();
        }
    }
}
