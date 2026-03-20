package com.tableorder.table.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.table.dto.*;
import com.tableorder.table.service.TableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Table", description = "테이블 관리 API")
@RestController
public class AdminTableController {

    private final TableService tableService;

    public AdminTableController(TableService tableService) {
        this.tableService = tableService;
    }

    @Operation(summary = "테이블 등록")
    @PostMapping("/api/admin/stores/{storeId}/tables")
    public ResponseEntity<ApiResponse<TableResponse>> setupTable(@PathVariable Long storeId, @Valid @RequestBody TableSetupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(tableService.setupTable(storeId, request)));
    }

    @Operation(summary = "테이블 목록 조회")
    @GetMapping("/api/admin/stores/{storeId}/tables")
    public ResponseEntity<ApiResponse<List<TableResponse>>> getTables(@PathVariable Long storeId) {
        return ResponseEntity.ok(ApiResponse.ok(tableService.getTables(storeId)));
    }

    @Operation(summary = "테이블 세션 종료", description = "주문 내역을 order_history에 아카이빙 후 세션 종료")
    @PostMapping("/api/admin/tables/{tableId}/end-session")
    public ResponseEntity<ApiResponse<Void>> endSession(@PathVariable Long tableId) {
        tableService.endSession(tableId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "주문 이력 조회", description = "Cursor 기반 페이지네이션. 1년 보관")
    @GetMapping("/api/admin/tables/{tableId}/history")
    public ResponseEntity<ApiResponse<OrderHistoryPage>> getOrderHistory(
            @PathVariable Long tableId,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(tableService.getOrderHistory(tableId, dateFrom, dateTo, lastId, size)));
    }
}
