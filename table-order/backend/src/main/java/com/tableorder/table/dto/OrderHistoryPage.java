package com.tableorder.table.dto;

import java.util.List;

public record OrderHistoryPage(List<OrderHistoryResponse> items, boolean hasNext, Long lastId) {}
