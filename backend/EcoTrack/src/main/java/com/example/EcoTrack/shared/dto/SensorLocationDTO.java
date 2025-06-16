package com.example.EcoTrack.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorLocationDTO {
    private  Long id;
    private double latitude;
    private double longitude;
}