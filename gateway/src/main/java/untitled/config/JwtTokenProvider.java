package untitled.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final long accessTokenValidityInMilliseconds;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-token-validity-in-seconds}") long validity) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenValidityInMilliseconds = validity * 1000;
    }

    public String createAccessToken(String userId, String userType) {
        return createAccessToken(userId, List.of(userType));
    }

    public String createAccessToken(String userId, List<String> userTypes) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);
        return Jwts.builder()
                .setSubject(userId)
                .claim("type", userTypes)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public String getUserIdFromToken(String token) {
        // 토큰 생성 시 subject에 사용자 ID를 저장했다고 가정합니다.
        // 다른 claim에 저장했다면 .get("claimName", String.class) 등으로 변경해야 합니다.
        return Jwts.parserBuilder()
                .setSigningKey(key) // 토큰 생성 시 사용한 SecretKey 객체
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 기존 validateToken 메소드가 boolean을 반환하도록 수정하거나 확인해주세요.
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}