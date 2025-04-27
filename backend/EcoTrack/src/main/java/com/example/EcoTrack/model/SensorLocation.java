package com.example.EcoTrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point location;

    @JsonIgnore
    @OneToOne(mappedBy = "userLocation")
    @JoinColumn(name = "user_location_code_id")
    private User user;

    private Date createdAt;
}
