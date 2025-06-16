package com.example.EcoTrack.task.service;


import com.example.EcoTrack.task.repository.TaskImagesRepository;
import com.example.EcoTrack.task.repository.TaskRepository;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.task.model.TaskImages;
import com.example.EcoTrack.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskImageService {
    private  final TaskRepository taskRepository;
    private  final TaskImagesRepository taskImagesRepository;



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




}
