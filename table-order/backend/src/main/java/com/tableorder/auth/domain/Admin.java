package com.tableorder.auth.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Admin() {}

    public Admin(Long storeId, String username, String password) {
        this.storeId = storeId;
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getStoreId() { return storeId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
