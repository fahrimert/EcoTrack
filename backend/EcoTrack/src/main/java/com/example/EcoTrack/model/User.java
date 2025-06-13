package com.example.EcoTrack.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  String email;
    @Column(unique = true)
    private String firstName;
    private  String surName;


    private  String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refresh_token_id",referencedColumnName = "id")
    private RefreshToken refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;


    private  boolean isTwoFactorAuthbeenverified = false  ;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "two_factor_code_id",referencedColumnName = "id")

    private TwoFactorCode twoFactorCode;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_location_code_id",referencedColumnName = "id")
    private UserLocation userLocation;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<SensorFix> sensorSessions;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_online_status_id",referencedColumnName = "id")
    private UserOnlineStatus userOnlineStatus;

    @OneToMany(mappedBy = "assignedTo")
    private List<Task> tasksAssignedToMe;

    @OneToMany(mappedBy = "assignedBy")
    private List<Task> tasksIAssigned;

    @OneToMany(mappedBy = "userNotifications")
    private List<Notification> notifications;

    @Column(name = "is_active")
    private Boolean isActive = true;

    private Date lastLoginTime;


}
