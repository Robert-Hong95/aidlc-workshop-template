package com.tableorder.auth.controller;

import com.tableorder.auth.dto.*;
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

@Tag(name = "Customer Auth", description = "고객(테이블) 인증 API")
@RestController
@RequestMapping("/api/customer/auth")
public class TableAuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public TableAuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "테이블 로그인", description = "매장코드 + 테이블번호 + 비밀번호로 로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody TableLoginRequest request,
                                                             HttpServletResponse response) {
        AuthTokens tokens = authService.loginTable(request.storeCode(), request.tableNo(), request.password());
        addRefreshTokenCookie(response, tokens.refreshToken());
        return ResponseEntity.ok(ApiResponse.ok(new TokenResponse(tokens.accessToken(), tokens.expiresIn(), tokens.role())));
    }

    @Operation(summary = "QR 로그인", description = "QR 토큰으로 테이블 자동 로그인")
    @PostMapping("/qr")
    public ResponseEntity<ApiResponse<TokenResponse>> loginByQr(@Valid @RequestBody QrLoginRequest request,
                                                                 HttpServletResponse response) {
        AuthTokens tokens = authService.loginByQrToken(request.qrToken());
        addRefreshTokenCookie(response, tokens.refreshToken());
        return ResponseEntity.ok(ApiResponse.ok(new TokenResponse(tokens.accessToken(), tokens.expiresIn(), tokens.role())));
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge((int) (jwtTokenProvider.getRefreshExpiration() / 1000));
        response.addCookie(cookie);
    }
}
