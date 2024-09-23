package euclid.lyc_spring.dto.request;

import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.dto.request.ImageRequestDTO.*;
import lombok.*;

import java.util.List;

@Getter
public class PostingRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostingSaveDTO {

        private Double minTemp;
        private Double maxTemp;
        private Style style;
        private String content;
        private Long fromMemberId;
        private Long toMemberId;
        private Long writerId;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class ImageSaveDTO {
        private final String imageUrl;
        private final List<String> links;
    }
}