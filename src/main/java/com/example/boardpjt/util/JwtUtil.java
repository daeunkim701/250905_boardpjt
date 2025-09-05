package com.example.boardpjt.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component // JwtUtil을 Component를 통해 어?
public class JwtUtil {
    private final SecretKey secretKey;
    private final Long accessExpiry;
    private final Long refreshExpiry;

    // hmacShaKeyFor 라는 알고리즘을 사용하는 것
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiry.access}") Long accessExpiry,
                   @Value("${jwt.expiry.refresh}") Long refreshExpiry) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiry = accessExpiry;
        this.refreshExpiry = refreshExpiry;
        System.out.println("secret = " + secret);
        System.out.println("accessExpiry = " + accessExpiry);
        System.out.println("refreshExpiry = " + refreshExpiry);
    }

    public String generateToken(String username, String role, boolean isRefresh) {
        // 일단은 Access만 구현
        return Jwts.builder()
                //...
                .subject(username)
                .claim("role", role)
                // ---
                .issuedAt(new Date())
                // System.currentTimeMillis() -> 현재시간을 걍 밀리세컨드로 나타낸 것
                .expiration(new Date(System.currentTimeMillis() + (isRefresh ? refreshExpiry : accessExpiry)))
                //...
                .signWith(secretKey)
                .compact();
    }

}
