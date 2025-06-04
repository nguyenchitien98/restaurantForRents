package com.tien.tenant.service;

import com.tien.tenant.entity.Tenant;
import com.tien.tenant.model.request.TenantRequest;
import com.tien.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

@Service
public class TenantService {

    private final JdbcTemplate jdbcTemplate;
    private final TenantRepository tenantRepository;

    @Qualifier("centralDataSource")
    private final DataSource centralDataSource;

    private final String templateSQLPath = "sql/template_schema.sql";

    public TenantService(JdbcTemplate jdbcTemplate,
                         TenantRepository tenantRepository,
                         @Qualifier("centralDataSource") DataSource centralDataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.tenantRepository = tenantRepository;
        this.centralDataSource = centralDataSource;
    }



    public String getPlanByTenantId(String tenantId) {
        try (Connection conn = centralDataSource.getConnection()) {
            System.out.println("Current schema/catalog = " + conn.getCatalog()); // Hoặc conn.getSchema() với driver mới
            String rawTenantId;
            if (tenantId != null && tenantId.startsWith("restaurant_")) {
                rawTenantId = tenantId.substring("restaurant_".length());
            } else {
                rawTenantId = tenantId; // fallback nếu không đúng định dạng
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT plan FROM tenants WHERE tenant_id = ?"
            );
            stmt.setString(1, rawTenantId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("plan");
            } else {
                throw new RuntimeException("Không tìm thấy tenant: " + tenantId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi truy vấn gói thuê bao", e);
        }
    }

    public void createTenant(TenantRequest request) throws IOException {
        // 1. Sinh schema name
        String schemaName = generateSchemaName(request);

        // Load file SQL template
        ClassPathResource resource = new ClassPathResource(templateSQLPath);
        String templateSQL;
        try (InputStream inputStream = resource.getInputStream()) {
            templateSQL = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        String schemaSQL = templateSQL.replace("${SCHEMA_NAME}", schemaName);

        // Thực thi câu lệnh tạo schema
        Arrays.stream(schemaSQL.split(";"))
                .map(String::trim)
                .filter(sql -> !sql.isEmpty())
                .forEach(sql -> jdbcTemplate.execute(sql));
    }

    private String generateSchemaName(TenantRequest request) {
        Tenant tenant = new Tenant();
        tenant.setName(request.getName());
        tenant.setEmail(request.getEmail());
        tenant.setSchemaName("restaurant"); // có thể sửa lại sau
        tenant.setPlan(request.getPlan() != null ? request.getPlan() : "basic");

        // Lưu lần đầu để sinh ID
        tenant = tenantRepository.save(tenant);

        // Format tenantId
        String tenantId = String.format("%03d", tenant.getId());
        String schemaName = String.format("restaurant_" + tenantId);
//
        tenantRepository.updateTenantIdAndSchema(tenantId, schemaName, tenant.getId());

        return schemaName;
    }
}
