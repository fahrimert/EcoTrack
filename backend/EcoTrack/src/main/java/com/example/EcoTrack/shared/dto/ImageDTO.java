package com.example.EcoTrack.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {

    private Long id;
    private String fileName;
    private String fileType;
    private byte[] base64Data;
}
