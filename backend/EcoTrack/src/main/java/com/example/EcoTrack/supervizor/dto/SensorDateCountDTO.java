package com.example.EcoTrack.supervizor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SensorDateCountDTO {
    private LocalDate date;
    private  Long count;
}
