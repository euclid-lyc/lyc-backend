package euclid.lyc_spring.dto;

import euclid.lyc_spring.domain.enums.BottomSize;
import euclid.lyc_spring.domain.enums.TopSize;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InfoDTO {
    @Getter
    public static class BasicInfoDTO {
        private Short height;
        private Short weight;
        private TopSize topSize;
        private BottomSize bottomSize;
        private Integer postalCode;
        private String address;
        private String detailAddress;
        private String text;

        @Builder(access = AccessLevel.PRIVATE)
        private BasicInfoDTO(Short height, Short weight, TopSize topSize, BottomSize bottomSize,
                             Integer postalCode, String address, String detailAddress, String text) {
            this.height = height;
            this.weight = weight;
            this.topSize = topSize;
            this.bottomSize = bottomSize;
            this.postalCode = postalCode;
            this.address = address;
            this.detailAddress = detailAddress;
            this.text = text;
        }

    }
}
