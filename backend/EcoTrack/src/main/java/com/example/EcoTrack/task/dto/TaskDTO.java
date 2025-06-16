package com.example.EcoTrack.dto;

import lombok.*;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
        private  Long Id;
        private String superVizorDescription;
        private LocalDateTime superVizorDeadline;
        private UserTaskDTO assignedTo;
        private UserTaskDTO assignedBy;

        private SensorTaskDTO sensorDTO;

        private Boolean workerArriving;
        private Boolean workerArrived;

}
