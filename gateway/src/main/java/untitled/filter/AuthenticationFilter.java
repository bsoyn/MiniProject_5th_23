package untitled.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import untitled.config.JwtTokenProvider;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j // <-- Lombok 라이브러리가 이 어노테이션을 보고 log 객체를 만들어줍니다.
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 1. 모든 요청에 대해 로그를 찍습니다.
        log.info(">>> [Gateway Filter] 요청 시작: {} {}", request.getMethod(), request.getURI());

        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        // 2. 토큰이 없는 요청은 바로 다음으로 넘깁니다. 최종 결정은 SecurityConfig가 합니다.
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info(">>> [Gateway Filter] 인증 헤더 없음. 다음 필터로 계속합니다.");
            return chain.filter(exchange);
        }

        // 3. 토큰이 있는 경우, 검증을 시작합니다.
        log.info(">>> [Gateway Filter] Authorization 헤더 발견: {}", authorizationHeader);
        String token = authorizationHeader.substring(7);

        try {
            if (!jwtTokenProvider.validateToken(token)) {
                 log.error("!!! [Gateway Filter] 유효하지 않은 JWT 토큰입니다.");
                 return handleUnauthorized(exchange, "유효하지 않은 토큰");
            }
            log.info(">>> [Gateway Filter] 토큰 검증 성공.");

            Authentication authentication = getAuthentication(token);
            log.info(">>> [Gateway Filter] 인증 객체 생성 성공: {}", authentication);

            String userId = jwtTokenProvider.getUserIdFromToken(token);
            String userRoles = authentication.getAuthorities().stream()
                                             .map(Object::toString)
                                             .collect(Collectors.joining(","));
            
            log.info(">>> [Gateway Filter] 다운스트림으로 보낼 헤더 추가: X-User-Id={}, X-User-Roles={}", userId, userRoles);
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Roles", userRoles)
                    .build();
            
            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            
            return chain.filter(mutatedExchange)
                    .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));

        } catch (JwtException | IllegalArgumentException e) {
            log.error("!!! [Gateway Filter] 토큰 처리 중 예외 발생", e);
            return handleUnauthorized(exchange, "토큰 처리 중 예외 발생");
        }
    }

    public Authentication getAuthentication(String token) {
        // ... (이전과 동일)
        Claims claims = jwtTokenProvider.getClaimsFromToken(token);
        List<String> roles = claims.get("type", List.class);
        if (roles == null || roles.isEmpty()) {
            roles = List.of("USER");
        }
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        String userId = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(userId, null, authorities);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String reason) {
        log.error("!!! [Gateway Filter] 401 Unauthorized 처리. 이유: {}", reason);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}