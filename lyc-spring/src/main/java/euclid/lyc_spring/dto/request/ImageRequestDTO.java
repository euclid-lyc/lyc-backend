package euclid.lyc_spring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
public class ImageRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageSaveDTO {

        private String image;
        private List<String> imageUrlList;

    }
}
