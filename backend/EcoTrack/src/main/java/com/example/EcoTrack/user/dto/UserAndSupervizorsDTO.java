package com.example.EcoTrack.user.service;

import com.example.EcoTrack.user.model.Role;
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
