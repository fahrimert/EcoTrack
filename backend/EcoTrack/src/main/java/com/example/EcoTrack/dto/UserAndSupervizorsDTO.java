package com.example.EcoTrack.dto;

import com.example.EcoTrack.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOnlineStatusDTO {
    private  Long id;
    private  String firstName;
    private String  surName;
    private  Role role;
    private UserOnlineStatus userOnlineStatus;




}
