package com.tien.scheduled.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Service
public class RetentionJobService {

    @Autowired
    private JdbcTemplate centralJdbc;

    @Autowired
    private DataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger(RetentionJobService.class);

    // Gửi thông báo nếu sắp đến hạn
    // Tên job	                Thời điểm chạy	        Điều kiện chính	                    Mục tiêu
    // notifyExpiringTenants()	Mỗi ngày 3h sáng	Tenant >= 2 năm, chưa được thông báo	Gửi cảnh báo dữ liệu sắp xóa
    @Scheduled(cron = "0 0 3 * * *") // 3h sáng mỗi ngày
    public void notifyExpiringTenants() {
        String sql = """
        SELECT t.tenant_id, t.schema_name, t.data_retention_years, t.created_at
        FROM tenants t
        WHERE t.plan = 'basic'
          AND t.is_active = TRUE
          AND t.created_at <= DATE_SUB(NOW(), INTERVAL 2 YEAR)
          AND NOT EXISTS (
              SELECT 1 FROM retention_notifications r
              WHERE r.tenant_id = t.tenant_id AND r.resolved = FALSE
          )
    """;

        List<Map<String, Object>> tenants = centralJdbc.queryForList(sql);

        System.out.println("🔎 Số tenant đủ điều kiện gửi cảnh báo: " + tenants.size());

        for (Map<String, Object> tenant : tenants) {
            String tenantId = (String) tenant.get("tenant_id");
            log.info("📩 Đã gửi thông báo tới tenant: {}", tenant);
            String schema = (String) tenant.get("schema_name");
            int retentionYears = (int) tenant.get("data_retention_years");

            // Kiểm tra xem có dữ liệu cũ trong schema không
            String checkDataSql = "SELECT EXISTS (SELECT 1 FROM " + schema + ".orders WHERE created_at < DATE_SUB(NOW(), INTERVAL ? YEAR))";
            Boolean hasOldData = centralJdbc.queryForObject(checkDataSql, Boolean.class, retentionYears);

            if (Boolean.FALSE.equals(hasOldData)) {
                System.out.println("❎ Tenant " + tenantId + " không có dữ liệu cũ, bỏ qua.");
                continue; // Không gửi nếu không có dữ liệu sắp bị xóa
            }

            centralJdbc.update("""
            INSERT INTO retention_notifications (tenant_id, message)
            VALUES (?, ?)
        """, tenantId, "⏳ Dữ liệu cũ của bạn sắp bị xóa. Hãy nâng cấp gói dịch vụ để giữ dữ liệu!");

            System.out.println("📩 Đã gửi thông báo tới tenant: " + tenantId);
        }
    }

    // Tên job	                    Thời điểm chạy	        Điều kiện chính	                        Mục tiêu
    // emindFinalWarningTenants()	Mỗi ngày 8h sáng	  Đã được thông báo từ 23-29 ngày trước	    Nhắc mỗi ngày cho tới lúc xóa
    // Xoá dữ liệu sau 30 ngày nếu chưa nâng cấp
    @Scheduled(cron = "0 0 4 * * *") // 4h sáng mỗi ngày
    public void deleteExpiredData() {
        String sql = """
        SELECT r.tenant_id, t.schema_name, t.data_retention_years
        FROM retention_notifications r
        JOIN tenants t ON t.tenant_id = r.tenant_id
        WHERE r.resolved = FALSE
          AND r.notified_at <= DATE_SUB(NOW(), INTERVAL 30 DAY)
          AND t.plan = 'basic'
          AND t.created_at <= DATE_SUB(NOW(), INTERVAL 2 YEAR)
    """;

        List<Map<String, Object>> rows = centralJdbc.queryForList(sql);
        for (Map<String, Object> row : rows) {
            String tenantId = (String) row.get("tenant_id");
            String schema = (String) row.get("schema_name");
            int retentionYears = (int) row.get("data_retention_years");

            try (Connection conn = dataSource.getConnection()) {
                Statement stmt = conn.createStatement();
                stmt.execute("USE " + schema);
                int deleted = stmt.executeUpdate("DELETE FROM orders WHERE created_at < DATE_SUB(NOW(), INTERVAL " + retentionYears + " YEAR)");
                System.out.println("🗑️ Đã xóa " + deleted + " bản ghi của tenant: " + tenantId);
            } catch (Exception e) {
                System.err.println("❌ Lỗi khi xóa dữ liệu tenant " + tenantId + ": " + e.getMessage());
            }

            // Cập nhật resolved = true
            String updateSql = """
                                UPDATE central_db.retention_notifications SET resolved = TRUE WHERE tenant_id = ?
                            """;
            centralJdbc.update(updateSql, tenantId);
        }
    }

    // Tên job	                Thời điểm chạy	        Điều kiện chính	                    Mục tiêu
    // deleteExpiredData()	Mỗi ngày 4h sáng	Đã thông báo >30 ngày, chưa nâng cấp	Xóa dữ liệu thật sự
    //Nhắc tenant không bị bỏ sót (vì có thể họ quên).
    //Tăng tỷ lệ nâng cấp dịch vụ (chuyển từ basic → premium/forever)
    //Đảm bảo bạn không bị khiếu nại sau khi xóa dữ liệu.
    // Chạy mỗi ngày, tìm các tenant đã được gửi thông báo trước đó từ 23 đến 29 ngày, và gửi nhắc nhở mỗi ngày cho đến lúc bị xóa.
    @Scheduled(cron = "0 0 8 * * *") // 8h sáng mỗi ngày
    public void remindFinalWarningTenants() {
        String sql = """
        SELECT r.tenant_id, t.schema_name, t.name, DATEDIFF(NOW(), r.notified_at) AS days_since_notify
        FROM retention_notifications r
        JOIN tenants t ON t.tenant_id = r.tenant_id
        WHERE r.resolved = FALSE
          AND DATEDIFF(NOW(), r.notified_at) BETWEEN 23 AND 29
    """;

        List<Map<String, Object>> tenants = centralJdbc.queryForList(sql);

        for (Map<String, Object> tenant : tenants) {
            String tenantId = (String) tenant.get("tenant_id");
            String name = (String) tenant.get("name");
            Number days = (Number) tenant.get("days_since_notify");
            int daysSinceNotify = days != null ? days.intValue() : 0;
            int daysLeft = 30 - daysSinceNotify;

            // Gửi nhắc nhở (email/thông báo hệ thống)
            System.out.printf("🔔 Nhắc nhở %s: Còn %d ngày nữa sẽ xóa dữ liệu! (tenant_id = %s)%n", name, daysLeft, tenantId);

            // Nếu bạn có service gửi email/push, gọi ở đây
        }
    }
}
