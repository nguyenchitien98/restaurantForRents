package com.tien.restaurant.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tables")
@Data
public class DiningTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_number")
    private Integer tableNumber;

    private Integer capacity;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
