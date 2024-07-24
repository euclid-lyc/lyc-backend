package euclid.lyc_spring.auth;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.JwtHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

/* ---------------------------------------- 요청이 서버로 들어올 때마다 필터 실행 ---------------------------------------- */

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String requestURI = request.getRequestURI();

        if ("/api/register".equals(requestURI) || "/api/sign-in".equals(requestURI)) {
            System.out.println("Skipping JwtAuthenticationFilter for: " + requestURI);
            chain.doFilter(req, res);
            return ;
        }

        // Request Header에서 Authorization(토큰) 추출
        String accessToken = jwtProvider.resolveToken(request);
        if (accessToken != null && jwtProvider.validateToken(accessToken)) {
            // request 토큰이 유효하면 보안 컨텍스트에 인증 정보 저장
            Authentication authentication = jwtProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            throw new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN);
        }

        chain.doFilter(req, res);
    }
}

