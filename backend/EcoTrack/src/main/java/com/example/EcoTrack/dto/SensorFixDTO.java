package com.example.EcoTrack.dto;

import com.example.EcoTrack.model.SensorSessionImages;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

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