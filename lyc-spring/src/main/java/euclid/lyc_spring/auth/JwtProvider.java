package euclid.lyc_spring.auth;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.JwtHandler;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey key;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Base64로 인코딩된 서버의 Secret Key를 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytes); // 디코딩된 Secret Key를 해싱하여 저장
    }

/* ---------------------------------------- 클라이언트 인가 요청 처리 ---------------------------------------- */

    // Authorization 요청 헤더 값 불러오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;

    }

    // accessToken 검증하기(인증 정보 위조 여부 확인)
    public Boolean validateToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken);
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            throw new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN);
        }
    }

    // 인증 정보 생성
    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaims(accessToken);
        String loginId = claims.getSubject();
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new UsernamePasswordAuthenticationToken(loginId, "", authorities);
    }

    // 토큰 Payload 불러오기
    private Claims getClaims(String accessToken) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }


}
