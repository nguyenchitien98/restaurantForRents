-- Tạo schema mới
CREATE SCHEMA IF NOT EXISTS `${SCHEMA_NAME}`;
USE `${SCHEMA_NAME}`;

-- Thực đơn
CREATE TABLE menus (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       description TEXT,
                       price DECIMAL(10,2) NOT NULL,
                       category VARCHAR(100),
                       is_available BOOLEAN DEFAULT TRUE
);

-- Bàn ăn
CREATE TABLE tables (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        table_number INT NOT NULL,
                        capacity INT,
                        is_active BOOLEAN DEFAULT TRUE
);

-- Hóa đơn
CREATE TABLE orders (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        table_id BIGINT,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        total_amount DECIMAL(10,2),
                        status VARCHAR(50) DEFAULT 'OPEN',
                        FOREIGN KEY (table_id) REFERENCES tables(id)
);

-- Món ăn trong hóa đơn
CREATE TABLE order_items (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             order_id BIGINT NOT NULL,
                             menu_id BIGINT NOT NULL,
                             quantity INT NOT NULL,
                             price DECIMAL(10,2) NOT NULL,
                             FOREIGN KEY (order_id) REFERENCES orders(id),
                             FOREIGN KEY (menu_id) REFERENCES menus(id)
);

-- Nhân viên
CREATE TABLE employees (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(255),
                           role VARCHAR(100),
                           email VARCHAR(255),
                           phone VARCHAR(20)
);

-- Khách hàng
CREATE TABLE customers (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(255),
                           phone VARCHAR(20),
                           email VARCHAR(255),
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);