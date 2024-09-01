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

        private String directorId;
        private InfoRequestDTO.BasicInfoDTO basicInfo;
        private StyleRequestDTO.StyleDTO style;
        private InfoRequestDTO.OtherMattersDTO otherMatters;
    }
}
