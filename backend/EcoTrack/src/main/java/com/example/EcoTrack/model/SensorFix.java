package com.example.EcoTrack.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

public class SensorFix {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  String Note;


    @OneToMany(mappedBy = "sensorFix",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<SensorSolvingImages> sensorSolvingImagesList;

    //bi user birden fazla sensor çözebilir bi sensor birden fazla user tarafından çözülebilir


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "sensor_id"
    )
    @JsonBackReference
    private  Sensor sensor;


    private Date startTime;

    private Date completedTime;

}
