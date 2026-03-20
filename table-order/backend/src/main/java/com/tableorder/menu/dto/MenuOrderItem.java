package com.tableorder.menu.dto;

import jakarta.validation.constraints.NotNull;
public record MenuOrderItem(@NotNull Long menuId, @NotNull Integer displayOrder) {}
