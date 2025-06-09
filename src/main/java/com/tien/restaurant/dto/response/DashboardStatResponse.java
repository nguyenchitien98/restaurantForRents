package com.tien.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatResponse {
    private String label;
    private String value;
    private String icon;
}
