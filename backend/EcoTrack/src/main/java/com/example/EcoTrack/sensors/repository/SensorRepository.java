package com.example.EcoTrack.sensors.repository;

import com.example.EcoTrack.pdfReports.model.PdfReports;
import com.example.EcoTrack.sensors.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  SensorRepository extends JpaRepository<Sensor,Long> {
   Optional<Sensor> findById(Long id);

    @Query(value = "SELECT * FROM sensors WHERE sensor_name = :sensorName", nativeQuery = true)
  Sensor findBySensorName(@Param("sensorName") String sensorName);
   @Query(value = "SELECT * FROM sensors WHERE status != 'IN_REPAIR' ",nativeQuery = true)
   List<Sensor> findAllAvailable();

    boolean existsBySensorName(String sensorName);

//   List<Sensor> findByCurrentSensorSessionAndCompletedTimeIsNull;
}
