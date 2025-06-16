package com.example.EcoTrack.sensors.controller;

import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.shared.dto.ImageResponseDTO;
import com.example.EcoTrack.shared.dto.SensorDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")

public class SensorController {
    private final SensorService sensorService;
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    //Get all sensors endpoint
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

    @GetMapping("/sensors/location/{sensorId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public ResponseEntity<ApiResponse> getInduvualSensorLocation(@PathVariable Long sensorId){
        return  sensorService.getInduvualSensorLocation(sensorId);
    }



    @GetMapping("/sensors/{sensorId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public ResponseEntity<ApiResponse> getInduvualSensorWithProtectionForWorker(@PathVariable Long sensorId){
        return  sensorService.getInduvualSensor(sensorId);
    }



    @GetMapping("/imagess/{sessionId}")
    @Transactional
    public List<ImageResponseDTO> getImageBySessionId(@PathVariable("sessionId") Long sessionId){
        return  sensorService.getImagesBySessionId(sessionId);
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

    @PostMapping("/sensor/{sensorId}/update-icon-image")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional

    public void updateIconImage( @PathVariable Long sensorId,@RequestParam("image")  MultipartFile file) throws IOException {
        sensorService.updateIconImage(file,sensorId);
    }








}

