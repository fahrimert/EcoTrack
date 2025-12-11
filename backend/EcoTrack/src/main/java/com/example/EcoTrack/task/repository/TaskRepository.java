package com.example.EcoTrack.task.repository;

import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    @
            Query("SELECT t FROM Task t " +
            "LEFT JOIN FETCH t.sensor s " +
            "LEFT JOIN FETCH s.sensorLocation sl " +
            "LEFT JOIN FETCH s.currentSensorSession ss " +
            "LEFT JOIN FETCH t.assignedBy u " +
            "WHERE t.assignedTo.id = :userId " +
            "AND t.taskCompletedTime IS NULL " +
            "ORDER BY t.superVizorDeadline ASC")
    List<Task> findIncompleteTasksByWorkerId(@Param("userId") Long userId);


    Task findByAssignedTo(User user);
}
