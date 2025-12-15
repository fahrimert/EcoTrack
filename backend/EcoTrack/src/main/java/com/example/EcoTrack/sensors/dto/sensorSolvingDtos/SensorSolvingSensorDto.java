package com.example.EcoTrack.sensors.dto.sensorSolvingDtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SensorSolvingSensorDto {
    private Long id;
    private String sensorName;
    private String status;
    private String color_code;
    private double latitude;
    private double longitude;

    private SensorSolvingSensorFixDto currentSensorSession;
}
