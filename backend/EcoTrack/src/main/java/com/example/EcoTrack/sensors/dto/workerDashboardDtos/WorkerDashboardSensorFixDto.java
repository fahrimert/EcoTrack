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
public class WorkerDashboardSensorFixDto {
    private Long id;
    private Date startTime;
    private Long userId;
}
