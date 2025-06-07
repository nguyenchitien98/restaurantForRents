package com.tien.restaurant.dto.request;

import lombok.Data;

@Data
public class UpdateEmployeeRequest {
    private String name;
    private String phone;
    private String role;
    private String email;
    private String agentId;
}