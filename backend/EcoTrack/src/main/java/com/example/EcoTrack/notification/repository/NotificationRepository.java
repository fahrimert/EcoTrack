package com.example.EcoTrack.notification.repository;

import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.sensors.model.Sensor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByReceiverId(Long receiverId);

    @Transactional
    List<Notification> findByReceiverIdAndIsReadFalse(Long userId);

    @Transactional
    int deleteBytaskId(Long taskId);


    //this query for ıntegration test class use purposes
    @Query(value =
            """ 
    SELECT * FROM notification WHERE receiver_id = :receiverId and id= :id
    """, nativeQuery = true)
    Notification findByReceiverIdAndId(@Param("receiverId") Long receiverId , @Param("id") Long id);
}
