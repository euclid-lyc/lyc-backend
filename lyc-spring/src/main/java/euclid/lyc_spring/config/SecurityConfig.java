package euclid.lyc_spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable) // disable basic auth
                .csrf(AbstractHttpConfigurer::disable) // disable csrf
                .formLogin(AbstractHttpConfigurer::disable) // disable form login
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // use stateless session
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/register").permitAll() // 회원가입에만 모든 요청 허가
                        .anyRequest().hasAuthority("ROLE_MEMBER")) // 나머지 요청은 멤버 권한이 있어야 허가
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class) // JWT 인증 필터 처리 -> 패스워드 인증 필터 처리
                .requiresChannel(requiresChannel -> requiresChannel // 모든 요청을 https로 강제
                        .anyRequest().requiresSecure())
                .build();

    }
}
