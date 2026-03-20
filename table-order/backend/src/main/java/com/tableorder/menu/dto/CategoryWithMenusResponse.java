package com.tableorder.menu.dto;

import java.util.List;

public record CategoryWithMenusResponse(Long categoryId, String categoryName, int displayOrder, List<MenuResponse> menus) {}
