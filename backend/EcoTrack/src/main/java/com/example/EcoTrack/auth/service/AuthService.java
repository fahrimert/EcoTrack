package com.example.EcoTrack.auth.service;

import com.example.EcoTrack.auth.dto.AuthResponseDto;
import com.example.EcoTrack.auth.model.RefreshToken;
import com.example.EcoTrack.auth.dto.UserRequestDTO;
import com.example.EcoTrack.auth.repository.RefreshTokenRepository;
import com.example.EcoTrack.security.customUserDetail.CustomUserDetailService;
import com.example.EcoTrack.security.principal.UserPrincipal;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class AuthService {

    private  final UserRepository userRepository;
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private  final CustomUserDetailService userDetailServicee;
    private  final JwtService jwtService;
    private  final RefreshTokenService refreshTokenService;
    private  final RefreshTokenRepository refreshTokenRepository;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, CustomUserDetailService userDetailServicee, JwtService jwtService, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailServicee = userDetailServicee;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }
    //Login functionality
    public AuthResponseDto login (UserRequestDTO userRequestDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequestDTO.getEmail(),
                        userRequestDTO.getPassword()
                )
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User dbUser = userPrincipal.getUser();

        String token = jwtService.generateToken(userPrincipal.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(dbUser);

        dbUser.setLastLoginTime(new Date());
        userRepository.save(dbUser);
        return new AuthResponseDto(token, refreshToken);

    };


    //Logout functionality
    public void logout(String refreshToken) {

        refreshTokenService.deleteByToken(refreshToken);
    }


}

