package com.tien.multitenancy.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/central_db"); // default schema
        config.setUsername("root");
        config.setPassword("123456");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(30); // CẤU HÌNH HẠN CHẾ
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate tenantAwareJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource); // dùng chung pool
    }

//Hiểu bản chất vấn đề
//MySQL giới hạn số connection đồng thời (thường mặc định là 151).
//Với multi-tenancy theo schema, nếu tạo 1 connection pool riêng cho từng tenant, hệ thống sẽ nổ ngay khi có 200–300 tenant online đồng thời.
//Phải dùng 1 connection pool duy nhất, hoặc tối đa vài pool (nếu group theo schema), và dùng logic chuyển schema trong runtime bằng USE schema_name.
}