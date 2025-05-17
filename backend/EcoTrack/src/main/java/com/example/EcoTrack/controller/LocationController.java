package com.example.EcoTrack.controller;


import com.example.EcoTrack.dto.UserAndSessionSensorDTO;
import com.example.EcoTrack.dto.UserLocationDTO;
import com.example.EcoTrack.service.UserLocationService;
import jakarta.transaction.Transactional;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Tüm origin'lere izin ver

public class LocationController {

    private UserLocationService userLocationService;
    public LocationController(UserLocationService userLocationService) {

        this.userLocationService = userLocationService;
    }

    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @PostMapping("/saveUserLocation")
    @SendTo("topic/locations")

    public  String saveLocation ( @RequestParam Double lat, @RequestParam Double longtitude) {

        Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
        String username = securityContextHolder.getName();

            return  userLocationService.saveUserLocation(username,lat,longtitude);

    }

    @GetMapping("/getUserLocation")
    @Transactional

    public UserLocationDTO getUserLocation(){
        Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
        String username = securityContextHolder.getName();

        return  userLocationService.getLocation(username);


    }
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @GetMapping("/getAllUserLocation")
    @Transactional

    public  List<UserAndSessionSensorDTO> getAllUserLocation(){
        return  userLocationService.getAllLocation();
    }



}
