package com.example.EcoTrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "refreshtoken")
public class RefreshToken {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long Id;
    private String token;
    private Date expiresAt;


    @OneToOne(mappedBy = "refreshToken")
    @JoinColumn(name = "user_id")
    @JsonIgnore

    private User user;
}
