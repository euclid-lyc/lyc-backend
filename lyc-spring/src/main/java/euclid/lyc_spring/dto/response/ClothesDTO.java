package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ClothesHandler;
import euclid.lyc_spring.domain.clothes.Clothes;
import euclid.lyc_spring.domain.enums.Fit;
import euclid.lyc_spring.domain.enums.Material;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClothesDTO {

    @Getter
    public static class ClothesImageResponseDTO {

        private final Long memberId;
        private final Long clothesId;
        private final Long clothesImageId;
        private final String image;
        private final String text;

        @Builder(access = AccessLevel.PRIVATE)
        private ClothesImageResponseDTO(Long memberId, Long clothesId,
                                        Long clothesImageId, String image, String text) {
            this.memberId = memberId;
            this.clothesId = clothesId;
            this.clothesImageId = clothesImageId;
            this.image = image;
            this.text = text;
        }

        public static ClothesImageResponseDTO toDTO(Clothes clothes) {

            if (clothes.getClothesImage() == null) {
                throw new ClothesHandler(ErrorStatus.CLOTHES_TEXT_NOT_FOUND);
            }

            return ClothesImageResponseDTO.builder()
                    .memberId(clothes.getMember().getId())
                    .clothesId(clothes.getId())
                    .clothesImageId(clothes.getClothesImage().getId())
                    .image(clothes.getClothesImage().getImage())
                    .text(clothes.getClothesImage().getText())
                    .build();
        }
    }

    @Getter
    public static class ClothesTextResponseDTO {

        private final Long memberId;
        private final Long clothesId;
        private final Long clothesTextId;
        private final String name;
        private final Material material;
        private final Fit fit;

        @Builder(access = AccessLevel.PRIVATE)
        private ClothesTextResponseDTO(Long memberId, Long clothesId, Long clothesTextId,
                                       String name, Material material, Fit fit) {
            this.memberId = memberId;
            this.clothesId = clothesId;
            this.clothesTextId = clothesTextId;
            this.name = name;
            this.material = material;
            this.fit = fit;
        }

        public static ClothesTextResponseDTO toDTO(Clothes clothes) {
            return ClothesTextResponseDTO.builder()
                    .memberId(clothes.getMember().getId())
                    .clothesId(clothes.getId())
                    .clothesTextId(clothes.getClothesText().getId())
                    .name(clothes.getClothesText().getName())
                    .material(clothes.getClothesText().getMaterial())
                    .fit(clothes.getClothesText().getFit())
                    .build();
        }
    }
}
