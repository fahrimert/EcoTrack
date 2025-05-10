package com.example.EcoTrack.service;

import com.example.EcoTrack.dto.ApiResponse;
import com.example.EcoTrack.dto.ImageDTO;
import com.example.EcoTrack.dto.SessionImageDTO;
import com.example.EcoTrack.model.SensorFix;
import com.example.EcoTrack.model.SensorSessionImages;
import com.example.EcoTrack.repository.SensorSessionImagesRepository;
import com.example.EcoTrack.repository.SensorSessionRepository;
import com.example.EcoTrack.util.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorSessionImageService {
    private final SensorSessionRepository sensorSessionRepository;
    private final SensorSessionImagesRepository sensorSessionImagesRepository;

    public List<SessionImageDTO> saveImages(List<MultipartFile> files, Long sessionId) {
        SensorFix sensorSession = sensorSessionRepository.findById(sessionId).orElseThrow();
        List<SessionImageDTO> savedImageDto = new ArrayList<>();
        for (MultipartFile file: files){
            try{
                SensorSessionImages images = new SensorSessionImages();
                images.setName(file.getOriginalFilename());
                images.setType(file.getContentType());
                images.setImage(ImageUtil.compressImage(file.getBytes()));
                images.setSensorSessions(sensorSession);


                SensorSessionImages savedImage = sensorSessionImagesRepository.save(images);

                sensorSessionImagesRepository.save(savedImage);

                SessionImageDTO imageDto = new SessionImageDTO();

                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getType());
                savedImageDto.add(imageDto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return  savedImageDto;
    }

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



//        public List<ImageDTO> getAllImagesForSession(Long sessionId) {
//            List<byte[]> images = sensorSessionImagesRepository.findBySensorSessionsId(sessionId);
//
//            return images.stream()
//                    .map(this::convertToDto)
//                    .collect(Collectors.toList());
//        }


    @Transactional
    public byte[] getImage(String name) {

        Optional<SensorSessionImages> dbImage = Optional.ofNullable(sensorSessionImagesRepository.findByName(name).orElseThrow(() -> new RuntimeException("Image not found with id: ")));
        byte[] image = ImageUtil.decompressImage(dbImage.get().getImage());
        return image;
    }
        private ImageDTO convertToDto(SensorSessionImages image) {
            ImageDTO dto = new ImageDTO();
            dto.setId(image.getId());
            dto.setFileName(image.getName());
            dto.setFileType(image.getType());
            dto.setBase64Data(ImageUtil.decompressImage(image.getImage()));

            // Base64 kodlaması yaparken transaction içinde kalmaya dikkat edin

            return dto;
        }
}
