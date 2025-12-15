package com.example.EcoTrack.sensors.dto.sensorSolvingDtos;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SensorSolvingSensorFixDto {
    private Long id;
    private String note;
    private Date startTime;
    private Date completedTime;

    private Long userId;

}
