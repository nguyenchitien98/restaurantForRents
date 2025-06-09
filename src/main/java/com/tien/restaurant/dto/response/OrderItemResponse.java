package com.tien.restaurant.dto.response;

import com.tien.restaurant.entity.OrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Long id;
    private int quantity;
    private BigDecimal price;
    private String note;
    private OrderItemStatus status;
    private String menuName;
}
