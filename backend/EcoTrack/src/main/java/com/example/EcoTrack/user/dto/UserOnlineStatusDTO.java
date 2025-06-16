package com.example.EcoTrack.dto;

import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.UserOnlineStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOnlineStatusDTO {
    private  Long id;
    private  String firstName;
    private String  surName;
    private Role role;
    private UserOnlineStatus userOnlineStatus;




}
