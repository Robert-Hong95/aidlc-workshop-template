package com.tableorder.order.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void 상태전이_PENDING_to_CONFIRMED() {
        Order order = new Order(1L, 1L, 1L, 10000);
        assertThat(order.canTransitionTo(Order.CONFIRMED)).isTrue();
        assertThat(order.canTransitionTo(Order.PREPARING)).isFalse();
        assertThat(order.canTransitionTo(Order.COMPLETED)).isFalse();
    }

    @Test
    void 상태전이_CONFIRMED_to_PREPARING() {
        Order order = new Order(1L, 1L, 1L, 10000);
        order.updateStatus(Order.CONFIRMED);
        assertThat(order.canTransitionTo(Order.PREPARING)).isTrue();
        assertThat(order.canTransitionTo(Order.COMPLETED)).isFalse();
    }

    @Test
    void 상태전이_PREPARING_to_COMPLETED() {
        Order order = new Order(1L, 1L, 1L, 10000);
        order.updateStatus(Order.CONFIRMED);
        order.updateStatus(Order.PREPARING);
        assertThat(order.canTransitionTo(Order.COMPLETED)).isTrue();
    }

    @Test
    void 초기상태_PENDING() {
        Order order = new Order(1L, 1L, 1L, 10000);
        assertThat(order.getStatus()).isEqualTo(Order.PENDING);
    }
}
