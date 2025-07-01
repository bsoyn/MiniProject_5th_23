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
                .antMatchers(HttpMethod.POST, "/managerReaders").permitAll()
                .antMatchers("/internal/**").permitAll()
                .antMatchers(HttpMethod.GET, "/managerReaders/**").hasAnyRole("READER", "ADMIN")
                .anyRequest().authenticated()
            .and()
                // ✨ 핵심: 모든 요청에 대해 우리가 만든 헤더 필터를 적용합니다.
                .addFilterBefore(authorizationHeaderFilter, UsernamePasswordAuthenticationFilter.class);
    }
}