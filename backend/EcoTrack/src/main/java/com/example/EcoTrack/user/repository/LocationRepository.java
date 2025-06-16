package com.example.EcoTrack.repository;

import com.example.EcoTrack.user.model.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<UserLocation,Long> {



}
