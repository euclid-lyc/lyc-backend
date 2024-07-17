package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.posting.Posting;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostingDTO {

    @Getter
    public static class PostingImageDTO {

        private final Long postingId;
        private final String image;

        @Builder(access = AccessLevel.PRIVATE)
        private PostingImageDTO(Long postingId, String image) {
            this.postingId = postingId;
            this.image = image;
        }

        public static PostingImageDTO toDTO(Posting posting) {
            return PostingImageDTO.builder()
                    .postingId(posting.getId())
                    .image(posting.getImageList().isEmpty() ? "" : posting.getImageList().get(0).getImage())
                    .build();
        }
    }

    @Getter
    public static class PostingImageListDTO {

        private final Long memberId;
        private final List<PostingImageDTO> imageList;

        @Builder
        public PostingImageListDTO(Long memberId, List<PostingImageDTO> imageList) {
            this.memberId = memberId;
            this.imageList = imageList;
        }

    }

}
