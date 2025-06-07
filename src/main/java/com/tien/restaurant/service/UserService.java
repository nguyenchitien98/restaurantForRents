package com.tien.restaurant.service;

import com.tien.multitenancy.config.TenantContext;
import com.tien.restaurant.central.repository.CentralEmployeeAccountRepository;
import com.tien.restaurant.dto.request.CreateEmployeeRequest;
import com.tien.restaurant.entity.Employee;
import com.tien.restaurant.central.entity.EmployeeAccount;
import com.tien.restaurant.mapper.EmployeeMapper;
import com.tien.restaurant.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {

    public UserService( EmployeeRepository employeeRepository, CentralEmployeeAccountRepository centralEmployeeAccountRepository) {
        this.employeeRepository = employeeRepository;
        this.centralEmployeeAccountRepository = centralEmployeeAccountRepository;
    }

    private final EmployeeRepository employeeRepository;

    @Qualifier("centralEmployeeAccountRepository")
    private final CentralEmployeeAccountRepository centralEmployeeAccountRepository;

//    public boolean authenticate(String email, String password) {
//        Optional<EmployeeAccount> optAccount = centralEmployeeAccountRepository.findByEmail(email);
//        if (optAccount.isEmpty()) return false;
//
//        // 1. Set TenantContext
//        String tenantId = "restaurant_"+ optAccount.get().getTenantId();
//        TenantContext.setTenant(tenantId); // giữ để dùng trong các chỗ khác nếu cần
//        System.out.println(TenantContext.getTenant());
//
//        Optional<Employee> optEmp = employeeRepositoryForJdbcTemplate.findByEmail(email);
//        if (optEmp.isEmpty()) return false;
//
//        return optEmp.get().getPassword().equals(password);
//    }

    public boolean authenticate(String email, String password) {
        Optional<EmployeeAccount> optAccount = centralEmployeeAccountRepository.findByEmail(email);
        if (optAccount.isEmpty()) return false;

        // 1. Set TenantContext
        String tenantId = "restaurant_" + optAccount.get().getTenantId();
//        String tenantId = optAccount.get().getTenantId();
        TenantContext.setTenant(tenantId);
        String tenant_Id =TenantContext.getTenant();
        System.out.println("1111: "+tenantId);
        System.out.println("2222: "+tenant_Id);

        // 2. Kiểm tra password trong schema tenant
        return employeeRepository.findByEmail(email)
                .map(emp -> emp.getPassword().equals(password)) // hoặc dùng mã hóa
                .orElse(false);
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

    public String getTenantIdByEmail(String email) {
        return centralEmployeeAccountRepository.findByEmail(email)
                .map(EmployeeAccount::getTenantId)
                .orElse(null);
    }

    public String getAgentIdByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .map(Employee::getAgentId)
                .orElse(null);
    }

    public String getRoleByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .map(Employee::getRole)
                .orElse(null);
    }
}
