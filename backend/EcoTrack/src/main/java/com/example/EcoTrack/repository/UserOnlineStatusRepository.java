package com.example.EcoTrack.repository;

import com.example.EcoTrack.model.User;
import com.example.EcoTrack.model.UserLocation;
import com.example.EcoTrack.model.UserOnlineStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserOnlineStatusRepository extends JpaRepository<UserOnlineStatus,Long> {


    Optional<UserOnlineStatus> findByUser(User user);

    List<UserOnlineStatus> findByIsOnlineTrue();
}
