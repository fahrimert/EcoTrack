package com.example.EcoTrack.user.service;

import com.example.EcoTrack.sensors.dto.ekipTakibiDtos.CrewJobsUserAndSessionSensorDTO;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.user.dto.UserAndSessionSensorDTO;
import com.example.EcoTrack.user.dto.UserLocationDTO;
import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.model.UserLocation;
import com.example.EcoTrack.user.repository.LocationRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserLocationService {
    private UserRepository userRepository;
    private LocationRepository locationRepository;
    private SimpMessagingTemplate messagingTemplate;
    public UserLocationService(UserRepository userRepository, LocationRepository locationRepository, SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.messagingTemplate = messagingTemplate;
    }

        public   String     saveUserLocation(String username , Double lat , Double longtitude){
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        double latitude = lat;
        double longitude = longtitude;

        Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        UserLocation userLocation = new UserLocation();

        userLocation.setLocation(location);
        Date now = new Date();



            userLocation.setCreatedAt(now);
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            userLocation.setUser(user);
            user.setUserLocation(userLocation);


        UserLocation userLocation1 = locationRepository.save(userLocation);

        UserLocationDTO userLocationn = getLocation(username);
//        messagingTemplate.convertAndSend("/topic/locations", userLocationn);

        return  "User location details saved successfully" ;

    }

    public UserLocationDTO getLocation(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı: " + username));

        UserLocation userLocation = user.getUserLocation();

        if (userLocation == null || userLocation.getLocation() == null) {
            throw new EntityNotFoundException("Kullanıcıya ait lokasyon bilgisi tanımlanmamış: " + username);
        }

        Point point = userLocation.getLocation();
        return new UserLocationDTO(user.getId(),point.getY(), point.getX());
    }

    //Get all workers session if they has and their own location for worker ekiptakibi page
    public   List<CrewJobsUserAndSessionSensorDTO> getAllWorkersSessionSensorAndTheirLocation() {
        List<User> activeWorkers = userRepository.findWorkersWithActiveSessions();
        return activeWorkers.stream()
                .map(user -> {
                    if (user.getUserLocation() == null || user.getUserLocation().getLocation() == null) return null;

                    Optional<SensorFix> activeSession = user.getSensorSessions().stream()
                            .filter(s -> s.getCompletedTime() == null)
                            .findFirst();

                    if (activeSession.isEmpty()) return null;

                    SensorFix session = activeSession.get();
                    Sensor sensor = session.getSensor();
                    Point workerLoc = user.getUserLocation().getLocation();
                    Point sensorLoc = sensor.getSensorLocation().getLocation();

                    return CrewJobsUserAndSessionSensorDTO.builder()
                            .workerId(user.getId())
                            .workerName(user.getFirstName() + " " + user.getSurName())
                            .workerLatitude(workerLoc.getY())
                            .workerLongitude(workerLoc.getX())
                            .isOnline(user.getUserOnlineStatus().getIsOnline())
                            .sensorId(sensor.getId())
                            .sensorName(sensor.getSensorName())
                            .sensorStatus(sensor.getStatus().name())
                            .sensorLatitude(sensorLoc.getY())
                            .sensorLongitude(sensorLoc.getX())
                            .sessionId(session.getId())
                            .startTime(session.getStartTime().toString())
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
