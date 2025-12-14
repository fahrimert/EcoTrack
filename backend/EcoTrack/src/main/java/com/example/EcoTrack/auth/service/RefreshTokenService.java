package com.example.EcoTrack.auth.service;

import com.example.EcoTrack.auth.exception.RefreshTokenException;
import com.example.EcoTrack.auth.model.RefreshToken;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.auth.repository.RefreshTokenRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;


@Service
public class RefreshTokenService  {
    private final RefreshTokenRepository refreshTokenRepository;
    private  final JwtService jwtService;
    private  final UserRepository userRepository;
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    private static final long EXPIRATION_MS = 7 * 24 * 60 * 1000;

    public String createRefreshToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiresAt(expiryDate);

        user.getRefreshTokens().add(refreshToken);
        refreshTokenRepository.save(refreshToken);
        userRepository.save(user);
        return refreshToken.getToken();
    }
    public String findByToken(String requestRefreshToken){
        RefreshToken token = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> new RefreshTokenException("Refresh token veritabanında bulunamadı!"));

        if (token.getExpiresAt().before(new Date())) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException("Refresh token süresi dolmuş. Lütfen tekrar giriş yapın.");
        }

        return jwtService.generateToken(token.getUser().getEmail());

    }

    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
