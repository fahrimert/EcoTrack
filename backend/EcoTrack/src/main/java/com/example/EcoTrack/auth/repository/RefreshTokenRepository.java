package com.example.EcoTrack.repository;

import com.example.EcoTrack.model.RefreshToken;
import com.example.EcoTrack.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    RefreshToken  findBytoken(String token);

    RefreshToken findByUser(User dbUser);

    RefreshToken findByUserId(Long id);
}
