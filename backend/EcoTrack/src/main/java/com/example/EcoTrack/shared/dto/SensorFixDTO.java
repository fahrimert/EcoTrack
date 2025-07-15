package com.example.EcoTrack.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class SensorFixDTO {
    private  Long id;
    private  String sensorName;
    private String displayName;
    private String color_code;
    private  String note;
    private Date startTime;

    private Date completedTime;
    private double latitude;
    private double longitude;


}