package euclid.lyc_spring.dto.request;

import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionHopeColor;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionHopeFit;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionHopeMaterial;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionHopeStyle;
import euclid.lyc_spring.domain.enums.Color;
import euclid.lyc_spring.domain.enums.Fit;
import euclid.lyc_spring.domain.enums.Material;
import euclid.lyc_spring.domain.enums.Style;
import lombok.*;

import java.util.List;

public class StyleDTO {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class StyleInfoDTO {
        private StyleListDTO styleList;
        private FitListDTO fitList;
        private MaterialListDTO materialList;
        private ColorListDTO colorList;

        public static StyleInfoDTO toDTO(Commission commission){
            return StyleInfoDTO.builder()
                    .styleList(StyleListDTO.toDTO(commission))
                    .fitList(FitListDTO.toDTO(commission))
                    .materialList(MaterialListDTO.toDTO(commission))
                    .colorList(ColorListDTO.toDTO(commission))
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class StyleListDTO{
        private List<Style> styleList;
        public static StyleListDTO toDTO(Commission commission) {
            return StyleListDTO.builder()
                    .styleList(commission.getHopeStyles().stream()
                            .map(CommissionHopeStyle::getStyle)
                            .toList())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class FitListDTO{
        private List<Fit> fitList;
        public static FitListDTO toDTO(Commission commission) {
            return FitListDTO.builder()
                    .fitList(commission.getHopeFits().stream()
                            .map(CommissionHopeFit::getFit)
                            .toList())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MaterialListDTO{
        private List<Material> materialList;
        public static MaterialListDTO toDTO(Commission commission) {
            return MaterialListDTO.builder()
                    .materialList(commission.getHopeMaterials().stream()
                            .map(CommissionHopeMaterial::getMaterial)
                            .toList())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ColorListDTO{
        private List<Color> colorList;
        public static ColorListDTO toDTO(Commission commission) {
            return ColorListDTO.builder()
                    .colorList(commission.getHopeColors().stream()
                            .map(CommissionHopeColor::getColor)
                            .toList())
                    .build();
        }
    }
}
