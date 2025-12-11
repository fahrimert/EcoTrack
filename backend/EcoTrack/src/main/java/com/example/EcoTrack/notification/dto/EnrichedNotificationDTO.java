package com.example.EcoTrack.notification.dto;

import com.example.EcoTrack.notification.type.NotificationType;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO; // Senin mevcut User DTO'n
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrichedNotificationDTO {
    private Long id;
    private String supervizorDescription;
    private LocalDateTime superVizorDeadline;
    private LocalDateTime createdAt;
    private NotificationType notificationType;
    private Long senderId;
    private Long receiverId;
    private Long taskId;
    private boolean isread;
    private UserOnlineStatusDTO sender;
}