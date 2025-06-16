package com.example.EcoTrack.dto;

import com.example.EcoTrack.sensors.model.SensorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class SensorSessionDTO {
    private  Long id;
    private  String sensorName;
    private String displayName;
    private String color_code;
    private List<ImageResponseDTO> imageResponseDTO;
    private ImageResponseDTO sensorIconImage;
    private  String note;
    private Date startTime;
    private  SensorStatus finalStatus;
    private Date completedTime;
    private double latitude;
    private double longitude;


}