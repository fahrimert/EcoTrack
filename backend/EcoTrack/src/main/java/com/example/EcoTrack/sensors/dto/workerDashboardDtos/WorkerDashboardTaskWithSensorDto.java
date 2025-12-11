package com.example.EcoTrack.sensors.dto.workerDashboardDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class WorkerDashboardTaskWithSensorDto {
    private Long id;
    private String sensorName;
    private String status;
    private String color_code;
    private double latitude;
    private double longitude;

    private WorkerDashboardTaskWithSensorFixDto currentSensorSession;
}
