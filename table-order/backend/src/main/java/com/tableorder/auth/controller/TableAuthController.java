package com.tableorder.auth.controller;

import com.tableorder.auth.dto.TableLoginRequest;
import com.tableorder.auth.dto.TableLoginResponse;
import com.tableorder.auth.service.AuthService;
import com.tableorder.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/auth")
public class TableAuthController {

    private final AuthService authService;

    public TableAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<TableLoginResponse> login(@Valid @RequestBody TableLoginRequest request) {
        return ApiResponse.ok(authService.loginTable(request));
    }
}
