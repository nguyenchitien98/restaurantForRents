package com.tien.restaurant.dto.request;

import com.tien.restaurant.entity.ConnectionType;
import lombok.Data;

@Data
public class ConnectionCheckRequest {
    private ConnectionType connectionType;
    private String ipAddress;
    private Integer port;
    private String agentId;
}
