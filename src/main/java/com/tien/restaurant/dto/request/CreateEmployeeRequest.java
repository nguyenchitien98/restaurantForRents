package com.tien.restaurant.dto.request;

import lombok.Data;

@Data
public class CreateEmployeeRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;
    private String agentId;
}