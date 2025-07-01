package untitled.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import untitled.filter.AuthenticationFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private final AuthenticationFilter authenticationFilter;
    public SecurityConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // return http
        //         .csrf().disable()
        //         .httpBasic().disable()
        //         .formLogin().disable()
        //         .authorizeExchange()
        //             .anyExchange().permitAll()
        //         .and()
        //         .build();
        return http
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                
                .authorizeExchange()
                    // 로그인, 회원 가입 관련 경로는 누구나 허용
                    .pathMatchers("/api/auth/**").permitAll()
                    .pathMatchers(HttpMethod.POST, "/managerReaders").permitAll()
                    
                    // --- 2. 인증(Authentication)이 필요한 경로 설정 ---
                    // 회원 목록 조회는 GET 메소드에 대해 인증을 요구
                    .pathMatchers(HttpMethod.GET, "/managerReaders").permitAll()
                    .pathMatchers(HttpMethod.GET, "/managerReaders/**").hasRole("READER")
                    
                    
                    .anyExchange().permitAll()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
