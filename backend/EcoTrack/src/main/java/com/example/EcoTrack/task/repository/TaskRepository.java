package com.example.EcoTrack.task.repository;

import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    Task findByAssignedTo(User user);
}
