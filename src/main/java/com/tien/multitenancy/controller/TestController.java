package com.tien.multitenancy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class TestController {
    @Autowired
    private TenantJdbcExecutor jdbcTemplate;

    @GetMapping
    public List<Map<String, Object>> getAllRestaurants() throws SQLException {
        return jdbcTemplate.executeQuery("SELECT * FROM menus");
    }
}
