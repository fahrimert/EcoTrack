package com.example.EcoTrack.pdfReports.model;


import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorIconImage;
import com.example.EcoTrack.user.model.User;
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
@Table(name = "pdfReports")
public class PdfReports {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long Id;
    //this columns based on the data gave in report sheet component
    @ManyToOne
    @JoinColumn(name = "original_sensor_id")
    private Sensor sensor;

    private String sensorName;
    private String technicianNote;
    private String startTime;
    private String completedTime;
    private Double latitude;
    private Double longitude;

    //imageyi eklemeyi sonradan yaparım
//    @JsonIgnore
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "sensor_icon_image_id",referencedColumnName = "id")
//    private SensorIconImage sensorIconImage;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    private User supervisor;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    private Date createdAt = new Date();


}
