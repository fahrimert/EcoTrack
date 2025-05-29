package com.example.EcoTrack.repository;

import com.example.EcoTrack.model.SensorSessionImages;
import com.example.EcoTrack.model.TaskImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskImagesRepository extends JpaRepository<TaskImages,Long> {
}
