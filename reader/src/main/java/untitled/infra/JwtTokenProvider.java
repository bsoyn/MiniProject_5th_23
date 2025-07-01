package untitled.infra; // 자신의 패키지 경로에 맞게 수정

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

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
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);
        return Jwts.builder()
                .setSubject(userId)
                .claim("type", userType) // "type":"READER" 정보 추가
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}