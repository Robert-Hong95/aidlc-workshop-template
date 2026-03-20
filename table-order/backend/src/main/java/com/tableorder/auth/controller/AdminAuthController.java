package com.tableorder.auth.controller;

import com.tableorder.auth.dto.AuthTokens;
import com.tableorder.auth.dto.LoginRequest;
import com.tableorder.auth.dto.TokenResponse;
import com.tableorder.auth.service.AuthService;
import com.tableorder.common.config.JwtTokenProvider;
import com.tableorder.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Auth", description = "관리자 인증 API")
@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminAuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "관리자 로그인", description = "매장코드 + 아이디 + 비밀번호로 로그인. 5회 실패 시 15분 잠금")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request,
                                                             HttpServletResponse response) {
        AuthTokens tokens = authService.loginAdmin(request.storeCode(), request.username(), request.password());
        addRefreshTokenCookie(response, tokens.refreshToken());
        return ResponseEntity.ok(ApiResponse.ok(new TokenResponse(tokens.accessToken(), tokens.expiresIn(), tokens.role())));
    }

    @Operation(summary = "관리자 로그아웃", description = "Refresh Token 쿠키 삭제")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        clearRefreshTokenCookie(response);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge((int) (jwtTokenProvider.getRefreshExpiration() / 1000));
        response.addCookie(cookie);
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
