CREATE DATABASE IF NOT EXISTS table_order
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE table_order;

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

CREATE TABLE stores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW()
);

CREATE TABLE admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    UNIQUE KEY uk_admin_store_username (store_id, username),
    FOREIGN KEY (store_id) REFERENCES stores(id)
);

CREATE TABLE store_tables (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    table_no INT NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    UNIQUE KEY uk_table_store_no (store_id, table_no),
    FOREIGN KEY (store_id) REFERENCES stores(id)
);

CREATE TABLE table_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_id BIGINT NOT NULL,
    started_at DATETIME NOT NULL DEFAULT NOW(),
    ended_at DATETIME NULL,
    FOREIGN KEY (table_id) REFERENCES store_tables(id)
);

CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    FOREIGN KEY (store_id) REFERENCES stores(id)
);

CREATE TABLE menus (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    description TEXT NULL,
    image_url VARCHAR(500) NULL,
    display_order INT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    FOREIGN KEY (store_id) REFERENCES stores(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    table_id BIGINT NOT NULL,
    session_id BIGINT NOT NULL,
    total_amount INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    FOREIGN KEY (store_id) REFERENCES stores(id),
    FOREIGN KEY (table_id) REFERENCES store_tables(id),
    FOREIGN KEY (session_id) REFERENCES table_sessions(id)
);

CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    menu_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    unit_price INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES menus(id)
);

CREATE TABLE order_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_id BIGINT NOT NULL,
    table_id BIGINT NOT NULL,
    session_id BIGINT NOT NULL,
    order_data JSON NOT NULL,
    total_amount INT NOT NULL,
    ordered_at DATETIME NOT NULL,
    completed_at DATETIME NOT NULL DEFAULT NOW()
);

-- Seed Data
-- Password for all seed accounts: "pass1234"
-- BCrypt hash generated with BCryptPasswordEncoder (cost=10)
INSERT INTO stores (store_code, name) VALUES ('STORE01', '테스트매장');
INSERT INTO admins (store_id, username, password) VALUES (1, 'admin', '$2a$10$8w0PR4oWNhD8zCTVn0yRs.DcuG9YT3cAHy4WqMAdFBftqoHz/FSYq');
INSERT INTO store_tables (store_id, table_no, password) VALUES (1, 1, '$2a$10$8w0PR4oWNhD8zCTVn0yRs.DcuG9YT3cAHy4WqMAdFBftqoHz/FSYq');
INSERT INTO store_tables (store_id, table_no, password) VALUES (1, 2, '$2a$10$8w0PR4oWNhD8zCTVn0yRs.DcuG9YT3cAHy4WqMAdFBftqoHz/FSYq');

-- Categories
INSERT INTO categories (store_id, name, display_order) VALUES (1, '커피', 0);
INSERT INTO categories (store_id, name, display_order) VALUES (1, '음료', 1);
INSERT INTO categories (store_id, name, display_order) VALUES (1, '디저트', 2);
INSERT INTO categories (store_id, name, display_order) VALUES (1, '식사', 3);

-- Menus
INSERT INTO menus (store_id, category_id, name, price, description, display_order, deleted) VALUES (1, 1, '아메리카노', 4500, '깊고 진한 에스프레소', 0, false);
INSERT INTO menus (store_id, category_id, name, price, description, display_order, deleted) VALUES (1, 1, '카페라떼', 5000, '부드러운 우유와 에스프레소', 1, false);
INSERT INTO menus (store_id, category_id, name, price, description, display_order, deleted) VALUES (1, 1, '카푸치노', 5000, '풍성한 우유 거품', 2, false);
INSERT INTO menus (store_id, category_id, name, price, description, display_order, deleted) VALUES (1, 2, '녹차라떼', 5500, '고소한 녹차와 우유', 0, false);
INSERT INTO menus (store_id, category_id, name, price, description, display_order, deleted) VALUES (1, 2, '자몽에이드', 6000, '상큼한 자몽 에이드', 1, false);
INSERT INTO menus (store_id, category_id, name, price, description, display_order, deleted) VALUES (1, 2, '레몬에이드', 5500, '시원한 레몬 에이드', 2, false);
INSERT INTO menus (store_id, category_id, name, price, description, display_order, deleted) VALUES (1, 3, '치즈케이크', 7000, '뉴욕 스타일 치즈케이크', 0, false);
INSERT INTO menus (store_id, category_id, name, price, description, display_order, deleted) VALUES (1, 3, '티라미수', 7500, '이탈리안 티라미수', 1, false);
INSERT INTO menus (store_id, category_id, name, price, description, display_order, deleted) VALUES (1, 4, '클럽샌드위치', 9000, '치킨, 베이컨, 야채', 0, false);
INSERT INTO menus (store_id, category_id, name, price, description, display_order, deleted) VALUES (1, 4, '파스타', 12000, '크림 파스타', 1, false);
