package com.example.EcoTrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

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
