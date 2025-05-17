package com.example.EcoTrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorFilterBasedOnStatsDTO {

    private  Number solved;
    private  Number inRepair;
    private  Number faulty;

}
