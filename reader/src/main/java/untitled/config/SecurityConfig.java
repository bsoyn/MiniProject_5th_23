package untitled.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // 추가
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 어노테이션 활성화를 위해 추가
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthorizationHeaderFilter authorizationHeaderFilter; // 필터 주입

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                // 회원가입과 내부 통신은 누구나 허용
                .antMatchers(HttpMethod.POST, "/managerReaders").permitAll()
                .antMatchers("/internal/**").permitAll()
                
                // ✨ 핵심: 이제 reader-service가 직접 역할 검사를 수행합니다.
                .antMatchers(HttpMethod.GET, "/managerReaders/**").hasAnyRole("READER", "ADMIN")
                
                // 그 외 모든 요청은 일단 인증이 필요함
                .anyRequest().authenticated()
            .and()
                .addFilterBefore(authorizationHeaderFilter, UsernamePasswordAuthenticationFilter.class);
    }
}