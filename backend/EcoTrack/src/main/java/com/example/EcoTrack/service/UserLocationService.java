package com.example.EcoTrack.service;

import com.example.EcoTrack.dto.UserLocationDTO;
import com.example.EcoTrack.model.User;
import com.example.EcoTrack.model.UserLocation;
import com.example.EcoTrack.repository.LocationRepository;
import com.example.EcoTrack.repository.UserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserLocationService {
    private UserRepository userRepository;
    private LocationRepository locationRepository;

    public UserLocationService(UserRepository userRepository, LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
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
        return  "User location details saved successfully" ;

    }

    public UserLocationDTO getLocation(String username) {
        User user = userRepository.findByFirstName(username);

        Point point =   user.getUserLocation().getLocation();

        return new UserLocationDTO(point.getY(), point.getX());
    }
}
