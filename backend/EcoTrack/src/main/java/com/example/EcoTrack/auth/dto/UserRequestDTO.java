    package com.example.EcoTrack.auth.dto;

    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Pattern;
    import jakarta.validation.constraints.Size;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
        public class UserRequestDTO {

            @NotBlank(message = "Email Kısmı Boş Kalamaz")
            @Email(message = "Email Formatı Yanlış")
            private String email;
            @NotBlank(message = "İsim Kısmı Boş Kalamaz")
            @Size(min = 2, max = 30, message = "İsim 2 ila 30 karakter arasında olmalı")
         @Pattern(regexp = "^[\\p{L}\\p{M}\\s\\d.,!?()-]+$", message = "Emoji veya geçersiz karakter")
            private  String firstName;
            @NotBlank(message = "Parola Kısmı Boş Kalamaz")
            private  String password;

        }
