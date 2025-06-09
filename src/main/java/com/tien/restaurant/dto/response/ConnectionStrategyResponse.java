package com.tien.restaurant.dto.response;

import lombok.Data;

@Data
public class ConnectionStrategyResponse {
    private Long id;
    private String connectionType; // LAN, USB, AGENT
    private String ipAddress;
    private Integer port;
    private String agentId;
}