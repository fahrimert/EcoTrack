package com.example.EcoTrack.dto;

import com.example.EcoTrack.model.Sensor;
import com.example.EcoTrack.model.User;
import jakarta.persistence.*;
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
