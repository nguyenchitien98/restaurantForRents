package com.tien.restaurant.controller;

import com.tien.restaurant.dto.request.LoginRequest;
import com.tien.restaurant.dto.response.LoginResponse;
import com.tien.restaurant.security.JwtUtil;
import com.tien.restaurant.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//        if (userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())) {
//            String tenantId = userService.getTenantIdByEmail(loginRequest.getEmail());
//            String agentId = userService.getAgentIdByEmail(loginRequest.getEmail());
//            String role = userService.getRoleByEmail(loginRequest.getEmail());
//            String token = jwtUtil.generateJwtToken(loginRequest.getEmail(), tenantId, agentId, role);
//
//            return ResponseEntity.ok(new LoginResponse(token));
//        } else {
//            return ResponseEntity.status(401).body("Email hoặc mật khẩu không đúng");
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())) {
            String tenantId = userService.getTenantIdByEmail(loginRequest.getEmail());
            String agentId = userService.getAgentIdByEmail(loginRequest.getEmail());
            String role = userService.getRoleByEmail(loginRequest.getEmail());
            System.out.println(tenantId);
            System.out.println(agentId);
            String token = jwtUtil.generateJwtToken(loginRequest.getEmail(), tenantId, agentId, role);

            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            return ResponseEntity.status(401).body("Email hoặc mật khẩu không đúng");
        }
    }

}