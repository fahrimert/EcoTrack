package com.example.EcoTrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorDTO {
    private  Long id;
    private  String sensorName;
    private String status;
    private  String color_code;
    private double latitude;
    private double longitude;
    private SensorFixDTO  currentSensorSession;
}