package euclid.lyc_spring.dto.request;

import euclid.lyc_spring.domain.enums.*;
import lombok.*;

import java.util.ArrayList;
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

        // test
        public InfoStyleListDTO(List<Style> styles) {
            this.preferredStyleList = styles;
            this.nonPreferredStyleList = styles;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InfoFitListDTO {

        private List<Fit> preferredFitList;
        private List<Fit> nonPreferredFitList;

        // test
        public InfoFitListDTO(List<Fit> Fits) {
            this.preferredFitList = Fits;
            this.nonPreferredFitList = Fits;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InfoMaterialListDTO {

        private List<Material> preferredMaterialList;
        private List<Material> nonPreferredMaterialList;

        // test
        public <E> InfoMaterialListDTO(List<Material> es) {
            this.preferredMaterialList = es;
            this.nonPreferredMaterialList = es;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InfoBodyTypeListDTO {

        private List<BodyType> goodBodyTypeList;
        private List<BodyType> badBodyTypeList;

        //test
        public <E> InfoBodyTypeListDTO(List<BodyType> es) {
        }
    }

}