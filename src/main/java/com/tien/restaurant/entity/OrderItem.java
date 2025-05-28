package com.tien.restaurant.entity;

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
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Integer quantity;

    private BigDecimal price;
}
