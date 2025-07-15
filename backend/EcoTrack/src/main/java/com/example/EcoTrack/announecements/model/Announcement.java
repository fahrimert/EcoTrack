package com.example.EcoTrack.announecements.model;

import com.example.EcoTrack.user.model.Role;
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
@Table(name = "announcement")

public class Announcement {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long Id;

    private  String title;
    private String content;


    private Long senderId;
    private  Long receiverId;

    @Column(name = "is_Read")
    private  Boolean isRead = false;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "annonucement_id")
    private User userAnnonucements;
    private LocalDateTime createdAt;

}
