package untitled.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // .httpBasic().disable() 
            .csrf().disable()      // ★ 1. CSRF 보호 기능을 비활성화
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                    // ★ 2. 회원가입 경로(POST /managerReaders)
                    .antMatchers(HttpMethod.POST, "/managerReaders").permitAll()
                    // ★ 3. Gateway와의 내부 통신 경로
                    .antMatchers("/internal/**").permitAll()
                    // 그 외 모든 요청은 인증이 필요하도록 설정
                    .anyRequest().authenticated();
    }
}