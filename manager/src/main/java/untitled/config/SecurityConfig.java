package untitled.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/manageAuthors/**").permitAll()  // Manager API 허용
            .antMatchers("/manageReaderInfos/**").permitAll() // Reader 관리 API 허용
            .antMatchers("/actuator/**").permitAll() // Health check 허용
            .antMatchers("/h2-console/**").permitAll() // H2 Console 허용 (개발용)
            .antMatchers("/error").permitAll() // 에러 페이지 허용
            .anyRequest().authenticated() // 나머지는 인증 필요
            .and()
            .httpBasic().disable()
            .formLogin().disable()
            .headers().frameOptions().disable(); // H2 Console을 위한 설정
    }
} 