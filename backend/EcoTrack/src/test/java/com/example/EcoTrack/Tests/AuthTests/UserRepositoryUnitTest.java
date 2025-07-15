package com.example.EcoTrack.Tests.AuthTests;

import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryUnitTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @TestConfiguration
    static class BCryptPasswordEncoderConfig {
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
    @Transactional
    @Test

    void findByUsername_ShouldReturnUser(){
        User mockUser = new User();
        mockUser.setEmail("test@mail.com");
        mockUser.setFirstName("Test User");
        mockUser.setPassword("encodedPass");
            userRepository.save(mockUser);

            User foundUser = userRepository.findByFirstName("Test User");

            assertEquals("Test User", foundUser.getFirstName());
        };
    @Transactional
    @Test

    void   findByUsername_ShouldReturnNullWhenUserDoesntExist() {

        User foundUser = userRepository.findByFirstName("Non Existent User");

        assertThat(foundUser).isNull();
    }

    @Transactional
    @Test
        void  save_ShouldUpdateLastLoginTime(){
        User mockUser = new User();
        mockUser.setEmail("test@mail.com");
        mockUser.setFirstName("Test User");
        mockUser.setPassword("encodedPass");
        mockUser.setIsActive(true);
        mockUser.setPassword(bCryptPasswordEncoder.encode("12345"));

        userRepository.save(mockUser);

        mockUser.setLastLoginTime(new Date());
        userRepository.save(mockUser);

        User updated = userRepository.findByFirstName("Test User");
        assertThat(updated.getLastLoginTime()).isNotNull();
    }
}
