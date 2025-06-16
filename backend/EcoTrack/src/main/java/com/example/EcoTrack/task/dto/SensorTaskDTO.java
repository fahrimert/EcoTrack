package com.example.EcoTrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorTaskDTO {
    private  Long id;
    private  String sensorName;
    private double latitude;
    private double longitude;
}