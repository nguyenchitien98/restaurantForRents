package com.tien.restaurant.dto.response;

import com.tien.restaurant.entity.OrderStatus;
import com.tien.restaurant.entity.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private String customerName;
    private String phoneNumber;
    private String deliveryAddress;
    private String note;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private OrderType orderType;
    private LocalDateTime createdAt;

    private Integer table;     // hoặc tableName nếu bạn cần
    private String employee;  // hoặc employeeName

    private List<OrderItemResponse> items;
}