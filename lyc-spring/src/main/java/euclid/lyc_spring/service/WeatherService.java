package euclid.lyc_spring.service;

import euclid.lyc_spring.dto.response.WeatherDTO;
import euclid.lyc_spring.dto.response.WeatherDTO.*;
import euclid.lyc_spring.dto.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherDTO getTodayWeather(String city) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

        if (response != null && response.getMain() != null) {
            Double tempMin = response.getMain().getTempMin();
            Double tempMax = response.getMain().getTempMax();
            return new WeatherDTO(tempMin, tempMax);
        } else {
            return null; // or throw an exception if preferred
        }
    }
}