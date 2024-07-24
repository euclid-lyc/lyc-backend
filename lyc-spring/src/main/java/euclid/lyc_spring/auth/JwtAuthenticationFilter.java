package euclid.lyc_spring.auth;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.JwtHandler;
import euclid.lyc_spring.dto.token.JwtTokenDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;
    private final JwtGenerator jwtGenerator;

/* ---------------------------------------- 요청이 서버로 들어올 때마다 필터 실행 ---------------------------------------- */

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String requestURI = request.getRequestURI();

        // 필터가 적용되지 않는 URI
        if (requestURI.equals("/api/register") || requestURI.equals("/api/sign-in") ||
            requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs")) {
            chain.doFilter(req, res);
            return ;
        }

        // Request Header에서 Authorization(토큰) 추출
        String accessToken = jwtProvider.resolveToken(request);

        if (accessToken != null) {
            // 요청 헤더에 access 토큰 값이 존재
             if (jwtProvider.validateToken(accessToken)) {
                 // access 토큰이 유효하면 보안 컨텍스트에 인증 정보 저장
                 setAuthentication(accessToken);
             } else {
                 // access 토큰이 유효하지 않으면 refresh 토큰 확인
                 if (jwtProvider.validateRefreshToken(accessToken)) {
                     // access 토큰은 유효하지 않지만 refresh 토큰이 유효하다면 새로운 token 발급 후 저장
                     String loginId = jwtProvider.getClaims(accessToken).getSubject();
                     JwtTokenDTO newAccessTokenDTO = jwtGenerator.generateToken(loginId);
                     jwtProvider.updateRefreshToken(newAccessTokenDTO.getRefreshToken());
                     // 헤더에 토큰 삽입 후 보안 컨텍스트에 인증 정보 저장
                     setHeader(newAccessTokenDTO, response);
                     setAuthentication(accessToken);
                 } else {
                     // access 토큰과 refresh 토큰 모두 유효하지 않다면 에러 발생
                     throw new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN);
                 }
             }
        } else {
            throw new JwtHandler(ErrorStatus.JWT_NULL_TOKEN);
        }

        chain.doFilter(req, res);
    }

    // 헤더에 토큰을 삽입하는 함수
    private void setHeader(JwtTokenDTO jwtTokenDTO, HttpServletResponse response) {
        response.addHeader("Access-Token", jwtTokenDTO.getAccessToken());
        response.addHeader("Refresh-Token", jwtTokenDTO.getRefreshToken());
    }

    // 보안 컨텍스트에 인증 정보를 저장하는 함수
    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}

