package com.tien.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// Thêm @BatchSize(size = 50) vào các entity nếu dùng saveAll() hoặc nhiều insert/update.
@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
