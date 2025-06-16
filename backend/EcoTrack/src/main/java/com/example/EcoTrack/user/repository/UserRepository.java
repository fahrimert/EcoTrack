package com.example.EcoTrack.user.repository;

import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByFirstName(String firstName);


    List<User> findAllByRole(Role workerRole);

    @Query(value = """
             select * from users where ROLE != 'MANAGER';
            """, nativeQuery = true)
    List<User> findAllExceptManager();





}
