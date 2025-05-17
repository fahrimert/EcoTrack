package com.example.EcoTrack.model;

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
public class UserOnlineStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean isOnline;

    @JsonIgnore
    @OneToOne(mappedBy = "userOnlineStatus")
    @JoinColumn(name = "user_online_status_id")
    private User user;

    private Date createdAt;
}
