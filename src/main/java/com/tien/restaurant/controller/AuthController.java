package com.tien.restaurant.controller;

import com.tien.multitenancy.config.TenantContext;
import com.tien.restaurant.dto.request.LoginRequest;
import com.tien.restaurant.dto.response.LoginResponse;
import com.tien.restaurant.entity.EmployeeAccount;
import com.tien.restaurant.repository.EmployeeRepository;
import com.tien.restaurant.security.JwtUtil;
import com.tien.restaurant.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final EmployeeRepository employeeRepository;

    // AuthController
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<EmployeeAccount> accountOpt = userService.findAccountFromCentral(request.getEmail());

        if (accountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email không tồn tại");
        }

        // Gắn tenantId vào token, để các request sau mới kiểm tra mật khẩu ở schema tenant
        EmployeeAccount account = accountOpt.get();
        String token = jwtUtil.generateJwtToken(
                request.getEmail(),
                account.getTenantId(),
                account.getAgentId(),
                "Employee"
        );

        Map<String, Object> response = Map.of("token", token);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//        if (userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())) {
//            String tenantId = userService.getTenantIdByEmail(loginRequest.getEmail());
//            String agentId = userService.getAgentIdByEmail(loginRequest.getEmail());
//            String role = userService.getRoleByEmail(loginRequest.getEmail());
//            System.out.println(tenantId);
//            System.out.println(agentId);
//            String token = jwtUtil.generateJwtToken(loginRequest.getEmail(), tenantId, agentId, role);
//
//            return ResponseEntity.ok(new LoginResponse(token));
//        } else {
//            return ResponseEntity.status(401).body("Email hoặc mật khẩu không đúng");
//        }
//    }

}