package com.tien.restaurant.dto.request;

import lombok.Data;

@Data
public class TutorialRequest {
    private Long id;
    private String title;
    private String category;
    private String description;
    private String ingredients;
    private String steps;
}
