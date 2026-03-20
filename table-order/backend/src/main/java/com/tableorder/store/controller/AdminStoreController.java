package com.tableorder.store.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.store.dto.StoreCreateRequest;
import com.tableorder.store.dto.StoreResponse;
import com.tableorder.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Store", description = "매장 관리 API")
@RestController
public class AdminStoreController {

    private final StoreService storeService;

    public AdminStoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @Operation(summary = "매장 생성", description = "매장 + 관리자 동시 생성. Rate Limit: 분당 10회")
    @PostMapping("/api/stores")
    public ResponseEntity<ApiResponse<StoreResponse>> createStore(@Valid @RequestBody StoreCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(storeService.createStore(request)));
    }

    @Operation(summary = "매장 목록 조회")
    @GetMapping("/api/admin/stores")
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getStores() {
        return ResponseEntity.ok(ApiResponse.ok(storeService.getStores()));
    }

    @Operation(summary = "매장 상세 조회")
    @GetMapping("/api/admin/stores/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponse>> getStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(ApiResponse.ok(storeService.getStore(storeId)));
    }
}
