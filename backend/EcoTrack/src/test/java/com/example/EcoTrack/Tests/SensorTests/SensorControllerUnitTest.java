package com.example.EcoTrack.Tests.UserTests;


import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.security.customUserDetail.CustomUserDetailService;
import com.example.EcoTrack.sensors.controller.SensorController;

import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.shared.dto.SensorDTO;
import com.example.EcoTrack.task.repository.TaskRepository;
import com.example.EcoTrack.task.service.TaskService;
import com.example.EcoTrack.user.dto.UserDTO;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.repository.LocationRepository;
import com.example.EcoTrack.user.repository.UserOnlineStatusRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class SensorControllerUnitTest {

    @InjectMocks
    private UserLocationService service;

    @InjectMocks
    private SensorController controller;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SensorRepository sensorRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @Mock
    private UserOnlineStatusRepository userOnlineStatusRepository;

    @Mock
    private Sensor sensor;


    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;


    @Mock
    private UserService userService;


    @Mock
    private UserLocationService userLocationService;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private SensorService sensorService;

    @Mock
    private UserDTO userDTO;

    @Mock
    private Notification notification;


    @InjectMocks
    private AuthService authService;

    @InjectMocks
    private JwtService jwtService;




    @InjectMocks
    public NotificationService notificationService;
    @Mock
    User user;
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private Point point;
    @Mock
    private SimpMessagingTemplate messagingTemplate;


    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;
    @Mock
    private NotificationRepository notificationRepository;
    ;

    //daha sonrasında geri dönücem buna
    @Test
    void shouldgetInduvualSensorWithProtectionForWorker(){
        {
            Long mockSensorId = 101L;
            String username = "TestUser";

            Sensor testSensor = new Sensor();
            testSensor.setId(mockSensorId);
            testSensor.setSensorName("Test Sensor");

            User mockUser = new User();
            mockUser.setId(1L);
            mockUser.setFirstName(username);

            SensorFix userSession = new SensorFix();
            userSession.setSensor(testSensor);
            userSession.setUser(mockUser);
            mockUser.setSensorSessions(List.of(userSession));

            Authentication auth = mock(Authentication.class);
            when(auth.getName()).thenReturn(username);
            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(auth);
            SecurityContextHolder.setContext(securityContext);


            when(sensorRepository.findById(mockSensorId)).thenReturn(   Optional.of(testSensor));


            when(userService.findByUsername(anyString())).thenReturn(mockUser);

            when(sensorRepository.findById(mockSensorId)).thenReturn(Optional.of(testSensor));

            SensorDTO mockSensorDTO = new SensorDTO();
            mockSensorDTO.setId(15L);
            mockSensorDTO.setLongitude(25.0);
            mockSensorDTO.setLatitude(24.0);
            mockSensorDTO.setSensorName("mock sensor");
            mockSensorDTO.setColor_code(null);
            mockSensorDTO.setCurrentSensorSession(null);
            ResponseEntity<ApiResponse> result = controller.getInduvualSensorWithProtectionForWorker(mockSensorId);
          ResponseEntity<PastNonTaskSensorApiResponse> expectedResponse =  ResponseEntity.status(HttpStatus.ACCEPTED).body(new PastNonTaskSensorApiResponse(true,"Successfully got sensor",mockSensorDTO   ,null,200));

            assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(expectedResponse.getStatusCode(), result.getBody().getStatus());
            assertEquals(expectedResponse.getBody(), result.getBody().getMessage());
            verify(sensorService).getInduvualSensor(mockSensorId);
        }
    }

}
