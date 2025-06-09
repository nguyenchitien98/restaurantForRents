package com.tien.restaurant.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PeripheralDeviceResponse {
    private Long id;
    private String name;
    private String location;
    private String type; // printer, cash_drawer, customer_display
    private String status; // connected, disconnected, error
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ConnectionStrategyResponse connection; // thông tin kết nối đi kèm
}
