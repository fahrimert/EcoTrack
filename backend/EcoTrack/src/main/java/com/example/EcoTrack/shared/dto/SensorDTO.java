package com.example.EcoTrack.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SensorDTO {
    private  Long id;
    private  String sensorName;
    private String status;
    private  String color_code;
    private double latitude;
    private double longitude;
    private SensorFixDTO  currentSensorSession;
}