package com.tien.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;

// Thêm @BatchSize(size = 50) vào các entity nếu dùng saveAll() hoặc nhiều insert/update.
@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role = Role.WAITER; // eg: ADMIN, CASHIER, KITCHEN, WAITER

    @Column(unique = true)
    private String email;

    private String phone;

    private String password; // Thêm vào để xác thực

    @Column(name = "agent_id")
    private String agentId;  // Mã thiết bị hợp lệ nếu cần ràng buộc
}
