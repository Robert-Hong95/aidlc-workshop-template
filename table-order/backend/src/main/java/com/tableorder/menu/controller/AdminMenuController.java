package com.tableorder.menu.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.menu.dto.*;
import com.tableorder.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Menu (Admin)", description = "메뉴 관리 API")
@RestController
public class AdminMenuController {

    private final MenuService menuService;

    public AdminMenuController(MenuService menuService) { this.menuService = menuService; }

    @Operation(summary = "카테고리 생성")
    @PostMapping("/api/admin/stores/{storeId}/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@PathVariable Long storeId, @Valid @RequestBody CategoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(menuService.createCategory(storeId, request)));
    }

    @Operation(summary = "카테고리 목록 조회")
    @GetMapping("/api/admin/stores/{storeId}/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories(@PathVariable Long storeId) {
        return ResponseEntity.ok(ApiResponse.ok(menuService.getCategories(storeId)));
    }

    @Operation(summary = "카테고리 수정")
    @PutMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(menuService.updateCategory(categoryId, request)));
    }

    @Operation(summary = "카테고리 삭제", description = "하위 메뉴가 있으면 삭제 불가")
    @DeleteMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long categoryId) {
        menuService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "메뉴 생성", description = "가격 0원 이상. JPG/PNG 이미지 5MB 이하")
    @PostMapping("/api/admin/stores/{storeId}/menus")
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(@PathVariable Long storeId, @Valid @RequestBody MenuCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(menuService.createMenu(storeId, request)));
    }

    @Operation(summary = "메뉴 목록 조회 (관리자)")
    @GetMapping("/api/admin/stores/{storeId}/menus")
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getAllMenus(@PathVariable Long storeId) {
        return ResponseEntity.ok(ApiResponse.ok(menuService.getAllMenus(storeId)));
    }

    @Operation(summary = "메뉴 수정")
    @PutMapping("/api/admin/menus/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu(@PathVariable Long menuId, @Valid @RequestBody MenuUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(menuService.updateMenu(menuId, request)));
    }

    @Operation(summary = "메뉴 삭제", description = "Soft Delete (is_available = false)")
    @DeleteMapping("/api/admin/menus/{menuId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "메뉴 순서 일괄 변경")
    @PutMapping("/api/admin/stores/{storeId}/menus/order")
    public ResponseEntity<ApiResponse<Void>> updateMenuOrder(@PathVariable Long storeId, @Valid @RequestBody List<MenuOrderItem> items) {
        menuService.updateMenuOrder(storeId, items);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
