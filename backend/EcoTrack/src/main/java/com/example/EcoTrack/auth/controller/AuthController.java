package com.example.EcoTrack.auth.controller;

import com.example.EcoTrack.auth.dto.UserRequestDTO;
import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
public class AuthController {
    private AuthService authService;

    public AuthController( AuthService authService ) {
        this.authService = authService;
    }

    //Login Endpoint For Login Functionality
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody UserRequestDTO loginRequest, HttpServletRequest request) {
        return authService.login(loginRequest);
    }

    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    //Logout Endpoint For Logout Functionality
    @PostMapping("/auth/customLogout")
    public ResponseEntity<ApiResponse<Boolean>> logout( HttpServletRequest request,HttpServletResponse response) {
        return authService.logout(request,response);
    }




}
