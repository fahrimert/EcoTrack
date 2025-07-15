    package com.example.EcoTrack.user.model;


    import com.example.EcoTrack.announecements.model.Announcement;
    import com.example.EcoTrack.auth.model.RefreshToken;
    import com.example.EcoTrack.notification.model.Notification;
    import com.example.EcoTrack.pdfReports.model.PdfReports;
    import com.example.EcoTrack.sensors.model.SensorFix;
    import com.example.EcoTrack.task.model.Task;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.RequiredArgsConstructor;
    import lombok.Setter;


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


    //    private  boolean isTwoFactorAuthbeenverified = false  ;


    //    @OneToOne(cascade = CascadeType.ALL)
    //    @JoinColumn(name = "two_factor_code_id",referencedColumnName = "id")
    //
    //    private TwoFactorCode twoFactorCode;


        @JsonIgnore
        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private UserLocation userLocation;

        @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
        private List<SensorFix> sensorSessions;



        @OneToOne(cascade = CascadeType.ALL)
        private UserOnlineStatus userOnlineStatus;

        @OneToMany(mappedBy = "assignedTo")
        private List<Task> tasksAssignedToMe;

        @OneToMany(mappedBy = "assignedBy")
        private List<Task> tasksIAssigned;

        @OneToMany(mappedBy = "supervisor")
        @JsonIgnore
        private List<PdfReports> reportsISent;

        @OneToMany(mappedBy = "manager")
        @JsonIgnore
        private List<PdfReports> reportsIReceived;


        @OneToMany(mappedBy = "userNotifications")
        private List<Notification> notifications;

        @OneToMany(mappedBy = "userAnnonucements")
        private List<Announcement> announcements;

        @Column(name = "is_active")
        private Boolean isActive = true;

        private Date lastLoginTime;


    }
