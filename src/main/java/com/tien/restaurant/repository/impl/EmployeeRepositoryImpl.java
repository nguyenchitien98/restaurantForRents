package com.tien.restaurant.repository.impl;
import com.tien.multitenancy.config.TenantContext;
import com.tien.multitenancy.controller.TenantJdbcExecutor;
import com.tien.restaurant.entity.Employee;
import com.tien.restaurant.repository.EmployeeRepositoryForJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepositoryForJdbcTemplate {

    private final TenantJdbcExecutor tenantJdbcExecutor;

    public EmployeeRepositoryImpl(TenantJdbcExecutor tenantJdbcExecutor) {
        this.tenantJdbcExecutor = tenantJdbcExecutor;
    }

    public Optional<Employee> findByEmail(String email) {
        try {
            String sql = "SELECT id, email, password, name, phone, role, agent_id FROM `"
                    + TenantContext.getTenant() + "`.employees WHERE email = ?";
            List<Map<String, Object>> result = tenantJdbcExecutor.executeQueryWithParams(sql, email);
            if (result.isEmpty()) return Optional.empty();

            Map<String, Object> row = result.get(0);
            Employee emp = new Employee();
            emp.setId((Long) row.get("id"));
            emp.setEmail((String) row.get("email"));
            emp.setPassword((String) row.get("password"));
            emp.setName((String) row.get("name"));
            emp.setPhone((String) row.get("phone"));
            emp.setRole((String) row.get("role"));
            emp.setAgentId((String) row.get("agent_id"));
            return Optional.of(emp);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
