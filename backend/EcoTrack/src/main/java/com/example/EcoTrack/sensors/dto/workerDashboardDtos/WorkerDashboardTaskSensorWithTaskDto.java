package com.example.EcoTrack.sensors.dto.workerDashboardDtos;

import com.example.EcoTrack.shared.dto.SensorDTO;
import com.example.EcoTrack.task.dto.UserTaskDTO;
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
public class WorkerDashboardTaskSensorWithTaskDto {
    private Long id;

    private WorkerDashboardTaskWithSensorDto taskSensors;

    private String superVizorDescription;
    private LocalDateTime superVizorDeadline;
    private UserTaskDTO assignedBy;

    private Boolean workerArriving;
    private Boolean workerArrived;

    private String workerNote;
    private String solvingNote;
    private List<TaskImages> taskImages;
    private Date taskCompletedTime;

}
