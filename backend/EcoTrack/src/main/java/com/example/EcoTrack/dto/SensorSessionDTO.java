package com.example.EcoTrack.dto;

import com.example.EcoTrack.model.SensorFix;
import com.example.EcoTrack.model.SensorStatus;
import com.example.EcoTrack.repository.SensorSessionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Date completedTime;
    private double latitude;
    private double longitude;


}