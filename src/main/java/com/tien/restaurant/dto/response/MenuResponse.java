package com.tien.restaurant.dto.response;

import com.tien.restaurant.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponse {
    private Long id;
    private String name;
    private String image;
    private String description;
    private BigDecimal price;
    private Boolean isAvailable;
    private Long categoryId;
    private String categoryName;

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getImage(),
                menu.getDescription(),
                menu.getPrice(),
                menu.getIsAvailable(),
                menu.getCategory() != null ? menu.getCategory().getId() : null,
                menu.getCategory() != null ? menu.getCategory().getName() : null
        );
    }
}