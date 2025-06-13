package com.example.EcoTrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor
public class AllSensorSessionDTOForManager {
        private  Long id;
        private  String sensorName;
        private  String firstName;
        private  String surName;
        private  boolean isOnline;


}
