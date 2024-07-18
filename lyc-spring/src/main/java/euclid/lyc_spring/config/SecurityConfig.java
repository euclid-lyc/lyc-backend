package euclid.lyc_spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // jwt session stateless 설정
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // csrf disable 설정
        // session 방식에서는 session이 고정되기 때문에 csrf 공격에 대비를 해줘야하는데 jwt 방식에서는 그럴 필요가 없음
        http.csrf((auth)->auth.disable());

        // jwt 방식으로 로그인을 할 것이기에 form방식과 http basic 인증 방식의 로그인은 disable
        http.formLogin((auth)->auth.disable());
        http.httpBasic((auth)->auth.disable());

        // 필터 등록
        //http.addFilterAt(new LoginFilter(configuration.getAuthenticationManager(), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // JWTFilter 추가
        //http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        return http.build();
    }
}
