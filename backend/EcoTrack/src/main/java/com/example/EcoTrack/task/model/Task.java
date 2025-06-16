package com.example.EcoTrack.model;

import com.example.EcoTrack.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long Id;

    private String superVizorDescription;
    private LocalDateTime superVizorDeadline;

        @ManyToOne(cascade = {CascadeType.MERGE})
        @JoinColumn(name = "assigned_to_user_id")
        private User assignedTo;

        @ManyToOne(cascade = {CascadeType.MERGE})
        @JoinColumn(name = "assigned_by_user_id")
        private User assignedBy;



    @ManyToOne
    @JoinColumn(name = "sensor_task_id")
    private Sensor sensor;


    private LocalDateTime createdAt;

    private Boolean workerArriving;
    private Boolean workerArrived;

    private String workerOnRoadNote;
    private  SensorStatus finalStatus;



    private  String solvingNote;
    @JsonIgnore
    @OneToMany(mappedBy = "task",cascade = CascadeType.ALL)
    private List<TaskImages> taskImages;
    private Date taskCompletedTime;


}
