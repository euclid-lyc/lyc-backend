package euclid.lyc_spring.dto.s3;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class S3DTO {

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class ImageListDTO {
        private final List<ImageDTO> imageUrls;
        private final Integer imageCount;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class ImageDTO {
        private final String imageUrl;
        private final LocalDateTime uploadAt;
    }
}
