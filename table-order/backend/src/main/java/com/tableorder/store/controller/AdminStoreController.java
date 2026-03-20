package com.tableorder.store.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.store.dto.*;
import com.tableorder.store.service.StoreService;
import com.tableorder.store.service.TableService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/stores")
public class AdminStoreController {

    private final StoreService storeService;
    private final TableService tableService;

    public AdminStoreController(StoreService storeService, TableService tableService) {
        this.storeService = storeService;
        this.tableService = tableService;
    }

    @PostMapping
    public ApiResponse<StoreResponse> create(@Valid @RequestBody StoreCreateRequest request) {
        return ApiResponse.ok(storeService.createStore(request));
    }

    @GetMapping
    public ApiResponse<List<StoreResponse>> list() {
        return ApiResponse.ok(storeService.getStores());
    }

    @GetMapping("/{storeId}")
    public ApiResponse<StoreResponse> get(@PathVariable Long storeId) {
        return ApiResponse.ok(storeService.getStore(storeId));
    }

    @PutMapping("/{storeId}")
    public ApiResponse<StoreResponse> update(@PathVariable Long storeId, @Valid @RequestBody StoreUpdateRequest request) {
        return ApiResponse.ok(storeService.updateStore(storeId, request));
    }

    @DeleteMapping("/{storeId}")
    public ApiResponse<Void> delete(@PathVariable Long storeId) {
        storeService.deleteStore(storeId);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{storeId}/tables")
    public ApiResponse<TableResponse> setupTable(@PathVariable Long storeId, @Valid @RequestBody TableSetupRequest request) {
        return ApiResponse.ok(tableService.setupTable(storeId, request));
    }

    @GetMapping("/{storeId}/tables")
    public ApiResponse<List<TableResponse>> listTables(@PathVariable Long storeId) {
        return ApiResponse.ok(tableService.getTables(storeId));
    }
}
