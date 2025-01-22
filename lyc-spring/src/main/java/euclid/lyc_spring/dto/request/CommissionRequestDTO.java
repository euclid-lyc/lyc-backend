package euclid.lyc_spring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CommissionRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommissionDTO {

        private Long directorId;
        private InfoDTO.BasicInfoDTO basicInfo;
        private StyleDTO.StyleInfoDTO style;
        private InfoDTO.OtherMattersDTO otherMatters;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClothesDTO {
        private String image;
        private String url;
    }
}
