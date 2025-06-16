package com.example.EcoTrack.dto;

import com.example.EcoTrack.auth.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private  Long id;
    private  String firstName;
    private String  surName;
    private Role role;

    private List<SensorFixDTO> sensorSessions;



}
