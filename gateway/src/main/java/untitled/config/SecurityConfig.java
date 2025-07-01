package untitled.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                
                .authorizeExchange()
                    // --- 1. 공개(Public) 경로 설정 ---
                    // 로그인 관련 경로는 누구나 허용
                    .pathMatchers("/api/auth/**").permitAll()
                    // 회원가입 경로는 POST 메소드만 허용
                    .pathMatchers(HttpMethod.POST, "/managerReaders").permitAll()
                    
                    // --- 2. 인증(Authentication)이 필요한 경로 설정 ---
                    // 회원 목록 조회는 GET 메소드에 대해 인증을 요구
                    .pathMatchers(HttpMethod.GET, "/managerReaders").permitAll()
                    
                    // ✨ 그 외 모든 요청도 일단 인증을 요구하도록 설정 (기본 보안 규칙)
                    .anyExchange().authenticated()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
