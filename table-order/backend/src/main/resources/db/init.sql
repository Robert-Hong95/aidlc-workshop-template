CREATE DATABASE IF NOT EXISTS table_order
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE table_order;

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
    login_attempts INT NOT NULL DEFAULT 0,
    locked_until DATETIME NULL,
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
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INT NOT NULL DEFAULT 0,
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
