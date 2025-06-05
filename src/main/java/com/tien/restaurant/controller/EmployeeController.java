package com.tien.restaurant.controller;

import com.tien.restaurant.dto.request.CreateEmployeeRequest;
import com.tien.restaurant.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createEmployee(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody CreateEmployeeRequest request) {

        userService.createEmployeeInTenantAndCentral(request, tenantId);
        return ResponseEntity.ok("Employee created successfully");
    }
}
