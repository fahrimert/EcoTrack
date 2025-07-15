package com.example.EcoTrack.announecements.repository;

import com.example.EcoTrack.announecements.model.Announcement;
import com.example.EcoTrack.notification.model.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface AnnounecementsRepository extends JpaRepository<Announcement,Long> {
}
