package com.example.EcoTrack.auth.repository;

import com.example.EcoTrack.auth.model.RefreshToken;
import com.example.EcoTrack.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    RefreshToken  findBytoken(String token);

    RefreshToken findByUser(User dbUser);

    RefreshToken findByUserId(Long id);
}
