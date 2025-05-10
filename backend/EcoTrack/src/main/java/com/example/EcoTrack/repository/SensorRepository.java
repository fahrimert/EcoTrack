package com.example.EcoTrack.repository;

import com.example.EcoTrack.dto.SensorDTO;
import com.example.EcoTrack.model.Sensor;
import com.example.EcoTrack.model.SensorSessionImages;
import com.example.EcoTrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  SensorRepository extends JpaRepository<Sensor,Long> {
   Optional<Sensor> findById(Long id);



//   List<Sensor> findByCurrentSensorSessionAndCompletedTimeIsNull;
}
