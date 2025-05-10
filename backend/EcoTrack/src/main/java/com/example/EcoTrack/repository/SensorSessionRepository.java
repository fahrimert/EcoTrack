package com.example.EcoTrack.repository;

import com.example.EcoTrack.dto.SensorDTO;
import com.example.EcoTrack.model.Sensor;
import com.example.EcoTrack.model.SensorFix;
import com.example.EcoTrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SensorSessionRepository extends JpaRepository<SensorFix,Long> {
   Optional<SensorFix> findById(Long id);

   List<SensorFix> findByUser(User user);

   Optional<SensorFix> findByUserAndCompletedTimeIsNull(User user);

   List<SensorFix> findByCompletedTimeIsNull();

   List<SensorFix> findAllByUserAndCompletedTimeIsNotNull(User user);
}
