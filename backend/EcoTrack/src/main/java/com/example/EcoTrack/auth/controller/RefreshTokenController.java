package com.example.EcoTrack.auth.controller;


import com.example.EcoTrack.auth.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
public class RefreshTokenController {
    private RefreshTokenService refreshTokenService;

    public RefreshTokenController( RefreshTokenService refreshTokenService ) {
        this.refreshTokenService = refreshTokenService;
    }
    //Refresh Token Endpoint For Frontend Middleware Session Refreshing Functionality
    @PostMapping("/refreshToken/{refreshToken}")
    public String refreshTokenController(HttpServletRequest request, HttpServletResponse response, @PathVariable String refreshToken ){
        return  refreshTokenService.findByToken(refreshToken,request,response);
    }



}
