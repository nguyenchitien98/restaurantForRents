package com.tien.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyOrderStatResponse {
    private Date name;
    private long orders;
}
