package com.example.EcoTrack.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
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