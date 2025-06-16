package com.example.EcoTrack.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AllSensorSessionDTOForManager {
        private  Long id;
        private  String sensorName;
        private  String firstName;
        private  String surName;
        private  boolean isOnline;


}
