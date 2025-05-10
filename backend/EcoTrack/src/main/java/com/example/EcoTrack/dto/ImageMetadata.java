package com.example.EcoTrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageMetadata {
    private Long id;
    private String fileName;
    private String fileType;
    private String downloadUrl;
    // constructor, getters and setters
}