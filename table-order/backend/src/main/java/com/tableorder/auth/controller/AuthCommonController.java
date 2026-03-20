package com.tableorder.auth.controller;

import com.tableorder.auth.dto.AuthTokens;
import com.tableorder.auth.dto.TokenResponse;
import com.tableorder.auth.service.AuthService;
import com.tableorder.common.config.JwtTokenProvider;
import com.tableorder.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth Common", description = "공통 인증 API")
@RestController
@RequestMapping("/api/auth")
public class AuthCommonController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthCommonController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "토큰 갱신", description = "Refresh Token 쿠키로 Access Token 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response) {
        AuthTokens tokens = authService.refreshToken(refreshToken);
        Cookie cookie = new Cookie("refreshToken", tokens.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge((int) (jwtTokenProvider.getRefreshExpiration() / 1000));
        response.addCookie(cookie);
        return ResponseEntity.ok(ApiResponse.ok(new TokenResponse(tokens.accessToken(), tokens.expiresIn(), tokens.role())));
    }
}
