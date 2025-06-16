package com.example.EcoTrack.sensors.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point location;

    @JsonIgnore
    @OneToOne(mappedBy = "sensorLocation")
    @JoinColumn(name = "sensor_location_code_id")
    private Sensor sensor;

    private Date createdAt;
}
