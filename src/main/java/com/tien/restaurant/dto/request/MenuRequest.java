package com.tien.restaurant.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuRequest {
    public String name;
    public String description;
    public BigDecimal price;
    public Long categoryId;
    public Boolean isAvailable;

}
