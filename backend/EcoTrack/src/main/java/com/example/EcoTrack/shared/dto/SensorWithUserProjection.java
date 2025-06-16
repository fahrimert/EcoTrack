package com.example.EcoTrack.dto;

import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.sensors.model.SensorStatus;

import java.util.Date;

public interface SensorWithUserProjection {
    Long getSensorsid();
    Long getSensorsessionsid();
    String getSensorName();
    String getNote();
    SensorStatus getFinalStatus();
    Date getStartTime();
    Date getCompletedTime();
    Long getUserid();
    String getFirstName();
    String getSurName();
    Role getRole();
}
