package com.tien.restaurant.central.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// EmployeeAccount.java (entity cho central_db)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_accounts")
public class EmployeeAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
