package com.tien.restaurant.dto.request;

import com.tien.restaurant.entity.NotificationType;
import lombok.Data;

@Data
public class CreateNotificationRequest {
    private String message;
    private NotificationType type;
}