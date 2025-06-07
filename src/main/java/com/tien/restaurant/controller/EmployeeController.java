package com.tien.restaurant.controller;

import com.tien.restaurant.dto.request.CreateEmployeeRequest;
import com.tien.restaurant.dto.request.UpdateEmployeeRequest;
import com.tien.restaurant.entity.Employee;
import com.tien.restaurant.service.EmployeeService;
import com.tien.restaurant.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody CreateEmployeeRequest request) {

        employeeService.createEmployeeInTenantAndCentral(request, tenantId);
        return ResponseEntity.ok("Employee created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        try {
            Employee updated = employeeService.updateEmployeeInTenantAndCentral(id, updateEmployeeRequest);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeeInTenantAndCentral(id);
        return ResponseEntity.noContent().build();
    }
}
