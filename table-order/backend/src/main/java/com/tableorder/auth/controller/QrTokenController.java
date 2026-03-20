package com.tableorder.auth.controller;

import com.tableorder.auth.dto.QrTokenRequest;
import com.tableorder.auth.dto.QrTokenResponse;
import com.tableorder.auth.service.AuthService;
import com.tableorder.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Auth", description = "관리자 인증 API")
@RestController
@RequestMapping("/api/admin/tables")
public class QrTokenController {

    private final AuthService authService;

    public QrTokenController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "QR 토큰 생성", description = "테이블용 QR 코드에 포함할 JWT 토큰 생성 (유효기간 10분)")
    @PostMapping("/qr-token")
    public ResponseEntity<ApiResponse<QrTokenResponse>> generateQrToken(@Valid @RequestBody QrTokenRequest request) {
        QrTokenResponse qrToken = authService.generateQrToken(request.storeId(), request.tableNo());
        return ResponseEntity.ok(ApiResponse.ok(qrToken));
    }
}
