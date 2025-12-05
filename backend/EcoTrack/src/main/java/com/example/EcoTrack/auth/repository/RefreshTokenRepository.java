package com.example.EcoTrack.auth.repository;

import com.example.EcoTrack.auth.model.RefreshToken;
import com.example.EcoTrack.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUser(User dbUser);

    void deleteByToken(String token);
    void deleteByUser(User user);

    RefreshToken findByUserId(Long id);
}
