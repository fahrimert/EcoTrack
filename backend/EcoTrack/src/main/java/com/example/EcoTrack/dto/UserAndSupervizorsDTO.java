package com.example.EcoTrack.dto;

import com.example.EcoTrack.model.Role;
import com.example.EcoTrack.model.UserOnlineStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndSupervizorsDTO {
    private  Long id;
    private  String firstName;
    private  String email;
    private String  surName;
    private  Role role;
    private Date lastLoginTime;
    private LocalDateTime lastOnlineTime;



}
