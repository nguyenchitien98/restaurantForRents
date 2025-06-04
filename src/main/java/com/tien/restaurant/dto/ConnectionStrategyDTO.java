package com.tien.restaurant.dto;

import lombok.Data;

@Data
public class ConnectionStrategyDTO {
    private Long id;
    private String connectionType; // LAN, USB, AGENT
    private String ipAddress;
    private Integer port;
    private String agentId;
}