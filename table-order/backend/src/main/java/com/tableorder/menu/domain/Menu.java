package com.tableorder.menu.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "menus")
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    private String description;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private int displayOrder;

    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected Menu() {}

    public Menu(Long storeId, Long categoryId, String name, int price, String description, String imageUrl, int displayOrder) {
        this.storeId = storeId;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
        this.deleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getStoreId() { return storeId; }
    public Long getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public int getDisplayOrder() { return displayOrder; }
    public boolean isDeleted() { return deleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void update(String name, int price, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() { this.deleted = true; this.updatedAt = LocalDateTime.now(); }
    public void updateDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
}
