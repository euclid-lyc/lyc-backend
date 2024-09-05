package euclid.lyc_spring.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.ErrorReasonDTO;
import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.dto.token.JwtTokenDTO;
import euclid.lyc_spring.repository.token.TokenBlackListRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtGenerator jwtGenerator;
    private final TokenBlackListRepository tokenBlackListRepository;

/* ---------------------------------------- 요청이 서버로 들어올 때마다 필터 실행 ---------------------------------------- */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 필터가 적용되지 않는 URI
        if (request.getRequestURI().startsWith("/lyc/auths/sign-up") || request.getRequestURI().startsWith("/lyc/auths/sign-in") ||
            request.getRequestURI().startsWith("/swagger-ui") || request.getRequestURI().startsWith("/v3/api-docs") ||
            request.getRequestURI().startsWith("/ws/lyc")) {
            filterChain.doFilter(request, response);
            return ;
        }

        // 임시 토큰이 적용되는 URI
        if (request.getRequestURI().equals("/lyc/auths/find-id") || request.getRequestURI().equals("/lyc/auths/find-pw")) {
            String accessToken = jwtProvider.resolveToken(request);
            if (accessToken != null && jwtProvider.validateToken(accessToken)) {
                setAuthentication(accessToken);
            } else {
                sendResponse(response, ErrorStatus.JWT_INVALID_TOKEN.getReasonHttpStatus());
                return ;
            }
            filterChain.doFilter(request, response);
            return ;
        }

        // Request Header에서 Authorization(토큰) 추출
        String accessToken = jwtProvider.resolveToken(request);

        if (accessToken != null) {
            // 요청 헤더에 access 토큰 값이 존재
            if (jwtProvider.validateToken(accessToken)) {
                if (!tokenBlackListRepository.isBlackListed(accessToken)) {
                    // access 토큰이 유효하면 보안 컨텍스트에 인증 정보 저장
                    setAuthentication(accessToken);
                }else {
                    // access 토큰이 유효하지만 로그아웃 되었을 경우(블랙리스트에 존재하는 경우)
                    sendResponse(response, ErrorStatus.JWT_ACCESS_TOKEN_EXPIRED.getReasonHttpStatus());
                    return ;
                }
            } else {
                // access 토큰이 유효하지 않으면 refresh 토큰 확인
                if (jwtProvider.validateRefreshToken(accessToken)) {
                    // access 토큰은 유효하지 않지만 refresh 토큰이 유효하다면 새로운 token 발급 후 저장
                    String loginId = jwtProvider.getClaims(accessToken).getSubject();
                    JwtTokenDTO newAccessTokenDTO = jwtGenerator.generateToken(loginId);
                    jwtProvider.updateRefreshToken(newAccessTokenDTO.getRefreshToken());
                    // 새로운 access 토큰과 refresh 토큰을 담아 응답
                    setHeader(newAccessTokenDTO, response);
                    sendResponse(response, ErrorStatus.JWT_ACCESS_TOKEN_EXPIRED.getReasonHttpStatus());
                    return ;
                } else {
                    sendResponse(response, ErrorStatus.JWT_INVALID_TOKEN.getReasonHttpStatus());
                    return ;
                }
            }
        } else {
            sendResponse(response, ErrorStatus.JWT_NULL_TOKEN.getReasonHttpStatus());
            return ;
        }

        filterChain.doFilter(request, response);
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

    private void sendResponse(HttpServletResponse response, ErrorReasonDTO status) throws IOException {
        ApiResponse<Void> apiResponse = new ApiResponse<>(Boolean.FALSE, status.getCode(), status.getMessage());

        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.getWriter().write(jsonResponse);
    }

}

