package com.example.EcoTrack.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "user")
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




}
