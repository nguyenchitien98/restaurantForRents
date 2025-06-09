package com.tien.restaurant.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderItemRequest {
    private Long menuId;
    private int quantity;
    private BigDecimal price;
    private String note;
}
