package com.example.EcoTrack.sensors.repository;

import com.example.EcoTrack.sensors.model.SensorIconImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorImageIconRepository extends JpaRepository<SensorIconImage,Long> {

    SensorIconImage findBySensorId(Long id);
}
