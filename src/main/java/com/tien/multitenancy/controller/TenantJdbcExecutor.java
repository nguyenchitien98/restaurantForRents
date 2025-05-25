package com.tien.multitenancy.controller;

import com.tien.multitenancy.config.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class TenantJdbcExecutor {
    @Autowired
    private DataSource dataSource;

    public List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        String tenant = TenantContext.getTenant();
        try (Connection connection = dataSource.getConnection()) {
            connection.setCatalog(tenant); // chuyển schema theo tenant
            JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(connection, true));
            return jdbcTemplate.queryForList(sql);
        }
    }
}
