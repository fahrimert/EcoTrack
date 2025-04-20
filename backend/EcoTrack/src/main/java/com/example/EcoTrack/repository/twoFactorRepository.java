package com.example.EcoTrack.repository;

import com.example.EcoTrack.model.TwoFactorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface twoFactorRepository extends JpaRepository<TwoFactorCode,Long> {
    TwoFactorCode findBytwoFactorToken(String token);

}
