package com.example.EcoTrack.repository;

import com.example.EcoTrack.dto.SensorDTO;
import com.example.EcoTrack.model.Sensor;
import com.example.EcoTrack.model.SensorFix;
import com.example.EcoTrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SensorSessionRepository extends JpaRepository<SensorFix,Long> {
   Optional<SensorFix> findById(Long id);

   List<SensorFix> findByUser(User user);

   Optional<SensorFix> findByUserAndCompletedTimeIsNull(User user);

   List<SensorFix> findByCompletedTimeIsNull();


//usera göre direk tüm sensor sessionları bulsam null olmayan onun üzerinden
   List<SensorFix> findAllByUserAndCompletedTimeIsNotNull(User user);

   @Query(value = "SELECT * FROM sensor_session WHERE user_id = :userId AND start_time >= now() - interval '1 month'  AND completed_time IS NOT NULL ", nativeQuery = true)
   List<SensorFix> findLastMonthDataByUserId(@Param("userId") Long userId, @Param("oneMonthAgo") LocalDateTime oneMonthAgo);



   @Query(value = "SELECT * FROM sensor_session WHERE user_id = :userId  AND completed_time IS NOT NULL ", nativeQuery = true)
   List<SensorFix> findDataByUserid(@Param("userId") Long userId);

//bulmam gereken şey userin sessionlarındaki sensorlerin locationlarına göre heatmap yani sensorsessionlarını usera göre bulup
   //sonrasında bunların locationlarını alsam yeter

   @Query(value = """
        SELECT sf.* 
        FROM sensor_session sf
        JOIN sensors s ON sf.sensor_id = s.id
        WHERE s.status = 'FAULTY'
        """, nativeQuery = true)
   List<SensorFix> findAllSensorFixesWithFaultySensors();

}
