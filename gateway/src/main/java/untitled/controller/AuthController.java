package untitled.controller;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.JwtException;
import reactor.core.publisher.Mono;
import untitled.dto.LoginRequest;
import untitled.dto.TokenResponse;
import untitled.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/login")
    public Mono<ResponseEntity<TokenResponse>> login(@RequestBody LoginRequest request) {
        return authService.login(request)
                .map(ResponseEntity::ok)
                .onErrorResume(BadCredentialsException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
                .onErrorResume(IllegalArgumentException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    @PostMapping("/api/token")
    public ResponseEntity<Map<String, String>> parseToken(@RequestHeader("Authorization") String authorizationHeader) {
        
        // 헤더가 없거나 Bearer 타입이 아니면 다음 필터로 진행
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Missing or invalid Authorization header"));
        }
        try {
            String token = authorizationHeader.substring(7); // "Bearer " 제거
            String userId = authService.takeUserId(token);
            String userName = authService.takeUserName(token);

            return ResponseEntity.ok(Map.of(
                "userId", userId,
                "userName", userName
            ));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }
    }
}