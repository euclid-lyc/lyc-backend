package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.response.WeatherDTO;
import euclid.lyc_spring.dto.response.WeatherDTO.*;
import euclid.lyc_spring.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Operation(summary = "오늘 날씨 불러오기", description = "오늘 날씨의 최고 기온과 최저 기온을 불러옵니다.")
    @GetMapping("/api/weather")
    public ApiResponse<WeatherDTO> getTodayWeather(@RequestParam String city) {
        WeatherDTO weatherInfoDTO = weatherService.getTodayWeather(city);
        return ApiResponse.onSuccess(SuccessStatus._WEATHER_FETCHED, weatherInfoDTO);
    }
}
