package com.tien.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Thêm @BatchSize(size = 50) vào các entity nếu dùng saveAll() hoặc nhiều insert/update.
@BatchSize(size = 50)
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableEntity table;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status= OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    private OrderType orderType;

    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "delivery_address")
    private String deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String note;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}