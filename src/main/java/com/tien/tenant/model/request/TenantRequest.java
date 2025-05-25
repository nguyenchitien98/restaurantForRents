package com.tien.tenant.model.request;

import lombok.Data;

@Data
public class TenantRequest {
    private String name;
    private String email;
    private String plan;
}