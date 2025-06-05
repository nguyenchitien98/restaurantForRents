package com.tien.restaurant.service;

import com.tien.multitenancy.config.TenantContext;
import com.tien.restaurant.dto.request.CreateEmployeeRequest;
import com.tien.restaurant.dto.request.LoginRequest;
import com.tien.restaurant.entity.Employee;
import com.tien.restaurant.entity.EmployeeAccount;
import com.tien.restaurant.mapper.EmployeeMapper;
import com.tien.restaurant.repository.CentralEmployeeAccountRepository;
import com.tien.restaurant.repository.EmployeeRepository;
import com.tien.restaurant.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final EmployeeRepository employeeRepository;
    private final CentralEmployeeAccountRepository centralEmployeeAccountRepository;

//    public boolean authenticate(String email, String password) {
//        Optional<EmployeeAccount> optAccount = centralEmployeeAccountRepository.findByEmail(email);
//        if (optAccount.isEmpty()) return false;
//
//        // 1. Set TenantContext
//        String tenantId = optAccount.get().getTenantId();
//        TenantContext.setTenant(tenantId);
//
//        // 2. Kiểm tra password trong schema tenant
//        return employeeRepository.findByEmail(email)
//                .map(emp -> emp.getPassword().equals(password)) // hoặc dùng mã hóa
//                .orElse(false);
//    }

    public Optional<EmployeeAccount> findAccountFromCentral(String email) {
        return centralEmployeeAccountRepository.findByEmail(email);
    }



    @Transactional
    public void createEmployeeInTenantAndCentral(CreateEmployeeRequest request, String tenantId) {
        // 1. Save vào tenant schema theo TenantContext
        employeeRepository.save(EmployeeMapper.toEntity(request));

        // 2. Save vào central_db qua repository dùng centralDataSource
        EmployeeAccount account = new EmployeeAccount();
        account.setEmail(request.getEmail());
        account.setTenantId(tenantId);
        account.setAgentId(request.getAgentId());
        centralEmployeeAccountRepository.save(account); // sẽ dùng EntityManager riêng
    }

    public String getTenantIdByEmail(String email) {
        return centralEmployeeAccountRepository.findByEmail(email)
                .map(EmployeeAccount::getTenantId)
                .orElse(null);
    }

    public String getAgentIdByEmail(String email) {
        return centralEmployeeAccountRepository.findByEmail(email)
                .map(EmployeeAccount::getAgentId)
                .orElse(null);
    }

    public String getRoleByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .map(Employee::getRole)
                .orElse(null);
    }
}
