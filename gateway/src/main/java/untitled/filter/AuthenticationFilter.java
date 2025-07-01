package untitled.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import untitled.config.JwtTokenProvider;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 헤더가 없거나 Bearer 타입이 아니면, 아무것도 하지 않고 그냥 통과시킵니다.
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        try {
            String token = authorizationHeader.substring(7);
            // 토큰이 유효하지 않으면, 아무것도 하지 않고 그냥 통과시킵니다.
            if (!jwtTokenProvider.validateToken(token)) {
                return chain.filter(exchange);
            }

            // ✨ 토큰이 유효한 경우에만 이름표(헤더)를 붙여줍니다.
            Authentication authentication = getAuthentication(token);
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            String userRoles = authentication.getAuthorities().stream()
                                             .map(Object::toString)
                                             .collect(Collectors.joining(","));

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Roles", userRoles)
                    .build();
            
            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            // 토큰 처리 중 어떤 에러가 발생해도, 요청을 막지 않고 그냥 통과시킵니다.
            return chain.filter(exchange);
        }
    }

    public Authentication getAuthentication(String token) {
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
}