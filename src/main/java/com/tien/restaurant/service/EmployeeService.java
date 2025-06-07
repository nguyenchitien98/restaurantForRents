package com.tien.restaurant.service;

import com.tien.restaurant.central.entity.EmployeeAccount;
import com.tien.restaurant.central.repository.CentralEmployeeAccountRepository;
import com.tien.restaurant.dto.request.CreateEmployeeRequest;
import com.tien.restaurant.dto.request.UpdateEmployeeRequest;
import com.tien.restaurant.entity.Employee;
import com.tien.restaurant.entity.Role;
import com.tien.restaurant.mapper.EmployeeMapper;
import com.tien.restaurant.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService {
    public EmployeeService(EmployeeRepository employeeRepository, CentralEmployeeAccountRepository centralEmployeeAccountRepository) {
        this.employeeRepository = employeeRepository;
        this.centralEmployeeAccountRepository = centralEmployeeAccountRepository;
    }

    private final EmployeeRepository employeeRepository;
    @Qualifier("centralEmployeeAccountRepository")
    private final CentralEmployeeAccountRepository centralEmployeeAccountRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional
    public void createEmployeeInTenantAndCentral(CreateEmployeeRequest request, String tenantId) {
        // 1. Save vào tenant schema theo TenantContext
        employeeRepository.save(EmployeeMapper.toEntity(request));

        // 2. Save vào central_db qua repository dùng centralDataSource
        EmployeeAccount account = new EmployeeAccount();
        account.setEmail(request.getEmail());
        account.setTenantId(tenantId);
        centralEmployeeAccountRepository.save(account); // sẽ dùng EntityManager riêng
    }

    @Transactional
    public Employee updateEmployeeInTenantAndCentral(Long id, UpdateEmployeeRequest request) {
        return employeeRepository.findById(id).map(employee -> {
            // Lưu email cũ để update account
            String oldEmail = employee.getEmail();

            employee.setName(request.getName());
            employee.setRole(Role.valueOf(request.getRole()));
            if (request.getEmail() != null){
                employee.setEmail(request.getEmail());
            }
            employee.setPhone(request.getPhone());
            employee.setAgentId(request.getAgentId());
            Employee updatedEmployee = employeeRepository.save(employee);

            // Update bảng employee_accounts nếu email thay đổi
            if (!oldEmail.equals(request.getEmail())) {
                centralEmployeeAccountRepository.findByEmail(oldEmail).ifPresent(account -> {
                    account.setEmail(request.getEmail());
                    centralEmployeeAccountRepository.save(account);
                });
            }

            return updatedEmployee;
        }).orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
    }

    @Transactional
    public void deleteEmployeeInTenantAndCentral(Long id) {
        // 1. Lấy employee để có email
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        String email = employee.getEmail();

        // 2. Xóa ở tenant schema
        employeeRepository.deleteById(id);

        // 3. Xóa ở central
        centralEmployeeAccountRepository.deleteByEmail(email);
    }
}
