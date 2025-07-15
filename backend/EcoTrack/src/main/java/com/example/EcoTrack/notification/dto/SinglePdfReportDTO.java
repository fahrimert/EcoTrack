package com.example.EcoTrack.notification.dto;

import com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.shared.dto.ImageResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SinglePdfReportDTO {
    private  Long id;
    private  String sensorName;
    private String displayName;
    private String color_code;
    private List<ImageResponseDTO> imageResponseDTO;
    private ImageResponseDTO sensorIconImage;

}
