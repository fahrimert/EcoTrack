package com.example.EcoTrack.user.dto;

import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.shared.dto.SensorFixDTO;
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
