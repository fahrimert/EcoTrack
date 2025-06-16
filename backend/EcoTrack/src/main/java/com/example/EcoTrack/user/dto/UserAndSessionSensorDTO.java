package com.example.EcoTrack.dto;

import com.example.EcoTrack.sensors.model.Sensor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndSessionSensorDTO {
    private  Long id;
    private  String name;
    private double latitude;
    private double longitude;
    private double sensorlatitude;
    private double sensorlongitude;
    private Sensor sensor;
}
