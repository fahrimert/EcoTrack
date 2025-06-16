package com.example.EcoTrack.repository;

import com.example.EcoTrack.notification.model.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByReceiverId(Long receiverId);

    List<Notification> findByReceiverIdAndIsReadFalse(Long userId);

    @Transactional
    int deleteBytaskId(Long taskId);
}
