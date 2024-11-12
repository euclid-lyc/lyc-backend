package euclid.lyc_spring.dto.request;

import euclid.lyc_spring.domain.enums.*;
import euclid.lyc_spring.domain.info.*;
import euclid.lyc_spring.dto.response.InfoResponseDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
public class InfoRequestDTO {

    @Getter
    @RequiredArgsConstructor
    public static class StyleInfoDTO {
        private final Boolean isPublic;
        private final Short height;
        private final Short weight;
        private final TopSize topSize;
        private final BottomSize bottomSize;
        private final List<Style> preferredStyleList;
        private final List<Style> nonPreferredStyleList;
        private final List<Material> preferredMaterialList;
        private final List<Material> nonPreferredMaterialList;
        private final List<Fit> preferredFitList;
        private final List<Fit> nonPreferredFitList;
        private final List<BodyType> goodBodyTypeList;
        private final List<BodyType> badBodyTypeList;
        private final String details;
    }

    @Getter
    @RequiredArgsConstructor
    public static class BasicInfoDTO {

        private final Short height;
        private final Short weight;
        private final TopSize topSize;
        private final BottomSize bottomSize;
        private final Integer postalCode;
        private final String address;
        private final String detailAddress;
        private final String text;
        private final InfoStyleListDTO infoStyle;
        private final InfoFitListDTO infoFit;
        private final InfoMaterialListDTO infoMaterial;
        private final InfoBodyTypeListDTO infoBodyType;
    }

    @Getter
    @RequiredArgsConstructor
    public static class InfoStyleListDTO {

        private final List<Style> preferredStyleList;
        private final List<Style> nonPreferredStyleList;
    }

    @Getter
    @RequiredArgsConstructor
    public static class InfoFitListDTO {

        private final List<Fit> preferredFitList;
        private final List<Fit> nonPreferredFitList;
    }

    @Getter
    @RequiredArgsConstructor
    public static class InfoMaterialListDTO {

        private final List<Material> preferredMaterialList;
        private final List<Material> nonPreferredMaterialList;
    }

    @Getter
    @RequiredArgsConstructor
    public static class InfoBodyTypeListDTO {

        private final List<BodyType> goodBodyTypeList;
        private final List<BodyType> badBodyTypeList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OtherMattersDTO {
        private Integer minPrice;
        private Integer maxPrice;
        private LocalDate dateToUse;
        private LocalDate desiredDate;
        private String text;
        private Boolean isShareClothesList;
    }
}