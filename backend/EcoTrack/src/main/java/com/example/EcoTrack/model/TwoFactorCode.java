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
@Table(name = "twoFactorCode")
public class TwoFactorCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long Id;
    private String twoFactorToken;
    private Date expiresAt;

    @OneToOne(mappedBy = "twoFactorCode")
    @JoinColumn(name = "user_id")
    private Usera user;
}
