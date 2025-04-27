package com.example.EcoTrack.controller;

import com.example.EcoTrack.dto.ApiResponse;
import com.example.EcoTrack.dto.UserRequestDTO;
import com.example.EcoTrack.model.TwoFactorCode;
import com.example.EcoTrack.model.User;
import com.example.EcoTrack.repository.UserRepository;
import com.example.EcoTrack.service.*;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RefreshTokenService refreshTokenService;
    private TwoFactorTokenService twoFactorTokenService;
    private JwtService jwtService;
    private OTPService otpService;
    public AuthController(UserService userService, UserRepository userRepository, RefreshTokenService refreshTokenService, TwoFactorTokenService twoFactorTokenService, JwtService jwtService, OTPService otpService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.twoFactorTokenService = twoFactorTokenService;
        this.jwtService = jwtService;
        this.otpService = otpService;
    }



    @PostMapping("/login")
        public ResponseEntity<ApiResponse<?>> login(@RequestBody UserRequestDTO loginRequest, HttpServletRequest request) {
        return userService.login(loginRequest);

    }

    @GetMapping("/user/{accessToken}")
    public Claims accessTokenController(HttpServletRequest request , HttpServletResponse response , @PathVariable String accessToken){
        return  jwtService.extractAllClaims(accessToken);
    }
    @GetMapping("/user/profile/{accessToken}")
    public User profileController(HttpServletRequest request , HttpServletResponse response , @PathVariable String accessToken){
        Claims claims =  jwtService.extractAllClaims(accessToken);
        User user = userService.findByUsername(claims.getSubject());
        return user;

    }

    @PostMapping("/refreshToken/{refreshToken}")
    public String refreshTokenController(HttpServletRequest request, HttpServletResponse response, @PathVariable String refreshToken ){
return  refreshTokenService.findByToken(refreshToken,request,response);
    }


    @PostMapping("/twofactorToken")
    public ResponseEntity<String> twoFactorTokenController(@RequestBody TwoFactorCode twoFactorCode) {

        return  twoFactorTokenService.verifyTwoFactorToken(twoFactorCode.getTwoFactorToken());
    }



}
