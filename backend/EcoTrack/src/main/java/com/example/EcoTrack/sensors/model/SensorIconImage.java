package com.example.EcoTrack.sensors.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sensorıconimage")
@Getter
@Setter
public class SensorIconImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String name;
    private String type; //bu filetype normalde

    @Lob
    @Column(name = "imageData",length = 1000)
    private byte[] image;


    @JsonIgnore

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;



}
