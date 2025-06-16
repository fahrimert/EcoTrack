package com.example.EcoTrack.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationDTO {
    private  Long id;
    private double latitude;
    private double longitude;
}