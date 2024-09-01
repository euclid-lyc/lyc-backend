package euclid.lyc_spring.dto.request;

import euclid.lyc_spring.domain.enums.Fit;
import euclid.lyc_spring.domain.enums.Material;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ClothesRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClothesByImageDTO {

        private String text;
        private String title;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClothesByTextDTO {

        private String title;
        private String text;
        private Material material;
        private Fit fit;
    }
}
