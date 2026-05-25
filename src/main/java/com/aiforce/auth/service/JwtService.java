package com.aiforce.auth.service;

import com.aiforce.auth.domain.UserAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey key;
    private final long jwtExpirationMillis;

    public JwtService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.expiration-minutes}") long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMillis = Duration.ofMinutes(expirationMinutes).toMillis();
    }

    public String generateToken(UserAccount userAccount, String sessionId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMillis);
        return Jwts.builder()
                .setClaims(Map.of(
                        "sid", sessionId,
                        "sub", userAccount.getUsername()
                ))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getJwtExpirationMillis() {
        return jwtExpirationMillis;
    }
}
