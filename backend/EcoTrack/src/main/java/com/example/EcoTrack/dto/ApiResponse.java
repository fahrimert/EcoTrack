package com.example.EcoTrack.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private int status;

    // Builder pattern ile kullanım kolaylığı
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data, null, HttpStatus.OK.value());
    }

    public static ApiResponse<?> error(String message, List<String> errors, HttpStatus status) {
        return new ApiResponse<>(false, message, null, errors, status.value());
    }

    // Constructor (private yapıp builder kullanılmasını sağlıyoruz)
    private ApiResponse(boolean success, String message, T data, List<String> errors, int status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.status = status;
    }

    // Getter'lar
}