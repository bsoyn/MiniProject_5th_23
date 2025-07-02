package untitled.filter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component; // ✨ 1. @Component 어노테이션 추가
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import untitled.config.JwtTokenProvider;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component // ✨ 1. Spring이 이 필터를 인식하고 사용하도록 @Component를 추가합니다.
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 헤더가 없거나 Bearer 타입이 아니면 다음 필터로 진행
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        try {
            String token = authorizationHeader.substring(7);
            
            // 토큰 유효성 검증
            if (jwtTokenProvider.validateToken(token)) {
                // 토큰이 유효하면 인증 객체 생성
                Authentication authentication = getAuthentication(token);
                log.info("[AuthenticationFilter] 인증 성공: User={}, Roles={}", authentication.getName(), authentication.getAuthorities());
                
                // ✨ 3. SecurityContext에 인증 정보 등록 (contextWrite 사용)
                return chain.filter(exchange)
                        .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));
            } else {
                log.warn("[AuthenticationFilter] 유효하지 않은 토큰입니다.");
                // 유효하지 않은 토큰은 인증 없이 다음 필터로 진행 (접근 결정은 SecurityConfig에서)
                return chain.filter(exchange);
            }
        } catch (Exception e) {
            log.error("[AuthenticationFilter] 토큰 처리 중 예외 발생", e);
            // 예외 발생 시에도 인증 없이 다음 필터로 진행
            return chain.filter(exchange);
        }
    }

    /**
     * 토큰에서 Claims를 추출하여 Authentication 객체를 생성하는 메소드
     */
    public Authentication getAuthentication(String token) {
        Claims claims = jwtTokenProvider.getClaimsFromToken(token);
        
        // ✨ 2. JWT의 "type" 클레임을 List<String>으로 받도록 수정
        List<String> roles = claims.get("type", List.class);
        if (roles == null || roles.isEmpty()) {
            // 역할 정보가 없으면 빈 권한 목록을 반환
            return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, Collections.emptyList());
        }

        // 역할 목록을 Spring Security의 GrantedAuthority 객체로 변환
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // "ROLE_" 접두사 추가
                .collect(Collectors.toList());
        
        String userId = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(userId, null, authorities);
    }

    @Override
    public int getOrder() {
        // 다른 필터보다 먼저 실행되도록 높은 우선순위를 부여합니다.
        return -200;
    }
}