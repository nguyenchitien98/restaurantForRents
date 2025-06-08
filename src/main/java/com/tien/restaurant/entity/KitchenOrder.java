package com.tien.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kitchen_orders")
public class KitchenOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private Long orderItemId;

    private String itemName;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private KitchenStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

//    public enum Status {
//        PENDING, IN_PROGRESS, COMPLETED
//    }
}