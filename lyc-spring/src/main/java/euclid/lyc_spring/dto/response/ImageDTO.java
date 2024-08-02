package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.posting.Image;
import euclid.lyc_spring.domain.posting.ImageUrl;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ImageDTO {

    @Getter
    public static class ImageInfoDTO {
        private final Long imageId;
        private final String image;
        private final List<ImageLinkDTO> linkList;

        @Builder(access = AccessLevel.PRIVATE)
        private ImageInfoDTO(Long imageId, String image, List<ImageLinkDTO> linkList) {
            this.imageId = imageId;
            this.image = image;
            this.linkList = linkList;
        }

        public static ImageInfoDTO toDTO(Image image) {
            return ImageInfoDTO.builder()
                    .imageId(image.getId())
                    .image(image.getImage())
                    .linkList(image.getImageUrlList().stream()
                            .map(ImageLinkDTO::toDTO)
                            .toList())
                    .build();
        }
    }

    @Getter
    public static class ImageLinkDTO {
        private final Long imageUrlId;
        private final String link;

        @Builder(access = AccessLevel.PRIVATE)
        private ImageLinkDTO(Long imageUrlId, String link) {
            this.imageUrlId = imageUrlId;
            this.link = link;
        }

        public static ImageLinkDTO toDTO(ImageUrl imageUrl) {
            return ImageLinkDTO.builder()
                    .imageUrlId(imageUrl.getId())
                    .link(imageUrl.getLink())
                    .build();
        }
    }
}
