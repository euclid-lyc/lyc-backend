package euclid.lyc_spring.dto.response;

import lombok.Getter;

@Getter
public class WeatherDTO {
    private Double temp_min;
    private Double temp_max;

    public WeatherDTO(Double tempMin, Double tempMax) {
        this.temp_min = tempMin;
        this.temp_max = tempMax;
    }
}


