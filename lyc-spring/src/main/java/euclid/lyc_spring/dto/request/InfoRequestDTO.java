package euclid.lyc_spring.dto.request;

import euclid.lyc_spring.domain.enums.*;
import lombok.*;

import java.util.List;

@Getter
public class InfoRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicInfoDTO {

        private Short height;
        private Short weight;
        private TopSize topSize;
        private BottomSize bottomSize;
        private Integer postalCode;
        private String address;
        private String detailAddress;
        private String text;
        private InfoStyleListDTO infoStyle;
        private InfoFitListDTO infoFit;
        private InfoMaterialListDTO infoMaterial;
        private InfoBodyTypeListDTO infoBodyType;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InfoStyleListDTO {

        private List<Style> preferredStyleList;
        private List<Style> nonPreferredStyleList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InfoFitListDTO {

        private List<Fit> preferredFitList;
        private List<Fit> nonPreferredFitList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InfoMaterialListDTO {

        private List<Material> preferredMaterialList;
        private List<Material> nonPreferredMaterialList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InfoBodyTypeListDTO {

        private List<BodyType> goodBodyTypeList;
        private List<BodyType> badBodyTypeList;
    }

}