package com.example.EcoTrack.auth.controller;

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
    @Operation(summary = "Generate a accessToken and refreshToken on user login")
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid  @RequestBody UserRequestDTO loginRequest,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Validation Error", errors, HttpStatus.BAD_REQUEST));
        }
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
