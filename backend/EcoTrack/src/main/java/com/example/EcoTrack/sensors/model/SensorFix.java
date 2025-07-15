package com.example.EcoTrack.sensors.model;

import com.example.EcoTrack.user.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "sensor_session")

public class SensorFix {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  String Note;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "sensor_id"
    )
    private  Sensor sensor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "user_id"
    )
    @JsonBackReference
    private User user;


    private  SensorStatus finalStatus;

    private Date startTime;

    private Date completedTime;

    @JsonIgnore
    @OneToMany(mappedBy = "sensorSessions",cascade = CascadeType.ALL)
    private List<SensorSessionImages> sensorSessionImages;

}
