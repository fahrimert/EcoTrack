package com.example.EcoTrack.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "sensors")

public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(
//            name = "sensor_id"
//    )
//    @JsonBackReference
//    private  User solvedBy;
//


    private Date installationDate;



}
