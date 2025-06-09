package com.tien.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;

// Thêm @BatchSize(size = 50) vào các entity nếu dùng saveAll() hoặc nhiều insert/update.
@BatchSize(size = 50)
@Entity
@Table(name = "order_items")
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    @JsonBackReference
    private Menu menu;

    private Integer quantity;

    private BigDecimal price;

    private String note;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;
}
