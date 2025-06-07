//package com.tien.restaurant.service;
//
//import com.tien.restaurant.central.entity.EmployeeAccount;
//import com.tien.restaurant.central.repository.CentralEmployeeAccountRepository;
//import com.tien.restaurant.dto.request.UpdateEmployeeRequest;
//import com.tien.restaurant.entity.Employee;
//import com.tien.restaurant.entity.Role;
//import com.tien.restaurant.repository.EmployeeRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class EmployeeService {
//    public EmployeeService(EmployeeRepository employeeRepository, CentralEmployeeAccountRepository centralEmployeeAccountRepository) {
//        this.employeeRepository = employeeRepository;
//        this.centralEmployeeAccountRepository = centralEmployeeAccountRepository;
//    }
//
//    private final EmployeeRepository employeeRepository;
//    @Qualifier("centralEmployeeAccountRepository")
//    private final CentralEmployeeAccountRepository centralEmployeeAccountRepository;
//
//    public List<Employee> getEmployees(String name, String role) {
//        if (role != null && !role.isEmpty()) {
//            return employeeRepository.findByNameContainingIgnoreCaseAndRole(name, Role.valueOf(role));
//        }
//        return employeeRepository.findByNameContainingIgnoreCase(name);
//    }
//
//    @Transactional
//    public void updateEmployeeInTenantAndCentral(Long id, UpdateEmployeeRequest request, String tenantId) {
//        // 1. Lấy employee từ tenant schema
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//
//        // 2. Lưu lại email cũ để kiểm tra thay đổi
//        String oldEmail = employee.getEmail();
//
//        // 3. Cập nhật dữ liệu trong tenant schema
//        employee.setName(request.getName());
//        employee.setPhone(request.getPhone());
//        employee.setRole(Role.valueOf(request.getRole()));
//        employee.setEmail(request.getEmail()); // cập nhật luôn để đồng bộ
//        employeeRepository.save(employee);
//
//        // 4. Nếu email thay đổi thì mới update bên central
//        if (!oldEmail.equalsIgnoreCase(request.getEmail())) {
//            // Xóa bản ghi cũ (email cũ) và thêm bản ghi mới
//            centralEmployeeAccountRepository.deleteByEmail(oldEmail);
//
//            EmployeeAccount newAccount = new EmployeeAccount();
//            newAccount.setEmail(request.getEmail());
//            newAccount.setTenantId(tenantId);
//            centralEmployeeAccountRepository.save(newAccount);
//        }
//    }
//
//    @Transactional
//    public void deleteEmployeeInTenantAndCentral(Long id, String tenantId) {
//        // 1. Lấy employee để có email
//        Employee employee = employeeRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));
//        String email = employee.getEmail();
//
//        // 2. Xóa ở tenant schema
//        employeeRepository.deleteById(id);
//
//        // 3. Xóa ở central
//        centralEmployeeAccountRepository.deleteByEmail(email);
//    }
//}
