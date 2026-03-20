package com.tableorder.order.domain;

public enum OrderStatus {
    PENDING, PREPARING, COMPLETED;

    public boolean canTransitionTo(OrderStatus next) {
        return this.ordinal() + 1 == next.ordinal();
    }
}
