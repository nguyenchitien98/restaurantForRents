package com.tien.restaurant.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutorialResponse {
    private Long id;
    private String title;
    private String category;
    private String description;
    private String image;
    private String ingredients;
    private String steps;
}
