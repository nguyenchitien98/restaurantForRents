package com.tien.restaurant.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tien.restaurant.entity.KitchenStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KitchenOrderResponseDTO {
    private Long id;
    private Long orderId;
    private Long orderItemId;
    private String itemName;
    private Integer quantity;
    private KitchenStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private Long tableId;
}