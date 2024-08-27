package euclid.lyc_spring.auth;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.JwtHandler;
import euclid.lyc_spring.domain.RefreshToken;
import euclid.lyc_spring.repository.token.RefreshTokenRepository;
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

@Component
public class JwtProvider {

    private final SecretKey key;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Base64로 인코딩된 서버의 Secret Key를 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytes); // 디코딩된 Secret Key를 해싱하여 저장
        this.refreshTokenRepository = refreshTokenRepository;
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

    // accessToken 검증하기(요청으로 받은 토큰이 유효한지 확인)
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 인증 정보 생성
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String loginId = claims.getSubject();
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(new String[]{"MEMBER"})
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new UsernamePasswordAuthenticationToken(loginId, "", authorities);
    }

    // 토큰 Payload 불러오기
    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .clockSkewSeconds(Integer.MAX_VALUE)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN);
        }

    }

    // refreshToken 검증하기
    public Boolean validateRefreshToken(String token) {

        // 로그인한 유저의 refreshToken이 Repository에 존재해야 함 (+ accessToken이 유효하지 않으면 오류)
        RefreshToken refreshToken = refreshTokenRepository.findByLoginId(getClaims(token).getSubject())
                .orElseThrow(() -> new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN));

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(refreshToken.getRefreshToken());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Refresh 토큰 업데이트하기 (입력 : access 토큰이 만료되면서 새로 발급받은 refresh 토큰)
    public void updateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByLoginId(getClaims(token).getSubject())
                .orElseThrow(() -> new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN));

        refreshToken.updateToken(token);
        refreshTokenRepository.save(refreshToken);
    }

}
