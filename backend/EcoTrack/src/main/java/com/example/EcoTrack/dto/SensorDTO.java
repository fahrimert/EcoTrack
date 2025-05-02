package com.example.EcoTrack.dto;

import com.example.EcoTrack.model.SensorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorDTO {
    private  String sensorName;
    private SensorStatus status;
    private double latitude;
    private double longitude;
}