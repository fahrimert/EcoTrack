package com.example.EcoTrack.Tests.SuperVizorBasedTests;

import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.manager.service.ManagerService;
import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.notification.type.NotificationType;
import com.example.EcoTrack.pdfReports.repository.PdfRepository;
import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.sensors.model.SensorLocation;
import com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.sensors.repository.SensorImageIconRepository;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionImagesRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.sensors.service.SensorSessionImageService;
import com.example.EcoTrack.shared.dto.SensorDTO;
import com.example.EcoTrack.shared.dto.SensorLocationDTO;
import com.example.EcoTrack.supervizor.dto.SensorCountDTO;
import com.example.EcoTrack.supervizor.dto.SensorDateCountDTO;
import com.example.EcoTrack.supervizor.service.SuperVizorService;
import com.example.EcoTrack.task.dto.SensorTaskDTO;
import com.example.EcoTrack.task.dto.TaskDTO;
import com.example.EcoTrack.task.dto.UserTaskDTO;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.task.repository.TaskRepository;
import com.example.EcoTrack.user.dto.UserDTO;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO;
import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.repository.LocationRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions. *;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
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
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@Transactional
public class SuperVisorBasedServiceUnitTests {


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

    @Mock
    NotificationService notificationService;

    @Mock
    private  SimpMessagingTemplate messagingTemplate;

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

    @InjectMocks
    private SuperVizorService superVizorService;


    @Mock
    private JwtService jwtService;


    @Mock
    private SecurityContext securityContext;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @Mock
    private Authentication authentication;

    @Test
    @WithMockUser(username = "testUser")
    void shouldGetAllSensorStatusMetricValues() {
        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(100L);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);

        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setId(101L);
        mockSensorFix2.setNote("Mock Sensor Fix 2 ");
        mockSensorFix2.setFinalStatus(SensorStatus.FAULTY);

        SensorFix mockSensorFix3 = new SensorFix();
        mockSensorFix3.setId(102L);
        mockSensorFix3.setNote("Mock Sensor Fix 3 ");
        mockSensorFix3.setFinalStatus(SensorStatus.SOLVED);

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        Date nextWeek = calendar.getTime();

        mockSensorFix.setCompletedTime(nextWeek);
        mockSensorFix.setStartTime(currentDate);
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix.setNote("mock mock note");

        when(sensorSessionRepository.findAll()).thenReturn(List.of(mockSensorFix, mockSensorFix2, mockSensorFix3));
        // 3. Metodu çağır
        ResponseEntity<Map<SensorStatus, Long>> response = superVizorService.getAllSensorStatusMetricValuesForDoughnutComponent();

        System.out.println(response);
        Map<SensorStatus, Long> statusCounts = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertEquals(2, statusCounts.get(SensorStatus.FAULTY).longValue());
        assertEquals(1, statusCounts.get(SensorStatus.SOLVED).longValue());

        assertNull(statusCounts.get(SensorStatus.ACTIVE));

    }



    @Test
    @WithMockUser
    void shouldGetFaultyLocationsForSupervizorDashboardHeatmapComponent() {
        Sensor mockSensor = new Sensor();
        mockSensor.setId(1L);
        mockSensor.setSensorName("mock sensor name");
        mockSensor.setStatus(SensorStatus.FAULTY);

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
        mockLocation.setLocation(point);

        mockSensor.setSensorLocation(mockLocation);

        Sensor mockSensor2 = new Sensor();
        mockSensor2.setId(2L);
        mockSensor2.setSensorName("mock sensor name 2");
        mockSensor2.setStatus(SensorStatus.FAULTY);

        SensorLocation mockLocation2 = new SensorLocation();
        Point point2 = new GeometryFactory().createPoint(new Coordinate(22, 26));
        mockLocation2.setLocation(point2);

        mockSensor2.setSensorLocation(mockLocation2);

        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(100L);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix.setSensor(mockSensor);

        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setId(102L);
        mockSensorFix2.setNote("MockatoMock Note");
        mockSensorFix2.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix2.setSensor(mockSensor2);


        when(sensorSessionRepository.findAllSensorFixesWithFaultySensors()).thenReturn(List.of(mockSensorFix, mockSensorFix2));


        List<SensorLocationDTO> response = superVizorService.getFaultyLocationsForSupervizorDashboardHeatmapComponent();
        assertNotNull(response);
        assertEquals(mockSensorFix.getId() ,response.get(0).getId());
        assertEquals(mockSensorFix2.getId() ,response.get(1).getId());

        assertEquals(mockSensorFix.getSensor().getSensorLocation().getLocation().getX() ,response.get(0).getLatitude());
        assertEquals(mockSensorFix2.getSensor().getSensorLocation().getLocation().getY() ,response.get(1).getLongitude());




    }

    @Test
    void shouldGetAllWorker() {
        User mockWorker = new User();

        mockWorker.setId(10L);
        mockWorker.setFirstName("Mock worker 1 ");

        User mockWorker2 = new User();

        mockWorker2.setId(12L);
        mockWorker2.setFirstName("Mock worker 2 ");
        List<User> workers = List.of(mockWorker, mockWorker2);

        when(userRepository.findAllByRole(Role.WORKER)).thenReturn(workers);


        List<UserOnlineStatusDTO> response = superVizorService.getAllWorker();
        System.out.println(response);
        assertNotNull(response);

        assertEquals(mockWorker.getId(), response.get(0).getId());
        assertEquals(mockWorker.getFirstName(), response.get(0).getFirstName());
        assertEquals(mockWorker.getRole(), response.get(0).getRole());

        assertEquals(mockWorker2.getId(), response.get(1).getId());
        assertEquals(mockWorker2.getFirstName(), response.get(1).getFirstName());
        assertEquals(mockWorker2.getRole(), response.get(1).getRole());


    }

    @Test
    void shouldGetSensorSessionsOfLastMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        Long mockUserId = 10L;


        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(100L);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix.setStartTime(new Date());
        mockSensorFix.setCompletedTime(new Date(System.currentTimeMillis() + 3600000));


        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setId(101L);
        mockSensorFix2.setNote("Mock Sensor Fix 2 ");
        mockSensorFix2.setStartTime(new Date());
        mockSensorFix2.setCompletedTime(new Date(System.currentTimeMillis() + 7200000));
        mockSensorFix2.setFinalStatus(SensorStatus.FAULTY);

        SensorFix mockSensorFix3 = new SensorFix();
        mockSensorFix3.setId(102L);
        mockSensorFix3.setNote("Mock Sensor Fix 3 ");
        mockSensorFix3.setStartTime(new Date());
        mockSensorFix3.setCompletedTime(new Date(System.currentTimeMillis() + 3600000));
        mockSensorFix3.setFinalStatus(SensorStatus.SOLVED);


        when(sensorSessionRepository.findLastMonthDataByUserId(anyLong(), any(LocalDateTime.class))).thenReturn(List.of(mockSensorFix, mockSensorFix2, mockSensorFix3));
        Map<Long, Map<String, Object>> result = superVizorService.getSensorSessionsOfLastMonth(mockUserId);
        assertNotNull(result);
        assertEquals(3, result.size());

        Map<String, Object> durationInfo1 = result.get(100L);
        assertEquals(60L, durationInfo1.get("rawMinutes"));
        assertTrue(((String) durationInfo1.get("formatted")).contains("00 gün 01 saat 00 dakika"));

        Map<String, Object> durationInfo2 = result.get(101L);
        assertEquals(120L, durationInfo2.get("rawMinutes"));
        assertTrue(((String) durationInfo2.get("formatted")).contains("00 gün 02 saat 00 dakika"));

        Map<String, Object> durationInfo3 = result.get(102L);
        assertEquals(60L, durationInfo3.get("rawMinutes"));
        assertTrue(((String) durationInfo3.get("formatted")).contains("00 gün 01 saat 00 dakika"));


        System.out.println(result);

    }

    @Test
    void shouldGetNonTaskSessionSolvingSensorNames() {

        Long mockUserId = 1L;

        Long mockSensor1Id = 10L;
        String mockSensorName1 = "mock sensor";
        Long mockSensor2Id = 11L;
        String mockSensorName2 = "mock sensor 2";


        User mockUser = new User();
        mockUser.setId(mockUserId);
        mockUser.setFirstName("mock mock");
        when(userService.findById(mockUserId)).thenReturn(mockUser);

        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(100L);
        mockSensorFix.setUser(mockUser);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);

        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setId(100L);
        mockSensorFix2.setUser(mockUser);
        mockSensorFix2.setNote("MockatoMock Note 2 ");
        mockSensorFix2.setFinalStatus(SensorStatus.FAULTY);

        Sensor mockSensor = new Sensor();
        mockSensor.setId(mockSensor1Id);
        mockSensor.setSensorName(mockSensorName1);
        mockSensor.setCurrentSensorSession(mockSensorFix);


        Sensor mockSensor2 = new Sensor();
        mockSensor2.setId(mockSensor2Id);
        mockSensor2.setSensorName(mockSensorName2);
        mockSensor2.setCurrentSensorSession(mockSensorFix2);



        mockSensorFix.setSensor(mockSensor);
        mockSensorFix2.setSensor(mockSensor2);
        mockUser.setSensorSessions(List.of(sensorFix, mockSensorFix2));
        when(sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(mockUser)).thenReturn(List.of(mockSensorFix, mockSensorFix2));

        List<SensorCountDTO> result = superVizorService.getNonTaskSessionSolvingSensorNames(mockUserId);
        assertNotNull(result);
        assertEquals(mockSensorName1, result.get(0).getName());
        assertEquals(mockSensorName2, result.get(1).getName());
        assertEquals(2, result.size());

    }


    @Test
    void shouldMapToDateCount() {
        Long mockUserId = 1L;

        User mockUser = new User();
        mockUser.setId(mockUserId);
        mockUser.setFirstName("mock mock");


        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));

        Sensor mockSensor = new Sensor();
        mockSensor.setId(1L);
        mockSensor.setSensorName("mock sensor name");


        Sensor mockSensor2 = new Sensor();
        mockSensor2.setId(2L);
        mockSensor2.setSensorName("mock sensor name 2");

        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(100L);
        mockSensorFix.setUser(mockUser);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix.setSensor(mockSensor);
        mockSensorFix.setCompletedTime(new Date(System.currentTimeMillis() + 3600000));


        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setId(101L);
        mockSensorFix2.setUser(mockUser);
        mockSensorFix2.setNote("MockatoMock Note 2 ");
        mockSensorFix2.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix2.setSensor(mockSensor);
        mockSensorFix2.setCompletedTime(new Date(System.currentTimeMillis() + 7200000));


        mockSensor.setCurrentSensorSession(mockSensorFix);
        mockSensor2.setCurrentSensorSession(mockSensorFix2);

        mockUser.setSensorSessions(List.of(mockSensorFix, mockSensorFix2));

        when(sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(mockUser)).thenReturn(List.of(mockSensorFix, mockSensorFix2));

        List<SensorDateCountDTO> result = superVizorService.getDatesAndBasedOnTheirSessionCounts(mockUserId);
        System.out.println(result);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getCount());

        LocalDate expectedDate = new Date(System.currentTimeMillis() + 3600000).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Assertions.assertEquals(expectedDate, result.get(0).getDate());
    }

    @Test
    void  shouldGetNonTaskHeatmapComponentForSupervizorDashboardPage(){
        Long mockUserId = 10L;

        User mockUser = new User();
        mockUser.setId(mockUserId);
        mockUser.setFirstName("mock mock");
        when(userService.findById(mockUserId)).thenReturn(mockUser);

        Sensor mockSensor = new Sensor();
        mockSensor.setId(1L);
        mockSensor.setSensorName("mock sensor name");


        Sensor mockSensor2 = new Sensor();
        mockSensor2.setId(2L);
        mockSensor2.setSensorName("mock sensor name 2");

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
        mockLocation.setLocation(point);

        mockSensor.setSensorLocation(mockLocation);


        SensorLocation mockLocation2 = new SensorLocation();
        Point point2 = new GeometryFactory().createPoint(new Coordinate(22, 27));
        mockLocation2.setLocation(point2);
        mockSensor2.setSensorLocation(mockLocation2);


        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(100L);
        mockSensorFix.setUser(mockUser);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix.setSensor(mockSensor);
        mockSensorFix.setCompletedTime(new Date(System.currentTimeMillis() + 3600000));


        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setId(101L);
        mockSensorFix2.setUser(mockUser);
        mockSensorFix2.setNote("MockatoMock Note 2 ");
        mockSensorFix2.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix2.setSensor(mockSensor2);
        mockSensorFix2.setCompletedTime(new Date(System.currentTimeMillis() + 7200000));

        mockUser.setSensorSessions(List.of(mockSensorFix,mockSensorFix2));
        when(sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(mockUser)).thenReturn(List.of(mockSensorFix,mockSensorFix2));

        List<SensorLocationDTO> result = superVizorService.getNonTaskHeatmapComponentForSupervizorDashboardPage(mockUserId);


        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals(mockSensorFix.getSensor().getId() ,result.get(0).getId());
        assertEquals(mockSensorFix.getSensor().getSensorLocation().getLocation().getX() ,result.get(0).getLatitude());

        assertEquals(mockSensorFix2.getSensor().getId() ,result.get(1).getId());
        assertEquals(mockSensorFix.getSensor().getId() ,result.get(0).getId());
        assertEquals(mockSensorFix.getSensor().getSensorLocation().getLocation().getY() ,result.get(0).getLongitude());

        System.out.println(result);

    }

    //burda bi hata verdi kalsın bu test
    @Test
    void shouldSupervizorCreateTaskForWorker(){
        UserTaskDTO userTaskDTOassignedto = new UserTaskDTO();
        UserTaskDTO userTaskDTOassignedBy = new UserTaskDTO();
        SensorTaskDTO mockSensorTaskDTO = new SensorTaskDTO();
        Long mockUserId = 10L;
        Long mockUser2Id = 11L;

        Sensor mockSensor = new Sensor();
        mockSensor.setId(1L);
        mockSensor.setSensorName("mock sensor name");
        mockSensor.setStatus(SensorStatus.ACTIVE);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        String mockUsernamee = SecurityContextHolder.getContext().getAuthentication().getName();

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
        mockLocation.setLocation(point);

        mockSensor.setSensorLocation(mockLocation);


        Task mockTask = new Task();
        mockTask.setSensor(mockSensor);

        when(sensorRepository.findById(mockTask.getSensor().getId())).thenReturn(Optional.of(mockSensor));


        mockSensorTaskDTO.setId(mockSensor.getId());
        mockSensorTaskDTO.setSensorName(mockSensor.getSensorName());
        mockSensorTaskDTO.setLatitude(mockSensor.getSensorLocation().getLocation().getX());
        mockSensorTaskDTO.setLongitude(mockSensor.getSensorLocation().getLocation().getY());


        User mockUser = new User();
        mockUser.setId(mockUserId);
        mockUser.setFirstName("testUser");

        User mockUser2 = new User();
        mockUser2.setId(mockUser2Id);
        mockUser2.setFirstName("testUser");
        mockUser2.setRole(Role.SUPERVISOR);

        mockTask.setAssignedTo(mockUser);
        mockTask.setAssignedBy(mockUser2);
        when(userService.findById(mockTask.getAssignedTo().getId())).thenReturn(mockUser);
        when(userService.findById(mockTask.getAssignedBy().getId())).thenReturn(mockUser2);
        when(userService.findByUsername(mockUser.getFirstName())).thenReturn(mockUser);


        Notification mockNotification = new Notification();
        mockNotification.setId(101L);
        mockNotification.setIsRead(false);
        mockNotification.setType(NotificationType.TASK);
        mockNotification.setReceiverId(mockUserId);
        mockNotification.setSenderId(15L);
        mockNotification.setCreatedAt(LocalDateTime.now());
        mockNotification.setSuperVizorDeadline(LocalDateTime.now().plusDays(7));


        Sensor mockSensor2 = new Sensor();
        mockSensor2.setId(2L);
        mockSensor2.setSensorName("mock sensor name 2");

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(mockTask);

        mockUser2.setTasksIAssigned(tasks);

        mockUser.setTasksAssignedToMe(tasks);
        mockSensor.setTasks(List.of(mockTask));





        userTaskDTOassignedto.setId(mockUser.getId());
        userTaskDTOassignedto.setFirstName(mockUser.getFirstName());
        userTaskDTOassignedto.setSurName(mockUser.getSurName());


        userTaskDTOassignedBy.setId(mockUser2.getId());
        userTaskDTOassignedBy.setFirstName(mockUser2.getFirstName());
        userTaskDTOassignedBy.setSurName(mockUser2.getSurName());

        TaskDTO taskDTO = new TaskDTO(mockTask.getId(), mockTask.getSuperVizorDescription(),mockTask.getSuperVizorDeadline()
                ,userTaskDTOassignedto
                ,userTaskDTOassignedBy
                ,mockSensorTaskDTO
                ,mockTask.getWorkerArriving()
                ,mockTask.getWorkerArrived()

        );

        ResponseEntity result = superVizorService.supervizorCreateTaskForWorker(mockTask);
        verify(userRepository, times(1)).save(mockUser);
        verify(userRepository, times(1)).save(mockUser2);
        verify(taskRepository).save(any());
        verify(sensorRepository).save(mockSensor);

        verify(notificationService).sendNotification(any(Notification.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/tasks"), any(TaskDTO.class));


        assertEquals(HttpStatus.OK,result.getStatusCode());
        TaskDTO resultTaskDto = (TaskDTO) result.getBody();

        assertEquals(userTaskDTOassignedBy,resultTaskDto.getAssignedBy());
        assertEquals(userTaskDTOassignedto,resultTaskDto.getAssignedTo());
        assertEquals(mockUser.getFirstName(),resultTaskDto.getAssignedTo().getFirstName());
        assertEquals(mockUser2.getFirstName(),resultTaskDto.getAssignedBy().getFirstName());
        assertEquals(HttpStatus.OK,result.getStatusCode());

    }

    @Test
    //burda kaldım
    void  shouldGetTaskIAssigned(){

        Long mockUserId = 10L;
        String mockUserName = "mock Username";
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User mockUser = new User();
        mockUser.setId(mockUserId);
        mockUser.setFirstName("mock mock");
        mockUser.setRole(Role.SUPERVISOR);



        User mockSupervizor = new User();
        mockSupervizor.setId(20L);
        mockSupervizor.setFirstName("testUser");
        mockSupervizor.setSurName("supervizorsurnamemock");
        mockSupervizor.setRole(Role.SUPERVISOR);

        Long mockTaskId = 4L;

        Sensor mockSensor = new Sensor();
        mockSensor.setId(mockSensor.getId());
        mockSensor.setSensorName("Test Sensor");
        SensorLocation location = new SensorLocation();
        location.setLocation(new GeometryFactory().createPoint(new Coordinate(10, 20)));
        mockSensor.setSensorLocation(location);


        Task mockTask = new Task();
        mockTask.setId(mockTaskId);
        mockTask.setSuperVizorDescription("Test Description");
        mockTask.setSuperVizorDeadline(LocalDateTime.now().plusDays(7));
        mockTask.setAssignedTo(mockUser);
        mockTask.setAssignedBy(mockSupervizor);
        mockTask.setSensor(mockSensor);
        mockTask.setWorkerArriving(false);
        mockTask.setWorkerArrived(false);


        when(userRepository.findByFirstName(mockSupervizor.getFirstName())).thenReturn(mockSupervizor);
        when(userService.getAllTask(mockSupervizor.getId())).thenReturn(List.of(mockTask));

        ResponseEntity response = superVizorService.getTasksOfIAssigned();
        List<TaskDTO> taskDTO = (List<TaskDTO>) response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(taskDTO,response.getBody());
        assertEquals(taskDTO.get(0).getAssignedBy().getFirstName(),mockTask.getAssignedBy().getFirstName());
        assertEquals(taskDTO.get(0).getAssignedTo().getFirstName(),mockTask.getAssignedTo().getFirstName());



    }

    @Test
    void getAllAvailableSensorsForAssigningTaskSelectComponent(){

        Sensor mockSensor = new Sensor();
        mockSensor.setId(1L);
        mockSensor.setSensorName("SensorMock");

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(39.9, 32.8));
        mockLocation.setLocation(point);
        mockSensor.setSensorLocation(mockLocation);
        mockSensor.setSensorLocation(mockLocation);
        mockSensor.setCurrentSensorSession(null);



        when(sensorRepository.findAllAvailable()).thenReturn(List.of(mockSensor));

        List<SensorDTO> response = superVizorService.getAllAvailableSensorsForAssigningTaskSelectComponent();

        System.out.println(response);
        assertNotNull(response);

        assertEquals(mockSensor.getSensorName(),response.get(0).getSensorName());
        assertEquals(mockSensor.getSensorLocation().getLocation().getX(),response.get(0).getLatitude());
        assertEquals(mockSensor.getSensorLocation().getLocation().getY(),response.get(0).getLongitude());
        assertEquals(mockSensor.getSensorName(),response.get(0).getSensorName());

    }




}
