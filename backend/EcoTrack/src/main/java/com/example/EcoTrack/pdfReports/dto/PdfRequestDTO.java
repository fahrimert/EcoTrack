package com.example.EcoTrack.pdfReports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PdfRequestDTO {
    private  Long originalSensorId;
    private String sensorName;
    private String note;
    private String startTime;
    private String completedTime;
    private double  latitude;
    private double longitude;
    private Long managerId;
    private Long supervizorId;
}
