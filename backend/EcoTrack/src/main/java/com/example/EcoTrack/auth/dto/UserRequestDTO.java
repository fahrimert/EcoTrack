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
            @NotBlank(message = "Parola Kısmı Boş Kalamaz")
            private  String password;

        }
