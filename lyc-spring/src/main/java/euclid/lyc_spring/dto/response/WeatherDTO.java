package euclid.lyc_spring.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class WeatherDTO {
    private Double temp_min;
    private Double temp_max;

    public WeatherDTO(Double tempMin, Double tempMax) {
        this.temp_min = tempMin;
        this.temp_max = tempMax;
    }
}


