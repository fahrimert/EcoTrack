package com.example.EcoTrack.dto;

import com.example.EcoTrack.sensors.model.SensorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TaskCountDTO {
    private SensorStatus name;
    private  Long count;
}
