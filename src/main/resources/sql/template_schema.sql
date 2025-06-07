-- Tạo schema mới
CREATE SCHEMA IF NOT EXISTS `${SCHEMA_NAME}`;
USE `${SCHEMA_NAME}`;

-- Loại Món ăn trong hóa đơn
CREATE TABLE product_category (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  name VARCHAR(255) NOT NULL,
                                  icon VARCHAR(255)
);

-- Thực đơn
CREATE TABLE menus (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       image TEXT,
                       category_id BIGINT
                       description TEXT,
                       price DECIMAL(10,2) NOT NULL,
                       is_available BOOLEAN DEFAULT TRUE,
                       FOREIGN KEY (category_id) REFERENCES product_category(id)
);

-- Bàn ăn
CREATE TABLE tables (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        table_number INT NOT NULL,
                        capacity INT,
                        status VARCHAR(20) ENUM('AVAILABLE','RESERVED','OCCUPIED') NOT NULL DEFAULT 'AVAILABLE'
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
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100),
                           role VARCHAR(50),
                           email VARCHAR(100) UNIQUE,
                           phone VARCHAR(20),
                           password VARCHAR(255),
                           agent_id VARCHAR(50),
);

-- Khách hàng
CREATE TABLE customers (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(255),
                           phone VARCHAR(20),
                           email VARCHAR(255),
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Thiết bị
CREATE TABLE peripheral_devices (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    name VARCHAR(100) NOT NULL,
                                    location nvarchar(100) NOT NULL,
                                    type ENUM('PRINTER', 'CASH_DRAWER', 'CUSTOMER_DISPLAY') NOT NULL,
                                    status ENUM('CONNECTED', 'DISCONNECTED', 'ERROR') DEFAULT 'DISCONNECTED',
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- Phương thức kết nối
CREATE TABLE connection_strategies (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       device_id BIGINT NOT NULL,
                                       connection_type ENUM('LAN', 'USB', 'AGENT') NOT NULL,
                                       ip_address VARCHAR(100),
                                       port INT,
                                       agent_id VARCHAR(100),
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                                       CONSTRAINT fk_device FOREIGN KEY (device_id) REFERENCES peripheral_devices(id) ON DELETE CASCADE
);