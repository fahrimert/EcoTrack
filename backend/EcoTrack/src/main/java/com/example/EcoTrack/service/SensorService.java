package com.example.EcoTrack.service;

import com.example.EcoTrack.dto.SensorDTO;
import com.example.EcoTrack.dto.SensorDTO;
import com.example.EcoTrack.dto.UserLocationDTO;
import com.example.EcoTrack.model.Sensor;
import com.example.EcoTrack.model.User;
import com.example.EcoTrack.repository.SensorRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorService {
        private SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<SensorDTO> getAllSensor() {

        List<Sensor> sensor = sensorRepository.findAll();
       List<SensorDTO> sensorlistDTO  = sensorRepository.findAll().stream().map(a -> new SensorDTO(a.getSensorName(), a.getStatus(),a.getSensorLocation().getLocation().getX(),a.getSensorLocation().getLocation().getY())).collect(Collectors.toList());
            return  sensorlistDTO;
   }
}
