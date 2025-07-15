package com.example.EcoTrack.Tests.NotificationTests;

import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.manager.service.ManagerService;
import com.example.EcoTrack.notification.dto.ManagerNotificationDTO;
import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.notification.type.NotificationType;
import com.example.EcoTrack.pdfReports.model.PdfReports;
import com.example.EcoTrack.pdfReports.repository.PdfRepository;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.sensors.repository.SensorImageIconRepository;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionImagesRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.sensors.service.SensorSessionImageService;
import com.example.EcoTrack.task.repository.TaskRepository;
import com.example.EcoTrack.user.dto.UserDTO;
import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.repository.LocationRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@Slf4j
@ExtendWith(MockitoExtension.class)
@Transactional
public class NotificationUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private UserLocationService userLocationService;


    @InjectMocks
    private SensorService sensorService;

    @Mock
    GeometryFactory geometryFactory;
    @InjectMocks
    private UserDTO userDTO;

    @InjectMocks
    private SensorFix sensorFix;

    @InjectMocks
    NotificationService notificationService;

    @InjectMocks
    private AuthService authService;

    @Mock
    private SensorSessionImageService sensorSessionImageService;

    @Mock
    private LocationRepository userLocationRepository;

    @Mock
    private SensorSessionRepository sensorSessionRepository;

    @Mock
    private SensorImageIconRepository sensorImageIconRepository;

    @Mock
    private SensorSessionImagesRepository sensorSessionImagesRepository;

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PdfRepository pdfRepository;

    @InjectMocks
    private ManagerService managerService;

    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @Mock
    private Authentication authentication;


    @Test
    @WithMockUser(username = "testUser")
    void shouldGetNotificationsOfManagerById(){
        ManagerNotificationDTO managerNotificationDTO = new ManagerNotificationDTO();

        PdfReports pdfReports = new PdfReports();
        String mockSupervizorUsername = "Supervizor User";
        String mockManagerUsername = "Manager User";

        User mockSupervizor = new User();
        mockSupervizor.setId(20L);
        mockSupervizor.setFirstName(mockSupervizorUsername);
        mockSupervizor.setSurName("supervizorsurnamemock");
        mockSupervizor.setRole(Role.SUPERVISOR);


        User mockManager = new User();
        mockManager.setId(102L);
        mockManager.setFirstName(mockManagerUsername);
        mockManager.setSurName("managersurnamemock");
        mockManager.setRole(Role.MANAGER);

        pdfReports.setManager(mockManager);
        pdfReports.setSupervisor(mockSupervizor);


        Notification notification = new Notification();
        notification.setId(10L);
        notification.setType(NotificationType.OTHER);
        notification.setIsRead(false);
        notification.setSenderId(pdfReports.getSupervisor().getId());
        notification.setReceiverId(pdfReports.getManager().getId());

        managerNotificationDTO.setId(mockManager.getId());
        managerNotificationDTO.setIsread(false);
        managerNotificationDTO.setSenderName(pdfReports.getSupervisor().getFirstName());
        managerNotificationDTO.setSensorName("mocksensor");
        mockManager.setNotifications(List.of(notification));


        when(notificationRepository.findByReceiverId(mockManager.getId())).thenReturn(List.of(notification));
        when(pdfRepository.findById(pdfReports.getId())).thenReturn(Optional.of(pdfReports));
        ResponseEntity<?> response = notificationService.getNotificationByManagerId(mockManager.getId());

        ArrayList<ManagerNotificationDTO> managerNotificationDTOO = (ArrayList<ManagerNotificationDTO>) response.getBody();
        System.out.println(managerNotificationDTOO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(managerNotificationDTOO.getFirst().getId(),((ArrayList<ManagerNotificationDTO>) response.getBody()).getFirst().getId());
        assertEquals(managerNotificationDTOO.getFirst().getSensorName(),(((ArrayList<ManagerNotificationDTO>) response.getBody()).getFirst().getSensorName()));

//        assertEquals(response.getBody().);

    }




 }
