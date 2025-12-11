package com.example.EcoTrack.sensors.dto.workerDashboardDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDashboardTaskWithSensorFixDto {
    private Long id;
    private String note;
    private Date startTime;
    private Date completedTime;
}
