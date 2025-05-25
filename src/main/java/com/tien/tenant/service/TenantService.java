package com.tien.tenant.service;

import com.tien.tenant.entity.Tenant;
import com.tien.tenant.model.request.TenantRequest;
import com.tien.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final JdbcTemplate jdbcTemplate;

    private final TenantRepository tenantRepository;

    private final String templateSQLPath = "sql/template_schema.sql";

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
