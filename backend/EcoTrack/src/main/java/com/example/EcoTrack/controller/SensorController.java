package com.example.EcoTrack.controller;

import com.example.EcoTrack.dto.SensorDTO;
import com.example.EcoTrack.dto.UserLocationDTO;
import com.example.EcoTrack.model.Sensor;
import com.example.EcoTrack.repository.SensorRepository;
import com.example.EcoTrack.service.SensorService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")

public class SensorController {
    private SensorRepository sensorRepository;
    private SensorService sensorService;

    public SensorController(SensorRepository sensorRepository, SensorService sensorService) {
        this.sensorRepository = sensorRepository;
        this.sensorService = sensorService;
    }


    @GetMapping("/sensors")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public List<SensorDTO> getSensors(){
        return  sensorService.getAllSensor();


    }
}
