package com.example.EcoTrack.repository;

import com.example.EcoTrack.dto.SensorFixDTO;
import com.example.EcoTrack.model.Role;
import com.example.EcoTrack.model.SensorFix;
import com.example.EcoTrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByFirstName(String firstName);

    void deleteTokenById(Long id);

    List<User> findAllByRole(Role workerRole);

    @Query(value = """
             select * from users where ROLE != 'MANAGER';
            """, nativeQuery = true)
    List<User> findAllExceptManager();

    @Query(value = """
    SELECT u.first_name, COUNT(t.id)
    FROM users u
    LEFT JOIN tasks t ON t.assigned_by_user_id = u.id AND t.worker_arrived IS NOT NULL
    WHERE u.role = 'SUPERVISOR'
    GROUP BY u.first_name
    """, nativeQuery = true)
    List<Object[]> countWorkerArrivedTasksByAllSupervisors();



}
