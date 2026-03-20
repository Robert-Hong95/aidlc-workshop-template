package com.tableorder.menu.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.menu.dto.*;
import com.tableorder.menu.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminMenuController {
    private final MenuService menuService;
    public AdminMenuController(MenuService menuService) { this.menuService = menuService; }

    @PostMapping("/stores/{storeId}/categories")
    public ApiResponse<CategoryResponse> createCategory(@PathVariable Long storeId, @Valid @RequestBody CategoryCreateRequest req) {
        return ApiResponse.ok(menuService.createCategory(storeId, req));
    }
    @GetMapping("/stores/{storeId}/categories")
    public ApiResponse<List<CategoryResponse>> getCategories(@PathVariable Long storeId) {
        return ApiResponse.ok(menuService.getCategories(storeId));
    }
    @PutMapping("/categories/{categoryId}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryCreateRequest req) {
        return ApiResponse.ok(menuService.updateCategory(categoryId, req));
    }
    @DeleteMapping("/categories/{categoryId}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long categoryId) {
        menuService.deleteCategory(categoryId); return ApiResponse.ok(null);
    }
    @PutMapping("/stores/{storeId}/categories/order")
    public ApiResponse<Void> updateCategoryOrder(@PathVariable Long storeId, @RequestBody DisplayOrderRequest req) {
        menuService.updateCategoryOrder(storeId, req); return ApiResponse.ok(null);
    }
    @PostMapping("/stores/{storeId}/menus")
    public ApiResponse<MenuResponse> createMenu(@PathVariable Long storeId, @Valid @RequestBody MenuCreateRequest req) {
        return ApiResponse.ok(menuService.createMenu(storeId, req));
    }
    @GetMapping("/stores/{storeId}/menus")
    public ApiResponse<List<MenuResponse>> getMenus(@PathVariable Long storeId) {
        return ApiResponse.ok(menuService.getMenus(storeId));
    }
    @PutMapping("/menus/{menuId}")
    public ApiResponse<MenuResponse> updateMenu(@PathVariable Long menuId, @Valid @RequestBody MenuUpdateRequest req) {
        return ApiResponse.ok(menuService.updateMenu(menuId, req));
    }
    @DeleteMapping("/menus/{menuId}")
    public ApiResponse<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId); return ApiResponse.ok(null);
    }
    @PutMapping("/stores/{storeId}/menus/order")
    public ApiResponse<Void> updateMenuOrder(@PathVariable Long storeId, @RequestBody DisplayOrderRequest req) {
        menuService.updateMenuOrder(storeId, req); return ApiResponse.ok(null);
    }
}
