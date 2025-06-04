package com.tien.restaurant.dto.request;

import lombok.Data;

@Data
public class CreatePeripheralDeviceRequest {
    private String name;
    private String location;
    private String type; // ENUM: printer, cash_drawer, customer_display

    private ConnectionStrategyRequest connection; // yêu cầu tạo kèm kết nối
}