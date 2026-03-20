package com.tableorder.menu.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_available", nullable = false)
    private boolean available = true;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
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
        this.available = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String name, Integer price, String description, Long categoryId, String imageUrl) {
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (description != null) this.description = description;
        if (categoryId != null) this.categoryId = categoryId;
        if (imageUrl != null) this.imageUrl = imageUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() { this.available = false; this.updatedAt = LocalDateTime.now(); }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public Long getId() { return id; }
    public Long getStoreId() { return storeId; }
    public Long getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public boolean isAvailable() { return available; }
    public int getDisplayOrder() { return displayOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
