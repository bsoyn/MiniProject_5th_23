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

@Slf4j
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

        List<String> whiteList = List.of("/api/auth/login", "/api/auth/register","/managerReaders");
        String path = request.getURI().getPath();
        if (whiteList.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.error("인증 헤더가 없거나 Bearer 타입이 아닙니다.");
            return handleUnauthorized(exchange);
        }

        String token = authorizationHeader.substring(7);

        try {
            if (!jwtTokenProvider.validateToken(token)) {
                log.error("유효하지 않은 JWT 토큰입니다.");
                return handleUnauthorized(exchange);
            }

            Authentication authentication = getAuthentication(token);

            String userId = jwtTokenProvider.getUserIdFromToken(token);
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .build();
            
            // ✨ 변경된 부분: .contextWrite -> .subscriberContext 로 수정
            // 이전 버전의 Project Reactor와의 호환성을 위해 subscriberContext를 사용합니다.
            return chain.filter(exchange.mutate().request(mutatedRequest).build())
                    .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));

        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT 토큰 처리 중 에러가 발생했습니다: {}", e.getMessage());
            return handleUnauthorized(exchange);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtTokenProvider.getClaimsFromToken(token);

        List<String> roles = claims.get("type", List.class);
        if (roles == null || roles.isEmpty()) {
            roles = List.of("ROLE_USER");
        }
        
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String userId = claims.getSubject();
        
        return new UsernamePasswordAuthenticationToken(userId, null, authorities);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
