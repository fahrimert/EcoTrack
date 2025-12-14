package com.example.EcoTrack.auth.controller;

import com.example.EcoTrack.auth.dto.AuthResponseDto;
import com.example.EcoTrack.auth.dto.RefreshTokenRequestDto;
import com.example.EcoTrack.auth.dto.UserRequestDTO;
import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@Tag(name = "Auth",description = "API for authentication operations")
public class AuthController {
    private AuthService authService;

    public AuthController( AuthService authService ) {
        this.authService = authService;
    }

    //Login Endpoint For Login Functionality
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody UserRequestDTO loginRequest) {

        AuthResponseDto authResponse = authService.login(loginRequest);

        return ResponseEntity
                .ok(ApiResponse.success(authResponse));
    }

        @CrossOrigin(
                origins = "http://localhost:9595", // veya frontend URL’in
                allowedHeaders = "*",
                methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
        )

        //Logout Endpoint For Logout Functionality
        @PostMapping("/auth/customLogout")
        public ResponseEntity<ApiResponse<Boolean>> logout(@RequestBody RefreshTokenRequestDto request) {
            authService.logout(request.getRefreshToken());
            return ResponseEntity.ok(ApiResponse.success(true));
        }




}
