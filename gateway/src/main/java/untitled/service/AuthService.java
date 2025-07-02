package untitled.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import untitled.client.AdminServiceClient;
import untitled.client.ReaderServiceClient;
import untitled.client.AuthorServiceClient;
import untitled.config.JwtTokenProvider; // JwtTokenProvider 경로
import untitled.dto.LoginRequest;
import untitled.dto.TokenResponse;
import untitled.dto.ValidationRequest;
import untitled.dto.ValidationResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ReaderServiceClient readerServiceClient;
    private final AuthorServiceClient writerServiceClient;
    private final AdminServiceClient adminServiceClient;
    private final JwtTokenProvider jwtTokenProvider;

    public Mono<TokenResponse> login(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String userType = request.getUserType().toUpperCase();

        Mono<ValidationResponse> validationMono;
        switch (userType) {
            case "READER":
                validationMono = Mono.fromCallable(() -> readerServiceClient.validate(new ValidationRequest(email, password)));
                break;
            case "AUTHOR": // 또는 "WRITER"
                validationMono = Mono.fromCallable(() -> writerServiceClient.validate(new ValidationRequest(email, password)));
                break;
            case "ADMIN":
                validationMono = Mono.fromCallable(() -> adminServiceClient.validate(new ValidationRequest(email, password)));
                break;
            default:
                return Mono.error(new IllegalArgumentException("Invalid user type"));
        }

        return validationMono
            .onErrorResume(e -> Mono.empty())
            .flatMap(response -> {
                if (response != null && response.isValid()) {
                    String accessToken = jwtTokenProvider.createAccessToken(response.getUserId(), userType);
                    return Mono.just(new TokenResponse(accessToken));
                } else {
                    return Mono.error(new BadCredentialsException("Invalid credentials"));
                }
            })
            .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid credentials")));
    }
}