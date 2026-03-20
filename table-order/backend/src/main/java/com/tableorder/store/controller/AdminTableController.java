package com.tableorder.store.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.store.dto.OrderHistoryResponse;
import com.tableorder.store.service.TableService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/tables")
public class AdminTableController {

    private final TableService tableService;

    public AdminTableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/{tableId}/end-session")
    public ApiResponse<Void> endSession(@PathVariable Long tableId) {
        tableService.endSession(tableId);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{tableId}/order-history")
    public ApiResponse<List<OrderHistoryResponse>> orderHistory(
            @PathVariable Long tableId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        return ApiResponse.ok(tableService.getOrderHistory(tableId, dateFrom, dateTo));
    }
}
