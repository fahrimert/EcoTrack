package com.example.EcoTrack.task.repository;

import com.example.EcoTrack.task.model.TaskImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskImagesRepository extends JpaRepository<TaskImages,Long> {
}
