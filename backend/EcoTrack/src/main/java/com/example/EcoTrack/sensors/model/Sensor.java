package com.example.EcoTrack.model;

import com.example.EcoTrack.task.model.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "sensors")

public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String sensorName;

    @Enumerated(EnumType.STRING)
    private SensorStatus status;

    //bi user birden fazla sensor çözebilir bi sensor birden fazla user tarafından çözülebilir

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sensor_location_id",referencedColumnName = "id")
    private SensorLocation sensorLocation;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sensor_icon_image_id",referencedColumnName = "id")
    private SensorIconImage sensorIconImage;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sensor_session_id",referencedColumnName = "id")
    private SensorFix currentSensorSession;

    @OneToMany(mappedBy = "sensor")
    private List<Task> tasks;

    private  Date lastUpdatedAt;

    private Date installationDate;



}
