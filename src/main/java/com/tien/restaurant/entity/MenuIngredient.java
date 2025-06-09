package com.tien.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "menu_ingredients")
// Đây là bảng trung gian giữa inventory và menu
public class MenuIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private InventoryItem inventoryItem;

    private BigDecimal quantity; // số lượng nguyên liệu cho 1 đơn vị món ăn

    private String unit; // đơn vị nguyên liệu (có thể khác đơn vị hiển thị trong kho)
}
