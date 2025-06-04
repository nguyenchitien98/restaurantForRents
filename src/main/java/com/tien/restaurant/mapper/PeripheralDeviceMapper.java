package com.tien.restaurant.mapper;

import com.tien.restaurant.dto.ConnectionStrategyDTO;
import com.tien.restaurant.dto.PeripheralDeviceDTO;
import com.tien.restaurant.dto.request.ConnectionStrategyRequest;
import com.tien.restaurant.dto.request.CreatePeripheralDeviceRequest;
import com.tien.restaurant.dto.request.UpdatePeripheralDeviceRequest;
import com.tien.restaurant.entity.*;

public class PeripheralDeviceMapper {

    public static PeripheralDeviceDTO toDTO(PeripheralDevice entity) {
        if (entity == null) return null;

        PeripheralDeviceDTO dto = new PeripheralDeviceDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLocation(entity.getLocation());
        dto.setType(entity.getType().name()); // Enum to String
        dto.setStatus(entity.getStatus().name());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getConnectionStrategies() != null && !entity.getConnectionStrategies().isEmpty()) {
            // giả sử lấy connection đầu tiên để hiển thị chính
            dto.setConnection(toDTO(entity.getConnectionStrategies().get(0)));
        }

        return dto;
    }

    public static ConnectionStrategyDTO toDTO(ConnectionStrategy entity) {
        if (entity == null) return null;

        ConnectionStrategyDTO dto = new ConnectionStrategyDTO();
        dto.setId(entity.getId());
        dto.setConnectionType(entity.getConnectionType().name()); // Enum to String
        dto.setIpAddress(entity.getIpAddress());
        dto.setPort(entity.getPort());
        dto.setAgentId(entity.getAgentId());
        return dto;
    }

    public static PeripheralDevice fromCreateRequest(CreatePeripheralDeviceRequest req) {
        PeripheralDevice entity = new PeripheralDevice();
        entity.setName(req.getName());
        entity.setLocation(req.getLocation());
        entity.setType(DeviceType.valueOf(req.getType().toUpperCase()));
        entity.setStatus(DeviceStatus.DISCONNECTED); // default
        return entity;
    }

    public static void updateFromRequest(PeripheralDevice entity, UpdatePeripheralDeviceRequest req) {
        entity.setName(req.getName());
        entity.setLocation(req.getLocation());
        entity.setType(DeviceType.valueOf(req.getType().toUpperCase()));
        entity.setStatus(DeviceStatus.valueOf(req.getStatus().toUpperCase()));
    }

    public static ConnectionStrategy fromRequest(ConnectionStrategyRequest req) {
        ConnectionStrategy entity = new ConnectionStrategy();
        entity.setConnectionType(ConnectionType.valueOf(req.getConnectionType().toUpperCase()));
        entity.setIpAddress(req.getIpAddress());
        entity.setPort(req.getPort());
        entity.setAgentId(req.getAgentId());
        return entity;
    }

    public static void updateConnection(ConnectionStrategy entity, ConnectionStrategyRequest req) {
        entity.setConnectionType(ConnectionType.valueOf(req.getConnectionType().toUpperCase()));
        entity.setIpAddress(req.getIpAddress());
        entity.setPort(req.getPort());
        entity.setAgentId(req.getAgentId());
    }
}
