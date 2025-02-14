package euclid.lyc_spring.config;

import euclid.lyc_spring.apiPayload.header.HttpHeadersCustom;
import euclid.lyc_spring.auth.JwtAuthenticationFilter;
import euclid.lyc_spring.auth.JwtGenerator;
import euclid.lyc_spring.auth.JwtProvider;
import euclid.lyc_spring.repository.token.TokenBlackListRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtGenerator jwtGenerator;
    private final TokenBlackListRepository tokenBlackListRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders(HttpHeaders.AUTHORIZATION)
                        .exposedHeaders(HttpHeadersCustom.ACCESSTOKEN)
                        .exposedHeaders(HttpHeadersCustom.REFRESHTOKEN)
                        .exposedHeaders(HttpHeadersCustom.TEMPTOKEN);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .httpBasic(AbstractHttpConfigurer::disable)                   // disable basic auth
                .csrf(AbstractHttpConfigurer::disable)                           // disable csrf
                .formLogin(AbstractHttpConfigurer::disable)                      // disable form login
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // use stateless session
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/lyc/auths/sign-up/send-verification-code", "/lyc/auths/sign-in/**").permitAll()
                        .requestMatchers("/ws/lyc/**").permitAll()
                        .requestMatchers("/lyc/**").authenticated()
                        .anyRequest().denyAll()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, jwtGenerator, tokenBlackListRepository), UsernamePasswordAuthenticationFilter.class)
                .build();

    }

}
