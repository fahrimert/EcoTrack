package com.example.EcoTrack.dto;

import com.example.EcoTrack.model.Role;
import com.example.EcoTrack.model.SensorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorWithUserDTO {
    private  Long sensorsid;
    private  Long sensorsessionsid;
    private  String sensorName;
    private  String note;
    private SensorStatus finalStatus;
    private Date startTime;
    private Date completedTime;

    private  Long userid;
    private  String firstName;
    private String  surName;
    private Role role;

}
