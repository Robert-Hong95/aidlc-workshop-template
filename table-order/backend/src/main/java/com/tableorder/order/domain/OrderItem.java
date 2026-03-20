package com.tableorder.order.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "menu_name", nullable = false, length = 100)
    private String menuName;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private int unitPrice;

    protected OrderItem() {}

    public OrderItem(Long orderId, Long menuId, String menuName, int quantity, int unitPrice) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public Long getMenuId() { return menuId; }
    public String getMenuName() { return menuName; }
    public int getQuantity() { return quantity; }
    public int getUnitPrice() { return unitPrice; }
}
