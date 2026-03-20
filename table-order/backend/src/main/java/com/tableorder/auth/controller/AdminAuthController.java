package com.tableorder.auth.controller;

import com.tableorder.auth.dto.AdminLoginRequest;
import com.tableorder.auth.dto.AdminLoginResponse;
import com.tableorder.auth.dto.AdminRegisterRequest;
import com.tableorder.auth.dto.AdminRegisterResponse;
import com.tableorder.auth.service.AuthService;
import com.tableorder.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AuthService authService;

    public AdminAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<AdminLoginResponse> login(@Valid @RequestBody AdminLoginRequest request) {
        return ApiResponse.ok(authService.loginAdmin(request));
    }

    @PostMapping("/register")
    public ApiResponse<AdminRegisterResponse> register(@Valid @RequestBody AdminRegisterRequest request) {
        return ApiResponse.ok(authService.registerAdmin(request));
    }
}
