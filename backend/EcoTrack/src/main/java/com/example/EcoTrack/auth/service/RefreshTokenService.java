package com.example.EcoTrack.auth.service;

import com.example.EcoTrack.auth.model.RefreshToken;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.auth.repository.RefreshTokenRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


@Service
public class RefreshTokenService  {
    private final RefreshTokenRepository refreshTokenRepository;
    private  final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }
    Date now = new Date();
    private static final long EXPIRATION_MS = 7 * 24 * 60 * 1000; // 7 gün

    Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);

    //Create Refresh Token OnLogin For User
    public  String  createRefreshToken(String username, User dbUser, UserRepository userRepository){

        RefreshToken existingRefreshToken = refreshTokenRepository.findByUser(dbUser);

        if (existingRefreshToken == null ){
            RefreshToken refreshToken =new RefreshToken(
            ) ;
            refreshToken.setUser(dbUser);
            refreshToken.setExpiresAt(expiryDate);
            refreshToken.setToken(UUID.randomUUID().toString());
            dbUser.setRefreshToken(refreshToken);
            RefreshToken createRefreshToken = refreshTokenRepository.save(refreshToken);
            return  createRefreshToken.getToken();
        }
        else {
            existingRefreshToken.setExpiresAt(expiryDate);
            existingRefreshToken.setToken(UUID.randomUUID().toString());
            RefreshToken updateRefreshToken = refreshTokenRepository.save(existingRefreshToken);
            return  updateRefreshToken.getToken();
        }

    }
    public String findByToken(String token, HttpServletRequest request, HttpServletResponse response){
        RefreshToken tokenn = refreshTokenRepository.findBytoken(token);

        User usera = tokenn.getUser();
        RefreshToken prevToken = expiredVerify(tokenn);

        if (prevToken != null){
            return  jwtService.generateToken(usera.getFirstName());
        }
        else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            new SecurityContextLogoutHandler().logout(request, response, authentication);

        }

        return token;
    }

    public RefreshToken expiredVerify(RefreshToken refreshToken){
        Date now = new Date();

        if (refreshToken.getExpiresAt().before(now)){
          refreshTokenRepository.deleteById(refreshToken.getId());
          throw  new RuntimeException("Refresh Token Zamanı Doldu");
        }
        else {
            return  refreshToken;
        }
    }

    public RefreshToken findByUser(User user) {
        return  refreshTokenRepository.findByUser(user);
    }

    public RefreshToken findByUserId(Long id) {
        return  refreshTokenRepository.findByUserId(id);

    }
}
