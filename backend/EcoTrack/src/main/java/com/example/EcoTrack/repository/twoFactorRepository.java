package com.example.mertsecurity.repository;

import com.example.mertsecurity.model.RefreshToken;
import com.example.mertsecurity.model.TwoFactorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface twoFactorRepository extends JpaRepository<TwoFactorCode,Long> {
    TwoFactorCode findBytwoFactorToken(String token);

}
