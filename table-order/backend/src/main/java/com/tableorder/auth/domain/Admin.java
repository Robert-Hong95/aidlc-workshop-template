package com.tableorder.auth.domain;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "admins", uniqueConstraints = @UniqueConstraint(columnNames = {"store_id", "username"}))
public class Admin {

    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "login_attempts", nullable = false)
    private int loginAttempts = 0;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Admin() {}

    public Admin(Long storeId, String username, String password) {
        this.storeId = storeId;
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now());
    }

    public void incrementLoginAttempts() {
        this.loginAttempts++;
        if (this.loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            lock(LOCK_DURATION);
        }
    }

    public void resetLoginAttempts() {
        this.loginAttempts = 0;
        this.lockedUntil = null;
    }

    public void lock(Duration duration) {
        this.lockedUntil = LocalDateTime.now().plus(duration);
    }

    public Long getId() { return id; }
    public Long getStoreId() { return storeId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getLoginAttempts() { return loginAttempts; }
    public LocalDateTime getLockedUntil() { return lockedUntil; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
