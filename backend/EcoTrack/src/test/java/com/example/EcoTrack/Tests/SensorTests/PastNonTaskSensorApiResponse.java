package com.example.EcoTrack.Tests.SensorTests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PastNonTaskSensorApiResponse {
    private  boolean success;
    private  String message;
    private Object data;
    private Object errors;
    private int statusCode;

}
