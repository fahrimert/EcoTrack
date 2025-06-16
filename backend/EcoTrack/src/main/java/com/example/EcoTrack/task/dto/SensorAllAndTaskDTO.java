package com.example.EcoTrack.task.dto;

import com.example.EcoTrack.shared.dto.SensorDTO;
import com.example.EcoTrack.task.model.TaskImages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorAllAndTaskDTO {
    SensorDTO taskSensors;
    private  Long Id;
    private String superVizorDescription;
    private LocalDateTime superVizorDeadline;
    private UserTaskDTO assignedBy;

    private Boolean workerArriving;
    private Boolean workerArrived;

    private String workerNote;
    private  String solvingNote;
    private List<TaskImages> taskImages;
    private Date taskCompletedTime;

}
