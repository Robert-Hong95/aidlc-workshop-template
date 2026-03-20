package com.tableorder.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AdminTest {

    private Admin createAdmin() {
        return new Admin(1L, "testuser", "encodedPassword");
    }

    // TC-AUTH-001
    @Test
    @DisplayName("isLocked - 잠금 시간 이전이면 true")
    void isLocked_whenLockedUntilIsFuture_returnsTrue() {
        Admin admin = createAdmin();
        admin.lock(Duration.ofMinutes(15));

        assertThat(admin.isLocked()).isTrue();
    }

    // TC-AUTH-002
    @Test
    @DisplayName("isLocked - 잠금 시간 경과 시 false")
    void isLocked_whenLockedUntilIsPast_returnsFalse() {
        Admin admin = createAdmin();
        admin.lock(Duration.ofMillis(1));

        // 약간의 대기로 만료 보장
        try { Thread.sleep(5); } catch (InterruptedException ignored) {}

        assertThat(admin.isLocked()).isFalse();
    }

    // TC-AUTH-003
    @Test
    @DisplayName("incrementLoginAttempts - 5회 미만이면 횟수만 증가")
    void incrementLoginAttempts_underMax_incrementsOnly() {
        Admin admin = createAdmin();
        admin.incrementLoginAttempts(); // 1
        admin.incrementLoginAttempts(); // 2
        admin.incrementLoginAttempts(); // 3
        admin.incrementLoginAttempts(); // 4

        assertThat(admin.getLoginAttempts()).isEqualTo(4);
        assertThat(admin.getLockedUntil()).isNull();
    }

    // TC-AUTH-004
    @Test
    @DisplayName("incrementLoginAttempts - 5회 도달 시 15분 잠금")
    void incrementLoginAttempts_atMax_locksAccount() {
        Admin admin = createAdmin();
        for (int i = 0; i < 5; i++) {
            admin.incrementLoginAttempts();
        }

        assertThat(admin.getLoginAttempts()).isEqualTo(5);
        assertThat(admin.getLockedUntil()).isNotNull();
        assertThat(admin.getLockedUntil()).isAfter(LocalDateTime.now());
        assertThat(admin.isLocked()).isTrue();
    }

    // TC-AUTH-005
    @Test
    @DisplayName("resetLoginAttempts - 초기화")
    void resetLoginAttempts_resetsAll() {
        Admin admin = createAdmin();
        for (int i = 0; i < 5; i++) {
            admin.incrementLoginAttempts();
        }

        admin.resetLoginAttempts();

        assertThat(admin.getLoginAttempts()).isZero();
        assertThat(admin.getLockedUntil()).isNull();
    }
}
