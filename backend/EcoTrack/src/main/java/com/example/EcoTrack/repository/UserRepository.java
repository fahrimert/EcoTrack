package com.example.EcoTrack.repository;

import com.example.EcoTrack.model.Role;
import com.example.EcoTrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByFirstName(String firstName);


    void deleteTokenById(Long id);

    List<User> findAllByRole(Role workerRole);
}
