package com.example.mertsecurity.repository;

import com.example.mertsecurity.model.RefreshToken;
import lombok.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken,Long> {
    RefreshToken  findBytoken(String token);

}
