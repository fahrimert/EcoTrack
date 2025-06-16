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


    public  List<SensorSessionImages> uploadImage(List<MultipartFile> files,Long sessionId) throws  IOException{
        List<SensorSessionImages> savedImageDto = new ArrayList<>();
        SensorFix sensorSession = sensorSessionRepository.findById(sessionId).orElseThrow();

        for(MultipartFile file:files){
            SensorSessionImages sensorSessionImagess = new SensorSessionImages();
            sensorSessionImagess.setName(file.getOriginalFilename());
            sensorSessionImagess.setType(file.getContentType());
            sensorSessionImagess.setSensorSessions(sensorSession);
            sensorSessionImagess.setImage(ImageUtil.compressImage(file.getBytes()));

            sensorSessionImagesRepository.save(sensorSessionImagess);
            savedImageDto.add(sensorSessionImagess);

        }


return  savedImageDto;
    }


}
