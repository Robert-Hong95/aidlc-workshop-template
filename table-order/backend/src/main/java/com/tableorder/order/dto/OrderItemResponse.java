package com.tableorder.order.dto;

public record OrderItemResponse(Long menuId, String menuName, int quantity, int unitPrice) {}
