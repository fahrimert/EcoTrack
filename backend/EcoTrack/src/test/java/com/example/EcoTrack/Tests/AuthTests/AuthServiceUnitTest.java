package com.example.EcoTrack.Tests.AuthTests;

import com.example.EcoTrack.auth.dto.UserRequestDTO;
import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@Transactional
public class AuthServiceUnitTest {


        @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;



    @Test
    void  login_shouldReturn403_whenUserIsNotActive(){
        User mockUser = new User();
        mockUser.setIsActive(false);
        mockUser.setFirstName("test");

        when(userRepository.findByFirstName("test")).thenReturn(mockUser);

        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setFirstName("test");

        ResponseEntity<ApiResponse<?>> response = authService.login(requestDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User Deactivated", response.getBody().getMessage());
    }



}
