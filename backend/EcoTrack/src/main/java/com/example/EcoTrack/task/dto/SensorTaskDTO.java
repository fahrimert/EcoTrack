package com.example.EcoTrack.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SensorTaskDTO {
    private  Long id;
    private  String sensorName;
    private double latitude;
    private double longitude;
}