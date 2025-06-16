package com.example.EcoTrack.sensors.dto;
import com.example.EcoTrack.shared.dto.ImageResponseDTO;
import com.example.EcoTrack.user.dto.AllSensorSessionDTOForManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AllSensorForManagerDTO {
    private  Long id;
    private  String sensorName;
    private ImageResponseDTO imageResponseDTO;
    private String status;
    private  String color_code;
    private double latitude;
    private double longitude;
    private Date installationDate;
    private Date lastUpdatedAt;

    private AllSensorSessionDTOForManager currentSensorSession;

}
