package com.tien.restaurant.dto.request;

import com.tien.restaurant.entity.OrderType;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private Long tableId;
    private OrderType orderType; // DINE_IN, TAKEAWAY, DELIVERY
    private String customerName;
    private String phoneNumber;
    private String deliveryAddress;
    private Long employeeId;
    private String note;
    private List<CreateOrderItemRequest> items;
}