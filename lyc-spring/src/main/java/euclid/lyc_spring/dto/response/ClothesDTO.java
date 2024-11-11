package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ClothesHandler;
import euclid.lyc_spring.domain.clothes.Clothes;
import euclid.lyc_spring.dto.response.MemberDTO.*;
import euclid.lyc_spring.domain.enums.Fit;
import euclid.lyc_spring.domain.enums.Material;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
                    .text(clothes.getText())
                    .build();
        }
    }

    @Getter
    public static class ClothesTextResponseDTO {

        private final Long memberId;
        private final Long clothesId;
        private final Long clothesTextId;
        private final String title;
        private final Material material;
        private final Fit fit;
        private final String text;

        @Builder(access = AccessLevel.PRIVATE)
        private ClothesTextResponseDTO(Long memberId, Long clothesId, Long clothesTextId,
                                       String title, Material material, Fit fit, String text) {
            this.memberId = memberId;
            this.clothesId = clothesId;
            this.clothesTextId = clothesTextId;
            this.title = title;
            this.material = material;
            this.fit = fit;
            this.text = text;
        }

        public static ClothesTextResponseDTO toDTO(Clothes clothes) {
            return ClothesTextResponseDTO.builder()
                    .memberId(clothes.getMember().getId())
                    .clothesId(clothes.getId())
                    .clothesTextId(clothes.getClothesText().getId())
                    .title(clothes.getTitle())
                    .material(clothes.getClothesText().getMaterial())
                    .fit(clothes.getClothesText().getFit())
                    .text(clothes.getText())
                    .build();
        }

    }

    @Getter
    public static class ClothesInfoDTO{
        private final Long clothesId;
        private final String image;
        private final String title;
        private final LocalDateTime createdAt;

        @Builder(access = AccessLevel.PRIVATE)
        private ClothesInfoDTO(Long clothesId, String image, String title, LocalDateTime createdAt) {
            this.clothesId = clothesId;
            this.image = image;
            this.title = title;
            this.createdAt = createdAt;
        }

        public static ClothesInfoDTO toDTO(Clothes clothes) {
            return ClothesInfoDTO.builder()
                    .clothesId(clothes.getId())
                    .image(clothes.getClothesImage() != null ? clothes.getClothesImage().getImage() : null)
                    .title(clothes.getTitle())
                    .createdAt(clothes.getCreatedAt())
                    .build();
        }
    }

    @Getter
    public static class ClothesListDTO{

        private final Long memberId;
        private final List<ClothesInfoDTO> clothesList;

        @Builder
        public ClothesListDTO(Long memberId, List<ClothesInfoDTO> clothesList) {
            this.memberId = memberId;
            this.clothesList = clothesList;
        }
    }

    @Getter
    public static class ClothesViewDTO{

        private final MemberProfileDTO member;
        private final Long clothesId;
        private final String title;
        private final String text;
        private final Fit fit;
        private final Material material;
        private final String image;

        @Builder(access = AccessLevel.PRIVATE)
        private ClothesViewDTO(MemberProfileDTO member, Long clothesId,
                               String title, String text,
                               Fit fit, Material material, String image) {
            this.member = member;
            this.clothesId = clothesId;
            this.title = title;
            this.text = text;
            this.fit = fit;
            this.material = material;
            this.image = image;
        }

        public static ClothesViewDTO toDTO(Clothes clothes){
            return ClothesViewDTO.builder()
                    .member(MemberProfileDTO.toDTO(clothes.getMember()))
                    .clothesId(clothes.getId())
                    .title(clothes.getTitle())
                    .text(clothes.getText())
                    .fit(clothes.getClothesText() != null ? clothes.getClothesText().getFit() : null)
                    .material(clothes.getClothesText() != null ? clothes.getClothesText().getMaterial() : null)
                    .image(clothes.getClothesImage() != null ? clothes.getClothesImage().getImage() : null)
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ClothesPreviewDTO {

        private final Long clothesId;
        private final String title;

        public static ClothesPreviewDTO toDTO(Clothes clothes){
            return ClothesPreviewDTO.builder()
                    .clothesId(clothes.getId())
                    .title(clothes.getTitle())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ClothesWithImageDTO {

        private final MemberProfileDTO member;
        private final String title;
        private final String text;
        private final String image;

        public static ClothesWithImageDTO toDTO(Clothes clothes){
            return ClothesWithImageDTO.builder()
                    .member(MemberProfileDTO.toDTO(clothes.getMember()))
                    .title(clothes.getTitle())
                    .text(clothes.getText())
                    .image(clothes.getClothesImage().getImage())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ClothesWithTextDTO {
        private final MemberProfileDTO member;
        private final String title;
        private final Material material;
        private final Fit fit;
        private final String detail;

        public static ClothesWithTextDTO toDTO(Clothes clothes){
            return ClothesWithTextDTO.builder()
                    .member(MemberProfileDTO.toDTO(clothes.getMember()))
                    .title(clothes.getTitle())
                    .material(clothes.getClothesText().getMaterial())
                    .fit(clothes.getClothesText().getFit())
                    .detail(clothes.getText())
                    .build();
        }
    }
}
