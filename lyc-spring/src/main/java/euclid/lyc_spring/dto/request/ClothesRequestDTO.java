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

        private Long memberId;
        private String image;
        private String text;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClothesByTextDTO {

        private Long memberId;
        private String name;
        private Material material;
        private Fit fit;
    }
}