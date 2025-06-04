package com.tien.restaurant.dto.request;

import lombok.Data;

@Data
public class ConnectionStrategyRequest {
    private String connectionType;
    private String ipAddress;
    private Integer port;
    private String agentId;
}