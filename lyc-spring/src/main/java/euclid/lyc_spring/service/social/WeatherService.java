package euclid.lyc_spring.service.social;

import euclid.lyc_spring.converter.GridConverter;
import euclid.lyc_spring.dto.response.WeatherDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;


@Service
public class WeatherService {

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }

    public WeatherDTO getDailyWeather(double lat, double lon) throws JSONException {
        // 위도와 경도를 격자 좌표로 변환
        GridConverter.LatXLngY grid = GridConverter.convertGRID_GPS(lat, lon);
        int nx = (int) grid.x;
        int ny = (int) grid.y;

        String apiKey = "Fugvw/AixcbCyPLZXK3jYerFtWGdWPfBP0Qk0m7Hju2z1S9wWdBdMEg4qeQ4QSBDfesnYLjCbati2fP2gVpnmg==";
        String url = String.format(
                "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=%s&numOfRows=500&dataType=JSON&base_date=%s&base_time=%s&nx=%d&ny=%d",
                apiKey, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                "0200", nx, ny
        );
        System.out.println(url);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String response = responseEntity.getBody();
        System.out.println(response);

        // JSON 응답 처리
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject body = jsonResponse.getJSONObject("response").optJSONObject("body");

        if (body != null) {
            // 'items'에서 'item' 배열 가져오기
            JSONArray itemsArray = body.getJSONObject("items").getJSONArray("item");

            double maxTemp = 0; // 기본값을 NaN으로 설정
            double minTemp = 100; // 기본값을 NaN으로 설정

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
            throw new RuntimeException("날씨 데이터를 가져오는 데 실패했습니다.");
        }
    }
}