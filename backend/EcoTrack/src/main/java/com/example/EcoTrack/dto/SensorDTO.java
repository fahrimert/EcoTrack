package com.example.EcoTrack.dto;

import com.example.EcoTrack.model.SensorFix;
import com.example.EcoTrack.model.SensorStatus;
import com.example.EcoTrack.repository.SensorSessionRepository;
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
    private SensorFix  currentSensorSession;
}