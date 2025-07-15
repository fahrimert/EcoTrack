package com.example.EcoTrack.auth.service;

import com.example.EcoTrack.auth.model.RefreshToken;
import com.example.EcoTrack.auth.dto.UserRequestDTO;
import com.example.EcoTrack.auth.repository.RefreshTokenRepository;
import com.example.EcoTrack.security.customUserDetail.CustomUserDetailService;
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
    public ResponseEntity<ApiResponse<?>> login (  @RequestBody UserRequestDTO user){
        // proper user validations
        User dbUser = userRepository.findByFirstName(user.getFirstName());
        if (dbUser == null ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Not Found",
                            List.of("User Not Found"),
                            HttpStatus.FORBIDDEN
                    ));
        }

        if (dbUser.getIsActive() == false){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "User Deactivated",
                            List.of("User Deactivated"),
                            HttpStatus.FORBIDDEN
                    ));
        }
        if (!dbUser.getEmail().equals(user.getEmail())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Error",
                            List.of("Credentials is wrong"),
                            HttpStatus.FORBIDDEN
                    ));
        }
        if (!dbUser.getFirstName().equals(user.getFirstName())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Error",
                            List.of("Credentials is wrong"),
                            HttpStatus.FORBIDDEN
                    ));
        }
        if (!bCryptPasswordEncoder.matches(user.getPassword(),dbUser.getPassword())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Error",
                            List.of("Credentials is wrong"),
                            HttpStatus.FORBIDDEN
                    ));
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dbUser.getFirstName(),
                        user.getPassword()
                )
        );

        //generate token and security modules for login with jwt
        UserDetails userDetails = userDetailServicee.loadUserByUsername(user.getFirstName());
        String token = jwtService.generateToken(userDetails.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(dbUser.getFirstName(), dbUser, userRepository);

        //Set Last Login Time
        Date now = new Date();
        dbUser.setLastLoginTime(now);
        userRepository.save(dbUser);


        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        Map.of(
                                "accessToken", token,
                                "refreshToken", refreshToken
                        )
                ));
    };


    //Logout functionality
    public ResponseEntity<ApiResponse<Boolean>> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByFirstName(authentication.getName());

        //necessary validations
        if (authentication != null && authentication.isAuthenticated()){
            String token = request.getHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")){
                token = token.substring(7);
            }else{
                throw new RuntimeException("Access Token Bulunamamaktadır");
            }

            RefreshToken refreshToken = refreshTokenService.findByUserId(user.getId());

            user.setRefreshToken(null);
            userRepository.save(user);
            refreshTokenRepository.delete(refreshToken);

            new SecurityContextLogoutHandler().logout(request, response, authentication);

            return ResponseEntity.ok(ApiResponse.success(true));

        }
        else {
            throw new RuntimeException("Kullanıcı Bulunamamaktadır");
        }
    }


}

