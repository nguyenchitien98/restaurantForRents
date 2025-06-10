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

-- Kho
CREATE TABLE inventory_items (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL,
                                 quantity DECIMAL(10, 2) NOT NULL DEFAULT 0, -- Số lượng tồn
                                 unit VARCHAR(50) NOT NULL,                 -- Đơn vị: kg, lon, ml...
                                 category VARCHAR(100),                     -- Loại: nguyên liệu, vật tư...
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- bảng trung gian
CREATE TABLE menu_ingredients (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  menu_id BIGINT NOT NULL,
                                  inventory_item_id BIGINT NOT NULL,
                                  quantity DECIMAL(10, 2) NOT NULL, -- số lượng nguyên liệu cần cho 1 món
                                  unit VARCHAR(50),
                                  FOREIGN KEY (menu_id) REFERENCES menus(id),
                                  FOREIGN KEY (inventory_item_id) REFERENCES inventory_items(id)
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
                        employee_id BIGINT NULL,
                        order_type ENUM('DINE_IN', 'TAKE_AWAY', 'DELIVERY') DEFAULT 'DINE_IN',

    -- Thông tin khách hàng (dùng cho DELIVERY)
                        customer_name VARCHAR(255),
                        phone_number VARCHAR(20),
                        delivery_address TEXT,

    -- Ghi chú chung đơn hàng
                        note TEXT,

                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        total_amount DECIMAL(10,2),
                        status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED','CANCELLED') DEFAULT 'PENDING',

                        FOREIGN KEY (table_id) REFERENCES tables(id),
                        FOREIGN KEY (employee_id) REFERENCES employees(id)
);

-- Món ăn trong hóa đơn
CREATE TABLE order_items (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             order_id BIGINT NOT NULL,
                             menu_id BIGINT NOT NULL,
                             quantity INT NOT NULL,
                             price DECIMAL(10,2) NOT NULL,
                             note TEXT,
                             status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'PENDING',
                             FOREIGN KEY (order_id) REFERENCES orders(id),
                             FOREIGN KEY (menu_id) REFERENCES menus(id)
);

-- bếp
CREATE TABLE kitchen_orders (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                order_id BIGINT NOT NULL,
                                order_item_id BIGINT NOT NULL,
                                item_name VARCHAR(255) NOT NULL,
                                quantity INT NOT NULL,
                                status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'PENDING',
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Indexes for fast lookup
                                INDEX idx_order_id (order_id),
                                INDEX idx_order_item_id (order_item_id)
);

-- Nhân viên
CREATE TABLE employees (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100),
                           role VARCHAR(50) ENUM('ADMIN', 'CASHIER', 'KITCHEN', 'WAITER') NOT NULL DEFAULT 'WAITER',
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

-- thông báo
CREATE TABLE notifications (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               message TEXT NOT NULL,
                               type ENUM('STORE', 'KITCHEN', 'COUNTER') NOT NULL,
                               is_read BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Hướng dẫn làm đồ/món ăn, nước uống
CREATE TABLE tutorials (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           title VARCHAR(255) NOT NULL,
                           category VARCHAR(100) NOT NULL,
                           description TEXT,
                           image TEXT,
                           ingredients TEXT, -- Chuỗi: các nguyên liệu cách nhau bằng dấu chấm hoặc xuống dòng
                           steps TEXT,       -- Chuỗi: các bước cách nhau bằng dấu chấm hoặc xuống dòng
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);