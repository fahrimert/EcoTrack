package com.example.EcoTrack.sensors.dto.ekipTakibiDtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrewJobsUserAndSessionSensorDTO {
    private Long workerId;
    private String workerName;
    private double workerLatitude;
    private double workerLongitude;
    private Boolean isOnline;
    private Long sensorId;
    private String sensorName;
    private String sensorStatus;
    private double sensorLatitude;
    private double sensorLongitude;

    private Long sessionId;
    private String startTime;

}
