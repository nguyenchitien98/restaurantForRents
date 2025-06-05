package com.tien.restaurant.repository;

import com.tien.restaurant.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepositoryForJdbcTemplate {
    Optional<Employee> findByEmail(String email);
}
