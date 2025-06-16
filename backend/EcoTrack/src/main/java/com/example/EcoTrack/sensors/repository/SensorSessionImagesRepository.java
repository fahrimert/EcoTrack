package com.example.EcoTrack.sensors.repository;

import com.example.EcoTrack.sensors.model.SensorSessionImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorSessionImagesRepository extends JpaRepository<SensorSessionImages,Long> {


    Optional<SensorSessionImages> findByName(String name);

    List<SensorSessionImages> findBySensorSessionsId(Long sessionId);
}
