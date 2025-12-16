package com.example.EcoTrack.auth.exception;


import com.example.EcoTrack.shared.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//validation üzerine gelen tüm exceptionları topluyor herhalde
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    //Bu methodun mantığı @Valid veya @Validated olarak işaretlenmiş controller methodlarını yani dtolarda böyle yapılan yerlerin doğrulaması başarısız olunca gelen bişe
    /// notnull size vb kuralları karşılamaması durumunda fırlatılan MethodArgumentNotValidException’ı yakalıyormuş

    public ResponseEntity<Map<String,String>> handleValidationException(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        //hata mesajı buradaki hashmapde

        ex.getBindingResult().getFieldErrors().forEach((error) -> errors.put(error.getField(),error.getDefaultMessage()));
        //hatanın adıyla hata mesajını alıyor getBindingResult() validationdaki başarılı ve başarısız alanları içeriyormuş
        //getFieldErrors @NotBlank @Email gibi faillemeleri listeliyormuş
        return  ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Geçersiz Parametre", List.of(ex.getMessage()), HttpStatus.BAD_REQUEST));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(
                        "Giriş Başarısız",
                        List.of("Email veya şifre hatalı"),
                        HttpStatus.UNAUTHORIZED));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Sunucu Hatası", List.of(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
