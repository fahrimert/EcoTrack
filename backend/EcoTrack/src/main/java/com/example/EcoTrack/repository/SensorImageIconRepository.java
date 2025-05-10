package com.example.EcoTrack.repository;

import com.example.EcoTrack.model.SensorIconImage;
import com.example.EcoTrack.model.SensorSessionImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorImageIconRepository extends JpaRepository<SensorIconImage,Long> {

    SensorIconImage findBySensorId(Long id);
}
