package com.tableorder.common.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessExpiration;
    private final long refreshExpiration;
    private final long qrExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration:3600000}") long accessExpiration,
            @Value("${jwt.refresh-expiration:57600000}") long refreshExpiration,
            @Value("${jwt.qr-expiration:600000}") long qrExpiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.qrExpiration = qrExpiration;
    }

    public String createAccessToken(String subject, Map<String, Object> claims) {
        Map<String, Object> allClaims = new HashMap<>(claims);
        allClaims.put("tokenType", "ACCESS");
        return buildToken(subject, allClaims, accessExpiration);
    }

    public String createRefreshToken(String subject, Map<String, Object> claims) {
        Map<String, Object> allClaims = new HashMap<>(claims);
        allClaims.put("tokenType", "REFRESH");
        return buildToken(subject, allClaims, refreshExpiration);
    }

    public String createQrToken(String subject, Map<String, Object> claims) {
        Map<String, Object> allClaims = new HashMap<>(claims);
        allClaims.put("type", "QR");
        return buildToken(subject, allClaims, qrExpiration);
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public long getAccessExpiration() { return accessExpiration; }
    public long getRefreshExpiration() { return refreshExpiration; }
    public long getQrExpiration() { return qrExpiration; }

    private String buildToken(String subject, Map<String, Object> claims, long expiration) {
        Date now = new Date();
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(key)
                .compact();
    }
}
