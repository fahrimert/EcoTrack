package com.example.EcoTrack.auth.controller;


import com.example.EcoTrack.auth.dto.RefreshTokenRequestDto;
import com.example.EcoTrack.auth.service.RefreshTokenService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
public class RefreshTokenController {
    private RefreshTokenService refreshTokenService;

    public RefreshTokenController( RefreshTokenService refreshTokenService ) {
        this.refreshTokenService = refreshTokenService;
    }
    //Refresh Token Endpoint For Frontend Middleware Session Refreshing Functionality
    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<Map<String, String>>> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
            String newAccessToken = refreshTokenService.findByToken(refreshTokenRequestDto.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success(Map.of("accessToken", newAccessToken)));
    }



}
