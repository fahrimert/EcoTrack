package com.example.EcoTrack.sensors.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sensorsessionimages")
@Getter
@Setter
public class SensorSessionImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String name;
    private String type; //bu filetype normalde

    @Lob
    @Column(name = "imageData",length = 1000)
    private byte[] image;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_session_id")
    private SensorFix sensorSessions;



}
