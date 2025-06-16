package com.example.EcoTrack.service;

import com.example.EcoTrack.dto.ImageDTO;
import com.example.EcoTrack.dto.SessionImageDTO;
import com.example.EcoTrack.model.SensorFix;
import com.example.EcoTrack.model.SensorSessionImages;
import com.example.EcoTrack.model.Task;
import com.example.EcoTrack.model.TaskImages;
import com.example.EcoTrack.repository.SensorSessionImagesRepository;
import com.example.EcoTrack.repository.SensorSessionRepository;
import com.example.EcoTrack.repository.TaskImagesRepository;
import com.example.EcoTrack.repository.TaskRepository;
import com.example.EcoTrack.util.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskImageService {
    private final SensorSessionRepository sensorSessionRepository;
    private final SensorSessionImagesRepository sensorSessionImagesRepository;
    private  final TaskRepository taskRepository;
    private  final TaskImagesRepository taskImagesRepository;


    public List<SessionImageDTO> saveTaskImages(List<MultipartFile> files, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        List<SessionImageDTO> savedImageDto = new ArrayList<>();
        for (MultipartFile file: files){
            try{
                TaskImages images = new TaskImages();
                images.setName(file.getOriginalFilename());
                images.setType(file.getContentType());
                images.setImage(ImageUtil.compressImage(file.getBytes()));

                TaskImages savedImage = taskImagesRepository.save(images);

                taskImagesRepository.save(savedImage);

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

    public  List<TaskImages> uploadTaskImage(List<MultipartFile> files,Long taskId) throws  IOException{
        List<TaskImages> savedImageDto = new ArrayList<>();
        Task task = taskRepository.findById(taskId).orElseThrow();

        for(MultipartFile file:files){
            TaskImages taskImages = new TaskImages();
            taskImages.setName(file.getOriginalFilename());
            taskImages.setType(file.getContentType());
            taskImages.setImage(ImageUtil.compressImage(file.getBytes()));

            taskImagesRepository.save(taskImages);
            savedImageDto.add(taskImages);

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
