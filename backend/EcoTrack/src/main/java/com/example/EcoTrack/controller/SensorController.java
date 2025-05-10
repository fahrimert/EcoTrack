package com.example.EcoTrack.controller;

import com.example.EcoTrack.dto.*;
import com.example.EcoTrack.model.SensorFix;
import com.example.EcoTrack.model.SensorSessionImages;
import com.example.EcoTrack.model.SensorStatus;
import com.example.EcoTrack.repository.SensorSessionImagesRepository;
import com.example.EcoTrack.service.SensorService;
import com.example.EcoTrack.service.SensorSessionImageService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")

public class SensorController {
    private final SensorService sensorService;
    private  SensorSessionImagesRepository sensorSessionImagesRepository;
    private  ImageResponseDTO imageResponseDTO;
    public SensorController(SensorService sensorService, SensorSessionImagesRepository sensorSessionImagesRepository) {
        this.sensorService = sensorService;
        this.sensorSessionImagesRepository = sensorSessionImagesRepository;
    }


    @GetMapping("/sensors")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public List<SensorDTO> getSensors(
    ){
        return  sensorService.getAllSensor();
    }

    @GetMapping("/past-sensors")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public List<SensorFix> getPastSensors(
    ){
        return  sensorService.getPastSensorsOfUser();
    }

    @GetMapping("/sensors/{sensorId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    public ResponseEntity<ApiResponse> getInduvualSensor(@PathVariable Long sensorId){
        return  sensorService.getInduvualSensor(sensorId);
    }


    @GetMapping("/sensors/getPastSensorDetail/{sensorId}")
    @Transactional
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    public ResponseEntity<ApiResponse> getPastSensorDetail(@PathVariable Long sensorId){


        return  sensorService.getSensorDetail(sensorId);
    }


//    @GetMapping("/images/{sessionId}")
//    public ResponseEntity<List<ImageMetadata>> getImagesMetadata(@PathVariable Long sessionId) {
//        List<SensorSessionImages> images = sensorSessionImagesRepository.findBySensorSessionsId(sessionId);
//
//        List<ImageMetadata> response = images.stream()
//                .map(image -> new ImageMetadata(
//                        image.getId(),
//                        image.getFileName(),
//                        image.getFileType(),
//                        image.getDownloadUrl()
//                ))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/imagess/{sessionId}")
    @Transactional
    public List<ImageResponseDTO> getImageBySessionId(@PathVariable("sessionId") Long sessionId){
        return  sensorService.getImagesBySessionId(sessionId);
    }

//    @PostMapping("/imagess")
//    public ResponseEntity<?> uploadImage(@RequestParam("image") List<MultipartFile> file) throws IOException {
//        List<SensorSessionImages> response = sensorSessionImageService.uploadImage(file);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(response);
//    }



    @GetMapping("/sensors/getSensorStatuses")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public List<SensorStatus> getAllSensorStatuses(){

        return SensorStatus.getAll();

    }

    @MessageMapping("/repair")
    @SendTo("topic/repair")
    @PutMapping("/sensor/repair/{sensorId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
        public ResponseEntity<String> updateSensorStatusToRepair(@PathVariable Long sensorId){
     return sensorService.updateInRepairTheSensor(sensorId);
        }


    @PutMapping("/sensor/AllState/{sensorId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public void updateSensorToFinal(@RequestParam String note,@RequestParam SensorStatus statusID, @PathVariable Long sensorId,@RequestParam List<MultipartFile> files){
             sensorService.updateFinalState(note,statusID,sensorId,files);
    }

    @PostMapping("/sensor/{sensorId}/create-icon-image")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional

    public void createIconImage( @PathVariable Long sensorId,@RequestParam("image")  MultipartFile file) throws IOException {
        sensorService.uploadIconImage(file,sensorId);
    }


}
