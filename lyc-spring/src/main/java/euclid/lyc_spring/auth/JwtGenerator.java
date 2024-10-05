package euclid.lyc_spring.auth;

import euclid.lyc_spring.dto.token.JwtTokenDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtGenerator {

    private final SecretKey key;

    public JwtGenerator(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Base64로 인코딩된 서버의 Secret Key를 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytes); // 디코딩된 Secret Key를 해싱하여 저장
    }

    public JwtTokenDTO generateToken(String loginId) {

        Date now = new Date();

        Date accessTokenExpiredAt = new Date(now.getTime() + JwtConstant.ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now.getTime() + JwtConstant.REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .issuer("euclid-lyc")
                .subject(loginId)
                .issuedAt(now)
                .expiration(accessTokenExpiredAt)
                .signWith(key) // Access Token에 digital signature 포함
                .compact();

        String refreshToken = Jwts.builder()
                .issuer("euclid-lyc")
                .subject(loginId)
                .issuedAt(now)
                .expiration(refreshTokenExpiredAt)
                .signWith(key) // Refresh Token에 digital signature 포함
                .compact();

        return JwtTokenDTO.toDTO(accessToken, refreshToken);

    }

    public String generateTempToken(String email) {

        Date now = new Date();
        Date tempTokenExpiredAt = new Date(now.getTime() + JwtConstant.TEMP_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .issuer("euclid-lyc")
                .subject(email)
                .issuedAt(now)
                .expiration(tempTokenExpiredAt)
                .signWith(key) // Temp Token에 digital signature 포함
                .compact();
    }

}
