package com.example.EcoTrack.user.dto.pastsensors;


import com.example.EcoTrack.shared.dto.ImageResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PastSensorDetailDto {
    private Long sensorId;
    private String sensorName;
    private String sensorStatus;
    private ImageResponseDTO iconImage;

    private Long sessionId;
    private String note;
    private String finalStatus;
    private Date startTime;
    private Date completedTime;

    private double latitude;
    private double longitude;

    private List<ImageResponseDTO> evidenceImages;
}
