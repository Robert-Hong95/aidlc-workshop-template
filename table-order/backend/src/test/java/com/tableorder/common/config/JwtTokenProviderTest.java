package com.tableorder.common.config;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(
                "test-secret-key-for-jwt-token-generation-min-256-bits-long",
                3600000, 57600000, 600000);
    }

    // TC-AUTH-030
    @Test
    @DisplayName("createAccessToken - tokenType=ACCESS 클레임 포함")
    void createAccessToken_containsAccessType() {
        String token = jwtTokenProvider.createAccessToken("1", Map.of("role", "ADMIN"));

        Claims claims = jwtTokenProvider.parseToken(token);
        assertThat(claims.get("tokenType", String.class)).isEqualTo("ACCESS");
        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
    }

    // TC-AUTH-031
    @Test
    @DisplayName("createRefreshToken - tokenType=REFRESH 클레임 포함")
    void createRefreshToken_containsRefreshType() {
        String token = jwtTokenProvider.createRefreshToken("1", Map.of("role", "ADMIN"));

        Claims claims = jwtTokenProvider.parseToken(token);
        assertThat(claims.get("tokenType", String.class)).isEqualTo("REFRESH");
    }

    // TC-AUTH-032
    @Test
    @DisplayName("createQrToken - type=QR 클레임 포함")
    void createQrToken_containsQrType() {
        String token = jwtTokenProvider.createQrToken("qr", Map.of("storeCode", "S001", "tableNo", 1));

        Claims claims = jwtTokenProvider.parseToken(token);
        assertThat(claims.get("type", String.class)).isEqualTo("QR");
        assertThat(claims.get("storeCode", String.class)).isEqualTo("S001");
    }

    @Test
    @DisplayName("validateToken - 유효한 토큰 true")
    void validateToken_valid() {
        String token = jwtTokenProvider.createAccessToken("1", Map.of("role", "ADMIN"));
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("validateToken - 무효한 토큰 false")
    void validateToken_invalid() {
        assertThat(jwtTokenProvider.validateToken("invalid-token")).isFalse();
    }
}
