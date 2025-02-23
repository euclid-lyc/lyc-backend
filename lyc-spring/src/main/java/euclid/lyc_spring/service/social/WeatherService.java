package euclid.lyc_spring.service.social;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.GeneralException;
import euclid.lyc_spring.converter.GridConverter;
import euclid.lyc_spring.dto.response.WeatherDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;
    private final String baseUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    public WeatherDTO getDailyWeather(double lat, double lon) throws JSONException {
        // 위도와 경도를 격자 좌표로 변환
        GridConverter.LatXLngY grid = GridConverter.convertGRID_GPS(lat, lon);
        int nx = (int) grid.x;
        int ny = (int) grid.y;

        // 기준 일시 설정
        String[] baseDateTime = getBaseTime();
        String baseDate = baseDateTime[0];
        String baseTime = baseDateTime[1];

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 500)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", nx)
                .queryParam("ny", ny);


        String uriString = uriBuilder.toUriString();
        uriString += "&serviceKey=" + apiKey;
        URI uri  = URI.create(uriString);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(uri , String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new GeneralException(ErrorStatus.WEATHER_API_ERROR);
        }

        String responseBody = response.getBody();
        System.out.println("응답 데이터: " + responseBody);

        // JSON 파싱 (예제)
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONObject body = jsonResponse.getJSONObject("response").optJSONObject("body");

        if (body != null) {

            // 'items'에서 'item' 배열 가져오기
            JSONArray itemsArray = body.getJSONObject("items").getJSONArray("item");

            double maxTemp = Double.MIN_VALUE;
            double minTemp = Double.MAX_VALUE;

            // itemsArray를 순회하며 TMX와 TMN 카테고리 값을 찾음
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject item = itemsArray.getJSONObject(i);
                String category = item.getString("category");

                if(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).equals(item.getString("fcstDate"))){
                    if ("TMX".equals(category)) { // 최대 기온
                        double temp = item.getDouble("fcstValue");
                        if (temp > maxTemp) {
                            maxTemp = temp;
                        }
                    } else if ("TMN".equals(category)) { // 최소 기온
                        double temp = item.getDouble("fcstValue");
                        if (temp < minTemp) {
                            minTemp = temp;
                        }
                    }
                }
            }

            return new WeatherDTO(minTemp, maxTemp);
        } else {
            // 오류 처리
            throw new GeneralException(ErrorStatus.WEATHER_JSON_PARSING_ERROR);
        }
    }

    private String[] getBaseTime() {

        // 현재 시각
        LocalDateTime now = LocalDateTime.now();
        // 날씨 발표 시각
        int[] forecastHours = {2, 5, 8, 11, 14, 17, 20, 23};

        // 날씨 제공 시각
        int forecastMin = 10;

        for (int forecastHour : forecastHours) {

            // 02:10, 05:00, 08:10, 11:10, 14:10, 17:10, 20:10, 23:10
            LocalDateTime forecastTime = now
                    .withHour(forecastHour)
                    .withMinute(forecastMin)
                    .withSecond(0)
                    .withNano(0);

            if (now.isAfter(forecastTime)) {
                return new String[]{
                        forecastTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                        String.format("%02d00", forecastHour)
                };
            }
        }

        // 00:00 ~ 02:10 요청은 전날 23시 발표 사용
        LocalDateTime previousDay = now.minusDays(1);
        return new String[]{
                previousDay.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                "2300"
        };
    }
}