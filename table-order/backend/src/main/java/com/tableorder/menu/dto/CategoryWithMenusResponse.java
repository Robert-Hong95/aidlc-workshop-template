package com.tableorder.menu.dto;

import java.util.List;
public record CategoryWithMenusResponse(Long id, String name, List<MenuResponse> menus) {}
