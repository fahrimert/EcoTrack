package com.example.EcoTrack.sensors.service;

import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.sensors.model.SensorSessionImages;
import com.example.EcoTrack.sensors.repository.SensorSessionImagesRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
import com.example.EcoTrack.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorSessionImageService {
    private final SensorSessionRepository sensorSessionRepository;
    private final SensorSessionImagesRepository sensorSessionImagesRepository;


    public  void uploadImage(List<MultipartFile> files,Long sessionId) throws  IOException{
        SensorFix session = sensorSessionRepository.getReferenceById(sessionId);

        List<SensorSessionImages> imagesToSave = new ArrayList<>();

        for (MultipartFile file : files) {
            SensorSessionImages image = new SensorSessionImages();
            image.setName(file.getOriginalFilename());
            image.setType(file.getContentType());
            image.setSensorSessions(session);
            image.setImage(ImageUtil.compressImage(file.getBytes()));

            imagesToSave.add(image);
        }

        sensorSessionImagesRepository.saveAll(imagesToSave);


    }


}
