package com.example.EcoTrack.Tests.UserTests;

import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.notification.dto.NotificationDTO;
import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.notification.type.NotificationType;
import  com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.security.customUserDetail.CustomUserDetailService;
import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.shared.dto.SensorDTO;
import com.example.EcoTrack.task.dto.SensorAllAndTaskDTO;
import com.example.EcoTrack.task.dto.UserTaskDTO;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.task.repository.TaskRepository;
import com.example.EcoTrack.task.service.TaskService;
import com.example.EcoTrack.user.controller.UserController;
import com.example.EcoTrack.user.dto.UserDTO;
import com.example.EcoTrack.user.dto.UserLocationDTO;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO;
import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.model.UserLocation;
import com.example.EcoTrack.user.model.UserOnlineStatus;
import com.example.EcoTrack.user.repository.UserOnlineStatusRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) //bunu yapınca authentication filtreleri gidiyor bearer tokena vesaire gerek kalmıyor

public class UserControllerIntegrationTest {
    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @MockitoBean
    private UserOnlineStatusRepository userOnlineStatusRepository;

    @Mock
    private Sensor sensor;


    @MockitoBean
    private SimpMessagingTemplate simpMessagingTemplate;


    @MockitoBean
    private UserService userService;


    @MockitoBean
    private UserLocationService userLocationService;

    @MockitoBean // Eksik olan mock'u ekleyin
    private TaskService taskService;

    @MockitoBean
    private SensorService sensorService;

    @InjectMocks
    private UserDTO userDTO;


    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    public NotificationService notificationService;
    @Mock
    private TaskRepository taskRepository;

    @MockitoBean
    private  Authentication securityContextHolder;

    @Mock
    private NotificationRepository notificationRepository;
;

    //    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup().build();
//    }
//    @BeforeEach
//    void setup() {
//        when(jwtService.verify(anyString())).thenReturn(true);
//    }
    @Test
    void get_detailOfLoggedInUser_ShouldReturnUserDTO() throws Exception {

        String mockToken = "test1234";


        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getSubject()).thenReturn("mockuser");


        User mockUser = new User();
        mockUser.setId(101L);
        mockUser.setFirstName("Mock User");
        mockUser.setSurName("Mock Surname");
        mockUser.setRole(Role.WORKER);
        mockUser.setSensorSessions(new ArrayList<>()); // boş liste ver

        when(userService.findByUsername("mockuser")).thenReturn(mockUser);
        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setFirstName("Mock User");
        mockUserDTO.setSurName("Mock Surname");
        mockUserDTO.setRole(Role.WORKER);
        mockUserDTO.setId(101L);
        mockUserDTO.setSensorSessions(new ArrayList<>());


        when(jwtService.extractTokenFromHeader(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.extractAllClaims(mockToken)).thenReturn(mockClaims);

        when(userService.getTheDetailOfALoggedInUser(mockToken)).thenReturn(mockUserDTO);

        mockMvc.perform(get("/user/me")
                        .with(csrf())
                        .header("Authorization", "Bearer " + mockToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.firstName").value("Mock User"));
        ;


    }

    @Test
    void shouldReturnNotificationsByUserId() throws Exception {
        Long mockUserId = 1L;
        NotificationDTO notification1 = new NotificationDTO();
        notification1.setId(15L);
        notification1.setReceiverId(mockUserId);
        notification1.setNotificationType(NotificationType.TASK);

        NotificationDTO notification2 = new NotificationDTO();
        notification2.setId(16L);
        notification2.setReceiverId(mockUserId);
        notification2.setNotificationType(NotificationType.OTHER);


        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        notificationDTOList.add(notification1);
        notificationDTOList.add(notification2);

        when(userService.getNotificationById(mockUserId))
                .thenReturn(ResponseEntity.ok(notificationDTOList));

        mockMvc.perform(get("/user/getNotifications/{userId}", mockUserId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
    @Test
    void shouldReturnWorkersByUserId () throws Exception {

        User user1 = new User();
        UserOnlineStatus userOnlineStatus1 = new UserOnlineStatus();
        UserOnlineStatusDTO userOnlineStatusDTO1 = new UserOnlineStatusDTO();

        user1.setId(15L);
        user1.setFirstName("mockuser1");
        user1.setSurName("mocksurname1");
        user1.setRole(Role.WORKER);
        userOnlineStatus1.setId(15L);
        userOnlineStatus1.setUser(user1);
        userOnlineStatus1.setIsOnline(true);
        userOnlineStatus1.setCreatedAt(new Date());


        userOnlineStatusDTO1.setId(15L);
        userOnlineStatusDTO1.setFirstName(user1.getFirstName());
        userOnlineStatusDTO1.setSurName(user1.getSurName());
        userOnlineStatusDTO1.setUserOnlineStatus(userOnlineStatus1);
        userOnlineStatusDTO1.setRole(user1.getRole());


        User user2 = new User();
        UserOnlineStatus userOnlineStatus2 = new UserOnlineStatus();
        UserOnlineStatusDTO userOnlineStatusDTO2 = new UserOnlineStatusDTO();

        user2.setId(16L);
        user2.setFirstName("mockuser2");
        user2.setSurName("mocksurname2");
        user2.setRole(Role.WORKER);
        userOnlineStatus2.setId(16L);
        userOnlineStatus2.setUser(user2);
        userOnlineStatus2.setIsOnline(true);
        userOnlineStatus2.setCreatedAt(new Date());


        userOnlineStatusDTO2.setId(16L);
        userOnlineStatusDTO2.setFirstName(user2.getFirstName());
        userOnlineStatusDTO2.setSurName(user2.getSurName());
        userOnlineStatusDTO2.setUserOnlineStatus(userOnlineStatus1);
        userOnlineStatusDTO2.setRole(user2.getRole());


        List<UserOnlineStatusDTO> userOnlineStatusDTOList = new ArrayList<>();
        userOnlineStatusDTOList.add(userOnlineStatusDTO1);
        userOnlineStatusDTOList.add(userOnlineStatusDTO2);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userService.findAllByIds(users.stream().map(a -> a.getId()).toList()))
                .thenReturn(users);

        mockMvc.perform(post("/worker/getProfilesOfWorkers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(users.stream().map(a -> a.getId()).toList())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
    @Test
    void shouldUpdateNotificationMarkIsRead() throws Exception {
        Long mockUserId = 1L;

        Notification notification1 = new Notification();
        notification1.setId(15L);
        notification1.setReceiverId(mockUserId);
        notification1.setType(NotificationType.TASK);

        Notification notification2 = new Notification();
        notification2.setId(16L);
        notification2.setReceiverId(mockUserId);
        notification2.setType(NotificationType.OTHER);


        List<Notification> notifications = new ArrayList<>();

        notifications.add(notification1);
        notifications.add(notification2);
        for (Notification n : notifications) {
            n.setIsRead(true);


            mockMvc.perform(put("/notifications/workerUpdateNotificationMarkIsRead/{userId}", mockUserId)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    public Task createMockTask() {
        Task mockTask = new Task();

        mockTask.setId(1L);
        mockTask.setSuperVizorDescription("Sensor kalibrasyonu lazım");
        mockTask.setSuperVizorDeadline(LocalDateTime.now().plusDays(7));

        User assignedToUser = new User();
        assignedToUser.setId(101L);
        assignedToUser.setFirstName("Worker");
        assignedToUser.setSurName("One");
        mockTask.setAssignedTo(assignedToUser);

        User assignedByUser = new User();
        assignedByUser.setId(201L);
        assignedByUser.setFirstName("Supervisor");
        assignedByUser.setSurName("Admin");
        mockTask.setAssignedBy(assignedByUser);

        Sensor mockSensor = new Sensor();
        mockSensor.setId(501L);
        mockSensor.setSensorName("Temperature-Sensor-01");
        mockTask.setSensor(mockSensor);

        mockTask.setCreatedAt(LocalDateTime.now());
        mockTask.setTaskCompletedTime(new Date());

        mockTask.setWorkerArriving(true);
        mockTask.setWorkerArrived(false);
        mockTask.setWorkerOnRoadNote("Traffic delay");

        mockTask.setFinalStatus(SensorStatus.ACTIVE);
        mockTask.setSolvingNote("Calibration completed successfully");




        return mockTask;
    }

@Test
void shouldReturnTasksOfMe() throws Exception {
    Long userId = 1L;
    List<SensorAllAndTaskDTO> mockResponse = List.of(
            createMockTaskDTOForShouldReturnTasksOfMeTest(1L, "Mock Task 1"),
            createMockTaskDTOForShouldReturnTasksOfMeTest(2L, "Mock Task 2")
    );
    //repo testi yok burda
    when(taskService.getSensorListFromTasksOfSingleUser(userId))
            .thenReturn(ResponseEntity.ok(mockResponse));

    mockMvc.perform(get("/worker/getTasksOfMe/{userId}", userId)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
}

    private SensorAllAndTaskDTO createMockTaskDTOForShouldReturnTasksOfMeTest(Long id, String taskName) {
        //sensorfix ve diğer yerler boş sadece iki yer setlenmiş
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setId(id);
        sensorDTO.setSensorName("Sensor for " + taskName);

        UserTaskDTO mockUserDTO = new UserTaskDTO();
        mockUserDTO.setId(1L);
        mockUserDTO.setFirstName("Test");
        mockUserDTO.setSurName("User");
    Date oneDayLater = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

        return new SensorAllAndTaskDTO(
                sensorDTO, id, "Mock Supervizor Description",
                LocalDateTime.now().plusDays(7), mockUserDTO,
                false, false, "Note",
                "Mock Solving Note", null, oneDayLater
        );
    }

    @Test
    void  shouldSaveWorkerLocation() throws Exception{
        String mockUserName = "mockUsername";
        Long mockUserId = 101L;

     GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        double Mocklatitude = 24.0;
        double MockLongitude = 22.0 ;

        Point location = geometryFactory.createPoint(new Coordinate(Mocklatitude, MockLongitude));

        UserLocation mockUserLocation = new UserLocation();

        mockUserLocation.setLocation(location);

        Date now = new Date();

        mockUserLocation.setCreatedAt(now);

        UserLocationDTO userLocationDTO = new UserLocationDTO();
        userLocationDTO.setId(mockUserId);
        userLocationDTO.setLongitude(MockLongitude);
        userLocationDTO.setLatitude(Mocklatitude);

        when(userLocationService.getLocation(mockUserName))
                .thenReturn(userLocationDTO);



        String mockResponse =  "User location details saved successfully";

        when(userLocationService.saveUserLocation(mockUserName, Mocklatitude, MockLongitude))
                .thenReturn(mockResponse);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(mockUserName);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(post("/workers/saveWorkersLocation")
                        .param("lat", String.valueOf(Mocklatitude))
                        .param("longtitude", String.valueOf(MockLongitude))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockResponse)))
                .andExpect(status().isOk());



    }

}