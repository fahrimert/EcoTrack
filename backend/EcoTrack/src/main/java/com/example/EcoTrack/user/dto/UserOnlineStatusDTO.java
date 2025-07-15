package com.example.EcoTrack.user.dto;

import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.UserOnlineStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserOnlineStatusDTO {
    private  Long id;
    private  String firstName;
    private String  surName;
    private Role role;
    private UserOnlineStatus userOnlineStatus;




}
