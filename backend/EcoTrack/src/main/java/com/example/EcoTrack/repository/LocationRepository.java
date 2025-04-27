package com.example.EcoTrack.repository;

import com.example.EcoTrack.model.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LocationRepository extends JpaRepository<UserLocation,Long> {

    @Query(value = "SELECT ST_asText(location) FROM user_location WHERE id=202 ", nativeQuery = true)
    UserLocation getUserLocation(Long id);

}
