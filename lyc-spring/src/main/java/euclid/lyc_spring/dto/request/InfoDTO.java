package euclid.lyc_spring.dto.request;

import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionBodyType;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionFit;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionMaterial;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionStyle;
import euclid.lyc_spring.domain.enums.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
public class InfoDTO {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class StyleInfoDTO {
        private Boolean isPublic;
        private Short height;
        private Short weight;
        private TopSize topSize;
        private BottomSize bottomSize;
        private List<Style> preferredStyleList;
        private List<Style> nonPreferredStyleList;
        private List<Material> preferredMaterialList;
        private List<Material> nonPreferredMaterialList;
        private List<Fit> preferredFitList;
        private List<Fit> nonPreferredFitList;
        private List<BodyType> goodBodyTypeList;
        private List<BodyType> badBodyTypeList;
        private String details;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BasicInfoDTO {
        private Short height;
        private Short weight;
        private TopSize topSize;
        private BottomSize bottomSize;
        private String text;
        private Integer postalCode;
        private String address;
        private String detailAddress;
        private InfoStyleListDTO infoStyle;
        private InfoFitListDTO infoFit;
        private InfoMaterialListDTO infoMaterial;
        private InfoBodyTypeListDTO infoBodyType;

        public static BasicInfoDTO basicInfoDTO(Commission commission) {
            return BasicInfoDTO.builder()
                    .height(commission.getHeight())
                    .weight(commission.getWeight())
                    .topSize(commission.getTopSize())
                    .bottomSize(commission.getBottomSize())
                    .text(commission.getText())
                    .infoStyle(InfoStyleListDTO.toDTO(commission))
                    .infoFit(InfoFitListDTO.toDTO(commission))
                    .infoMaterial(InfoMaterialListDTO.toDTO(commission))
                    .infoBodyType(InfoBodyTypeListDTO.toDTO(commission))
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InfoStyleListDTO {

        private List<Style> preferredStyleList;
        private List<Style> nonPreferredStyleList;

        public static InfoStyleListDTO toDTO(Commission commission) {
            return InfoStyleListDTO.builder()
                    .preferredStyleList(commission.getStyles().stream()
                            .filter(CommissionStyle::getIsPrefer)
                            .map(CommissionStyle::getStyle)
                            .toList())
                    .nonPreferredStyleList(commission.getStyles().stream()
                            .filter(commissionStyle -> !commissionStyle.getIsPrefer())
                            .map(CommissionStyle::getStyle)
                            .toList())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InfoFitListDTO {

        private List<Fit> preferredFitList;
        private List<Fit> nonPreferredFitList;

        public static InfoFitListDTO toDTO(Commission commission) {
            return InfoFitListDTO.builder()
                    .preferredFitList(commission.getFits().stream()
                            .filter(CommissionFit::getIsPrefer)
                            .map(CommissionFit::getFit)
                            .toList())
                    .nonPreferredFitList(commission.getFits().stream()
                            .filter(commissionFit -> !commissionFit.getIsPrefer())
                            .map(CommissionFit::getFit)
                            .toList())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InfoMaterialListDTO {

        private List<Material> preferredMaterialList;
        private List<Material> nonPreferredMaterialList;

        public static InfoMaterialListDTO toDTO(Commission commission) {
            return InfoMaterialListDTO.builder()
                    .preferredMaterialList(commission.getMaterials().stream()
                            .filter(CommissionMaterial::getIsPrefer)
                            .map(CommissionMaterial::getMaterial)
                            .toList())
                    .nonPreferredMaterialList(commission.getMaterials().stream()
                            .filter(commissionMaterial -> !commissionMaterial.getIsPrefer())
                            .map(CommissionMaterial::getMaterial)
                            .toList())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InfoBodyTypeListDTO {

        private List<BodyType> goodBodyTypeList;
        private List<BodyType> badBodyTypeList;

        public static InfoBodyTypeListDTO toDTO(Commission commission) {
            return InfoBodyTypeListDTO.builder()
                    .goodBodyTypeList(commission.getBodyTypes().stream()
                            .filter(CommissionBodyType::getIsGood)
                            .map(CommissionBodyType::getBodyType)
                            .toList())
                    .badBodyTypeList(commission.getBodyTypes().stream()
                            .filter(commissionBodyType -> !commissionBodyType.getIsGood())
                            .map(CommissionBodyType::getBodyType)
                            .toList())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class OtherMattersDTO {
        private Integer minPrice;
        private Integer maxPrice;
        private LocalDate dateToUse;
        private LocalDate desiredDate;
        private String text;
        private Boolean isShared;

        public static OtherMattersDTO toDTO(Commission commission) {
            return OtherMattersDTO.builder()
                    .minPrice(commission.getMinPrice())
                    .maxPrice(commission.getMaxPrice())
                    .dateToUse(commission.getDateToUse())
                    .desiredDate(commission.getDesiredDate())
                    .text(commission.getText())
                    .isShared(commission.getIsShared())
                    .build();
        }
    }
}