package untitled.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .csrf().disable()      // ★ 1. CSRF 보호 기능 비활성화
            .formLogin().disable() // ★ 2. 폼 기반 로그인 비활성화
            .httpBasic().disable() // ★ 3. HTTP Basic 인증 비활성화
            .authorizeExchange()   // ★ 4. 경로별 권한 설정 시작
                .anyExchange().permitAll() // ★ 5. 모든 경로를 일단 허용
                .and()
            .build();
    }
}