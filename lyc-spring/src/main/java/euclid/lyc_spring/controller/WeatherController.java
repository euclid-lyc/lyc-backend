package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.response.WeatherDTO;
import euclid.lyc_spring.service.social.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Weather", description = "날씨 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc")
public class WeatherController {

    private final WeatherService weatherService;

    @Operation(summary = "[구현완료] 오늘 날씨 불러오기", description = "오늘 날씨의 최고 기온과 최저 기온을 불러옵니다.")
    @GetMapping("/weather")
    public ApiResponse<WeatherDTO> getTodayWeather(@RequestParam String city) {
        WeatherDTO weatherInfoDTO = weatherService.getTodayWeather(city);
        return ApiResponse.onSuccess(SuccessStatus._WEATHER_FETCHED, weatherInfoDTO);
    }
}
