package com.example.EcoTrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDTO {
    private String name;
    private String type;
    private String base64Image;


    // Getters ve Setters (Lombok varsa @Getter/@Setter ekleyebilirsin)
}