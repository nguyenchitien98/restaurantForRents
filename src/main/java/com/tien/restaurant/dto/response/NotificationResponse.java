package com.tien.restaurant.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tien.restaurant.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private String message;
    private NotificationType type;

    @JsonProperty("isRead")
    private boolean isRead; // CHÚ Ý: phải là boolean, không phải String hay Integer
}
