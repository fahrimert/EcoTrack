package com.example.EcoTrack.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "twoFactorCode")
public class TwoFactorCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long Id;
    private String twoFactorToken;
    private Date expiresAt;

    @OneToOne(mappedBy = "twoFactorCode")
    @JoinColumn(name = "user_id")
    private User user;
}
