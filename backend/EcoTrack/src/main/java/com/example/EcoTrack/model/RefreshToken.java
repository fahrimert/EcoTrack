package com.example.mertsecurity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.*;

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
    private String device_info;

    @OneToOne(mappedBy = "refreshToken")
    @JoinColumn(name = "user_id")
    private Usera user;
}
