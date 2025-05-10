package com.example.EcoTrack.service;

import com.example.EcoTrack.dto.UserAndSessionSensor;
import com.example.EcoTrack.dto.UserLocationDTO;
import com.example.EcoTrack.model.SensorFix;
import com.example.EcoTrack.model.User;
import com.example.EcoTrack.model.UserLocation;
import com.example.EcoTrack.repository.LocationRepository;
import com.example.EcoTrack.repository.SensorSessionRepository;
import com.example.EcoTrack.repository.UserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserLocationService {
    private UserRepository userRepository;
    private LocationRepository locationRepository;
    private SensorSessionRepository sensorSessionRepository;
    private SimpMessagingTemplate messagingTemplate;
    public UserLocationService(UserRepository userRepository, LocationRepository locationRepository, SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public   String  saveUserLocation(String username , Double lat , Double longtitude){
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

// Enlem (latitude) ve boylam (longitude)
        double latitude = lat;
        double longitude = longtitude;

        Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        UserLocation userLocation = new UserLocation();

        userLocation.setLocation(location);
        Date now = new Date();

        userLocation.setCreatedAt(now);
        User user = userRepository.findByFirstName(username);
        userLocation.setUser(user);
        user.setUserLocation(userLocation);


        UserLocation userLocation1 = locationRepository.save(userLocation);

        UserLocationDTO userLocationn = getLocation(username);
        messagingTemplate.convertAndSend("/topic/locations", userLocationn);

        return  "User location details saved successfully" ;

    }

    public UserLocationDTO getLocation(String username) {
        User user = userRepository.findByFirstName(username);

        Point point =   user.getUserLocation().getLocation();

        return new UserLocationDTO(user.getId(),point.getY(), point.getX());
    }

    public   List<UserAndSessionSensor>  getAllLocation() {
        List<User> usersLocationList = userRepository.findAll();

        return  usersLocationList.stream()
                .map(user -> {//böyle yapılabilir kalsın bi
                    Point point = user.getUserLocation().getLocation();
                    return new UserAndSessionSensor(
                            user.getId(),
                            user.getFirstName(),
                            point.getY(),
                            point.getX(),
                            user.getSensorSessions().stream()
                                    .filter(a -> a.getCompletedTime() != null)
                                    .findFirst()
                                    .orElse(null)
                                    .getSensor().getSensorLocation().getLocation().getY(),
                    user.getSensorSessions().stream()
                            .filter(a -> a.getCompletedTime() != null)
                            .findFirst()
                            .orElse(null)
                            .getSensor().getSensorLocation().getLocation().getX(),
                    user.getSensorSessions().stream()
                            .filter(a -> a.getCompletedTime() != null)
                            .findFirst()
                            .orElse(null)
                            .getSensor());

                })
                .collect(Collectors.toList());

    }
}
