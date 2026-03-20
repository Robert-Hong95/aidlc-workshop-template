package com.tableorder.menu.controller;

import com.tableorder.common.dto.ApiResponse;
import com.tableorder.menu.dto.CategoryWithMenusResponse;
import com.tableorder.menu.service.MenuService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerMenuController {
    private final MenuService menuService;
    public CustomerMenuController(MenuService menuService) { this.menuService = menuService; }

    @GetMapping("/stores/{storeId}/menus")
    public ApiResponse<List<CategoryWithMenusResponse>> getMenus(@PathVariable Long storeId) {
        return ApiResponse.ok(menuService.getMenusForCustomer(storeId));
    }
}
