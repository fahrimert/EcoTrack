package com.example.EcoTrack.sensors.repository;

import com.example.EcoTrack.shared.dto.SensorWithUserProjection;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SensorSessionRepository extends JpaRepository<SensorFix,Long> {
   Optional<SensorFix> findById(Long id);
   Optional<SensorFix> findByUserAndCompletedTimeIsNull(User user);


   @Query(value = """
           SELECT
               s.id AS sensorsid,
               sf.id AS sensorsessionsid,
               s.sensor_name AS sensorName,
               sf.note,
               CASE sf.final_status
              WHEN 0 THEN 'ACTIVE'
              WHEN 1 THEN 'FAULTY'
              WHEN 2 THEN 'IN_REPAIR'
              WHEN 3 THEN 'SOLVED'
          END as finalStatus,
               sf.start_time,
               sf.completed_time,
               u.id AS userid,
               u.first_name AS firstName,
               u.sur_name AS surName,
               u.role
           FROM
               sensor_session sf
           JOIN sensors s ON sf.sensor_id = s.id
           JOIN users u ON sf.user_id = u.id
           WHERE
               u.role = :role
               AND sf.completed_time IS NOT NULL;
    """,
           nativeQuery = true)
   List<SensorWithUserProjection>  findCompletedSensorsWithUserDetails(@Param("role") String role);

   List<SensorFix> findAllByUserAndCompletedTimeIsNotNull(User user);



   @Query(value = "SELECT * FROM sensor_session WHERE user_id = :userId AND start_time >= now() - interval '1 month'  AND completed_time IS NOT NULL ", nativeQuery = true)
   List<SensorFix> findLastMonthDataByUserId(@Param("userId") Long userId, @Param("oneMonthAgo") LocalDateTime oneMonthAgo);


   @Query(value = """
           SELECT sf.*
           FROM sensor_session sf
           WHERE sf.final_status = '2'
        """, nativeQuery = true)
   List<SensorFix> findAllSensorFixesWithFaultySensors();

}
