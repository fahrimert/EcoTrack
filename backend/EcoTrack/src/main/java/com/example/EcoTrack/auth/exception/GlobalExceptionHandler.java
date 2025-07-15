package com.example.EcoTrack.auth.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
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

}
