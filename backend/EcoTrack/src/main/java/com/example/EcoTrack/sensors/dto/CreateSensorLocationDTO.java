package com.example.EcoTrack.sensors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSensorLocationDTO {
    private double latitude;
    private double longitude;
}
