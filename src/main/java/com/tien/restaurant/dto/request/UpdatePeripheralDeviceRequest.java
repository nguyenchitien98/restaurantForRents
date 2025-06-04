package com.tien.restaurant.dto.request;

import lombok.Data;

@Data
public class UpdatePeripheralDeviceRequest {
    private String name;
    private String location;
    private String type;
    private String status;

    private ConnectionStrategyRequest connection;
}