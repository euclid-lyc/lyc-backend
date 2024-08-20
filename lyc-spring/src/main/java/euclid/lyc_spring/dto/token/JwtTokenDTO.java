package euclid.lyc_spring.dto.token;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class JwtTokenDTO {

    private final String accessToken;
    private final String refreshToken;

    public static JwtTokenDTO toDTO(String accessToken, String refreshToken) {
        return JwtTokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
