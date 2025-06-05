package com.tien.restaurant.mapper;

import com.tien.restaurant.dto.request.CreateEmployeeRequest;
import com.tien.restaurant.entity.Employee;

public class EmployeeMapper {
    public static Employee toEntity(CreateEmployeeRequest request) {
        Employee employee = new Employee();
        employee.setEmail(request.getEmail());
        employee.setPassword(request.getPassword());
        employee.setName(request.getName());
        employee.setRole(request.getRole());
        employee.setPhone(request.getPhone());
        return employee;
    }
}
