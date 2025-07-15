package com.example.EcoTrack.notification.dto;

import com.example.EcoTrack.notification.type.NotificationType;
import com.example.EcoTrack.pdfReports.dto.PdfRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerNotificationDTO {
    private String sensorName;
    private String senderName;
    private  Long Id;
    private  boolean isread;


}
