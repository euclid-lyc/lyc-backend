package euclid.lyc_spring.dto.request;

import euclid.lyc_spring.domain.enums.Color;
import euclid.lyc_spring.domain.enums.Fit;
import euclid.lyc_spring.domain.enums.Material;
import euclid.lyc_spring.domain.enums.Style;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class StyleRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StyleDTO{
        private String occasion;
        private StyleListDTO styleList;
        private FitListDTO fitList;
        private MaterialListDTO materialList;
        private ColorListDTO colorList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StyleListDTO{
        private List<Style> styleList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FitListDTO{
        private List<Fit> fitList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaterialListDTO{
        private List<Material> materialList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColorListDTO{
        private List<Color> colorList;
    }
}
