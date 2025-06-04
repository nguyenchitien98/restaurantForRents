package com.tien.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary // Tenant-aware DataSource
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

    // Đây là DataSource riêng cố định để truy vấn metadata ở central_db
    @Bean(name = "centralDataSource") // Dành riêng cho central DB
    public DataSource centralDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/central_db");
        config.setUsername("root");
        config.setPassword("123456");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(5); // thấp vì chỉ dùng metadata
        return new HikariDataSource(config);
    }

//Hiểu bản chất vấn đề
//MySQL giới hạn số connection đồng thời (thường mặc định là 151).
//Với multi-tenancy theo schema, nếu tạo 1 connection pool riêng cho từng tenant, hệ thống sẽ nổ ngay khi có 200–300 tenant online đồng thời.
//Phải dùng 1 connection pool duy nhất, hoặc tối đa vài pool (nếu group theo schema), và dùng logic chuyển schema trong runtime bằng USE schema_name.
}