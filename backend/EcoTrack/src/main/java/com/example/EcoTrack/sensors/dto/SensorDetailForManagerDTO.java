package com.example.EcoTrack.dto;

//management -  sensor management page UPDATE COMPONENT ınıtıaldata dto

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SensorDetailForManagerDTO {
    private  String sensorName;
    private  ImageResponseDTO imageResponseDTO;
}
