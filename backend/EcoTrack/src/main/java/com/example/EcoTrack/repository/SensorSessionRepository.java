package com.example.EcoTrack.repository;

import com.example.EcoTrack.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface    SensorRepository extends JpaRepository<Sensor,Long> {
   Optional<Sensor> findById(Long id);
}
