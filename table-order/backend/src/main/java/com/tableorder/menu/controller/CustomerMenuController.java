package com.tableorder.menu.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.menu.dto.CategoryWithMenusResponse;
import com.tableorder.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Menu (Customer)", description = "고객 메뉴 조회 API")
@RestController
public class CustomerMenuController {

    private final MenuService menuService;

    public CustomerMenuController(MenuService menuService) { this.menuService = menuService; }

    @Operation(summary = "메뉴 전체 조회", description = "카테고리별 메뉴 목록 (인증 불필요)")
    @GetMapping("/api/customer/stores/{storeId}/menus")
    public ResponseEntity<ApiResponse<List<CategoryWithMenusResponse>>> getMenus(@PathVariable Long storeId) {
        return ResponseEntity.ok(ApiResponse.ok(menuService.getMenusForCustomer(storeId)));
    }
}
