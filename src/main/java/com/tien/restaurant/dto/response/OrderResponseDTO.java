package com.tien.restaurant.dto.response;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class OrderResponseDTO {
    private Long id;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private String note;
    private TableDTO table;
    private Map<String, Integer> itemsObj; // <Tên món, Số lượng>

    @Data
    public static class TableDTO {
        private Long id;
        private int tableNumber;
    }
}