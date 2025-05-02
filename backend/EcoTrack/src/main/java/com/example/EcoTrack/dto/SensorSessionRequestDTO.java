package com.example.EcoTrack.dto;

import com.example.EcoTrack.model.SensorFix;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorSessionRequestDTO {
    private  String note;
    private String statusID;
}