package com.example.EcoTrack.notification.model;

import com.example.EcoTrack.notification.type.NotificationType;
import com.example.EcoTrack.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "notification")

public class Notification {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long Id;

    private  String supervizorDescription;
    private LocalDateTime superVizorDeadline;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Long senderId;
    private  Long receiverId;
    private  Long taskId;
    private Long pdfReportId;

    @Column(name = "is_Read", nullable = false)
    private  Boolean isRead = false;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_notification_id")
    private User userNotifications;


    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
