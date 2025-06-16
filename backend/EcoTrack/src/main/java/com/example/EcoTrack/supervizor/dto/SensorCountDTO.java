package com.example.EcoTrack.supervizor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SensorCountDTO {
    private  String name;
    private  Long count;
}
