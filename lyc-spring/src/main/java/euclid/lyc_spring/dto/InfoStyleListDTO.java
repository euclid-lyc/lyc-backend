package euclid.lyc_spring.dto;

import euclid.lyc_spring.domain.enums.Style;
import lombok.Getter;

import java.util.List;

@Getter
public class InfoStyleListDTO {
    private List<Style> infoStyleList;
    private List<Boolean> isPreferInfoStyle;
}
