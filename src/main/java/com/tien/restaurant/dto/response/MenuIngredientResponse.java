package com.tien.restaurant.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuIngredientResponse {
    private Long id;
    private Long menuId;
    private Long inventoryItemId;
    private BigDecimal quantity;
    private String unit;
}
