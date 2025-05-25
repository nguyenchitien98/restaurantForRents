# 🍽️ Multi-Tenant Data Retention Management System

Hệ thống quản lý nhà hàng dạng **multi-tenant** với cơ chế lưu trữ dữ liệu riêng biệt cho từng tenant (mỗi nhà hàng một schema). Hệ thống hỗ trợ:

- Tự động thông báo khi sắp hết thời hạn lưu trữ
- Xóa dữ liệu cũ theo chính sách
- Hỗ trợ nâng cấp gói để kéo dài thời hạn lưu dữ liệu

---

## 🏗️ Cấu Trúc Chính

- `tenants`: Thông tin nhà hàng (mỗi tenant có 1 schema riêng)
- `retention_notifications`: Lưu lịch sử thông báo sắp xóa dữ liệu
- `retention_policies`: Cấu hình gói lưu trữ (2 năm, 5 năm, vĩnh viễn)
- `orders`: Dữ liệu giao dịch của từng tenant

---

## 🚀 Cài đặt

1. **Cấu hình cơ sở dữ liệu:**

Tạo các bảng hệ thống:
```sql
-- central schema
use central_db;

CREATE TABLE tenants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id VARCHAR(100) UNIQUE, -- mã định danh (restaurant_001)
    name VARCHAR(255) NOT NULL,             -- tên nhà hàng
    email VARCHAR(255) NOT NULL,            -- email chủ nhà hàng
    schema_name VARCHAR(255) NOT NULL,      -- tên schema tương ứng (vd: restaurant_001)
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    plan VARCHAR(50) DEFAULT 'basic',       -- gói dịch vụ hiện tại
    data_retention_years INT DEFAULT 2,     -- số năm lưu dữ liệu
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE retention_policies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan VARCHAR(50) UNIQUE NOT NULL,        -- gói dịch vụ: basic, premium, enterprise
    retention_years INT,            		 -- số năm giữ liệu
    price_per_year DECIMAL(10,2),            -- giá/năm lưu thêm
    description TEXT
);
 -- Data test
INSERT INTO retention_policies (plan, retention_years, price_per_year, description)
VALUES 
('premium', 5, 5000000 ,'Giữ dữ liệu 5 năm'),
('forever', Null,10000000, 'Giữ vĩnh viễn');

CREATE TABLE retention_notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id VARCHAR(100) NOT NULL,
    notified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    message TEXT,
    resolved BOOLEAN DEFAULT FALSE,          -- true nếu khách đã nâng cấp
    FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id)
);

Tạo schema riêng cho mỗi nhà hàng (vd: restaurant_001) và bảng orders trong đó.
-- data test: Ví dụ schema restaurant_008
    use restaurant_008;
-- Đơn hàng cũ hơn 2 năm → sẽ bị xóa
INSERT INTO orders (table_id ,created_at, total_amount, status)
VALUES (1,DATE_SUB(NOW(), INTERVAL 3 YEAR), 50000.0, "pending");

-- Đơn hàng mới trong 1 năm → sẽ không bị xóa
INSERT INTO orders (table_id ,created_at, total_amount, status)
VALUES (1,DATE_SUB(NOW(), INTERVAL 6 MONTH), 200000.0, "pending");


🔄 Cron Jobs (Tự động)
Các job chạy mỗi ngày theo lịch:

Job	                        Lúc chạy	    Chức năng
notifyExpiringTenants()	    3h sáng	        Gửi thông báo tenant >= 2 năm, có dữ liệu sắp xóa
remindFinalWarningTenants()	8h sáng	        Gửi nhắc nhở mỗi ngày từ ngày 23–29 sau khi đã gửi cảnh báo
deleteExpiredData()	        4h sáng	        Xóa dữ liệu cũ nếu đã cảnh báo > 30 ngày và chưa nâng cấp dịch vụ

📡 API Test Thủ Công
1. Gửi thông báo sắp xóa dữ liệu (giả lập job)
GET /admin/test/notify

2. Nhắc nhở mạnh mẽ trước khi xóa
GET /admin/test/remind

3. Xóa dữ liệu thật sự
GET /admin/test/delete


🧪 Cách Test Đầy Đủ
1. Thêm tenant mới:
Tenant tạo trước > 2 năm (giả lập bằng cách chỉnh created_at trong DB).

2. Thêm dữ liệu vào bảng orders của schema riêng, ví dụ:
   -- Đơn hàng cũ hơn 2 năm → sẽ bị xóa
INSERT INTO orders (table_id ,created_at, total_amount, status)
VALUES (1,DATE_SUB(NOW(), INTERVAL 3 YEAR), 50000.0, "pending");

-- Đơn hàng mới trong 1 năm → sẽ không bị xóa
INSERT INTO orders (table_id ,created_at, total_amount, status)
VALUES (1,DATE_SUB(NOW(), INTERVAL 6 MONTH), 200000.0, "pending");
3. Gửi thông báo thử:
curl http://localhost:8080/admin/test/notify

4. Kiểm tra bảng retention_notifications, đảm bảo có thông báo được tạo.
5. Giả lập tenant không nâng cấp, rồi chạy:
curl http://localhost:8080/admin/test/remind

6. Giả lập sau 30 ngày, chạy xóa:
curl http://localhost:8080/admin/test/delete

```
``` Chính sách gói dịch vụ (demo)
📦 Chính sách gói dịch vụ (demo)
Gói	Thời gian lưu	Ghi chú
basic	2 năm	        Mặc định
premium	5 năm	        Trả phí thêm
forever	Không xóa	Trả phí cao nhất

📁 Đường dẫn quan trọng
RetentionJobService.java: Xử lý toàn bộ logic job định kỳ

application.yml: Cấu hình DB và lịch cron (nếu cần)

🛠️ Phụ trợ
Spring Boot 3.x
MySQL (1 central DB + nhiều schema)
Spring Scheduler
JDBC Template

🧑‍💻 Tác giả
Nguyễn Chí Tiến – 2025
Email: ...
```



