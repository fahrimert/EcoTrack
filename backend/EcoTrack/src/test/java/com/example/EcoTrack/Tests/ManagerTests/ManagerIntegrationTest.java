package com.example.EcoTrack.Tests.ManagerTests;

import com.example.EcoTrack.auth.controller.AuthController;
import com.example.EcoTrack.auth.dto.UserRequestDTO;
import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.auth.service.RefreshTokenService;
import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.notification.type.NotificationType;
import com.example.EcoTrack.pdfReports.dto.PdfRequestDTO;
import com.example.EcoTrack.pdfReports.model.PdfReports;
import com.example.EcoTrack.pdfReports.repository.PdfRepository;
import com.example.EcoTrack.security.config.SecurityConfig;
import com.example.EcoTrack.sensors.dto.CreateSensorLocationRequestDTO;
import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorIconImage;
import com.example.EcoTrack.sensors.model.SensorLocation;
import com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.sensors.repository.SensorImageIconRepository;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.task.repository.TaskRepository;
import com.example.EcoTrack.task.service.TaskService;
import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.repository.UserOnlineStatusRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.service.UserService;
import com.example.EcoTrack.util.ImageUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
public class ManagerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AuthService authService;

    @Autowired
     private static UserRepository userRepository;

    @Autowired
    SensorImageIconRepository sensorImageIconRepository;

    @Autowired
    SensorRepository sensorRepository;


    @Autowired
     UserRepository userRepositoryy;

    @Autowired
    static  TaskRepository taskRepository;
    @Autowired
    TaskRepository taskRepositoryy;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    PdfRepository pdfRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserOnlineStatusRepository userOnlineStatusRepository;



    @Autowired
    SimpMessagingTemplate messagingTemplate;


    @Autowired
    UserService userService;

    @Autowired
    UserLocationService userLocationService;


    @Autowired
    NotificationService notificationService;

    @Autowired
    JwtService jwtService;


    @Autowired
    SensorService sensorService;


    @Autowired
    TaskService taskService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailServicee;

    @Autowired
    private SecurityConfig securityConfig;



    @Autowired
    AuthController controller;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private static PlatformTransactionManager transactionManager;




    @BeforeEach
    void cleanUserBeforeTestsStatic() {
        User user = userRepositoryy.findByEmail("john@example.com");


        Task task = taskRepositoryy.findByAssignedTo(user);

        if (task != null){
            taskRepositoryy.delete(task);
            taskRepositoryy.flush();
        }

        if (user != null) {
            userRepositoryy.delete(user);
            userRepositoryy.flush();
        }



    }
    @Test
    void  shouldGetSuperVizorTasks() throws  Exception {
        UserRequestDTO validRequest = new UserRequestDTO(
        );
        validRequest.setEmail("manageruser@example.com");
        validRequest.setFirstName("ManagerUser");
        validRequest.setPassword("test1234");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/auth/login",
                validRequest,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");

        String token = (String) data.get("accessToken");
        assertNotNull(token);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

        ResponseEntity<List> responseGetSupervizorTask = restTemplate.exchange(
                "/manager/getSuperVizorTasks",
                HttpMethod.GET,
                entity,
                List.class
        );

        System.out.println(responseGetSupervizorTask);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().size());

    }

    @Test
    void  shouldGetSensorNameCounts() throws  Exception {
        UserRequestDTO validRequest = new UserRequestDTO(
        );
        validRequest.setEmail("manageruser@example.com");
        validRequest.setFirstName("ManagerUser");
        validRequest.setPassword("test1234");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/auth/login",
                validRequest,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, String> data = (Map<String, String>) response.getBody().get("data");

        String token = (String) data.get("accessToken");
        assertNotNull(token);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> responseGetSupervizorTask = restTemplate.exchange(
                "/manager/getAllAssignedTaskStatusValuesForDoughnutComponent",
                HttpMethod.GET,
                entity,
                String.class
        );
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> nameAndCount = mapper.readValue(responseGetSupervizorTask.getBody(),        new TypeReference<List<Map<String, Object>>>() {});

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertThat(nameAndCount.get(0).get("name")).isNotNull();
        assertThat(nameAndCount.get(0).get("count")).isNotNull();

        assertNotNull(response.getBody());


    }

    @Test
    @Commit
    void  getSensorSessionsOfLastMonth() throws  Exception{
        UserRequestDTO validRequest = new UserRequestDTO(
        );
        validRequest.setEmail("manageruser@example.com");
        validRequest.setFirstName("ManagerUser");
        validRequest.setPassword("test1234");


        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/auth/login",
                validRequest,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, String> data = (Map<String, String>) response.getBody().get("data");

        String token = (String) data.get("accessToken");
        assertNotNull(token);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);


        Map<String, Long> uriVariables = new HashMap<>();
        uriVariables.put("id", 5L);
        User testUser = new User();
        testUser.setFirstName("john");
        testUser.setEmail("john@example.com");
        testUser.setRole(Role.WORKER);

        User returnedUser = userRepositoryy.saveAndFlush(testUser);

        Task task = new Task();
        task.setAssignedTo(testUser);
        task.setCreatedAt(LocalDateTime.now().minusMinutes(30));
        task.setTaskCompletedTime(Date.from(Instant.now()));
        task.setWorkerArrived(true);
        Task returnedTask = taskRepositoryy.saveAndFlush(task);



        User updatedUser = userRepositoryy.findById(returnedUser.getId()).orElseThrow();



        userRepositoryy.flush();

        ResponseEntity<String> responseGetSupervizorTask = restTemplate.exchange(
                "/manager/getAverageTaskMinsOfLastMonth" ,
                HttpMethod.GET,
                entity,
                String.class
        );
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Map<String, Integer>> averageChartDataMap = mapper.readValue(
                responseGetSupervizorTask.getBody(),
                new TypeReference<Map<String, Map<String, Integer>>>() {}
        );
        assertNotNull(responseGetSupervizorTask);
        assertEquals(HttpStatus.OK ,responseGetSupervizorTask.getStatusCode());


        Integer actualAvg = averageChartDataMap.get("averageChartData").get(returnedUser.getFirstName());
        Integer avgTime = averageChartDataMap.get("averageChartData").get(returnedUser.getFirstName());
        assertNotNull(avgTime);

    }


    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:delete"})
    void  deleteUserById() throws  Exception{
        User goingToBeDeletedTestUser = new User();
        goingToBeDeletedTestUser.setFirstName("goingtobedeleted");
        goingToBeDeletedTestUser.setEmail("goingtobedeleted@example.com");
        goingToBeDeletedTestUser.setRole(Role.WORKER);


        User returnedgoingToBeDeletedTestUser = userRepositoryy.saveAndFlush(goingToBeDeletedTestUser);


        userRepositoryy.flush();


        mockMvc.perform(delete("/manager/deleteUserById/"+returnedgoingToBeDeletedTestUser.getId()))
                .andDo(print())
                .andExpect(content().string(containsString("User Successfully deleted")))
                .andExpect(status().isOk());

    }


    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:put"})
    void  deactivateUserById() throws  Exception{
        User goingToDeactivatedTestUser = new User();
        goingToDeactivatedTestUser.setFirstName("goingToDeactivated");
        goingToDeactivatedTestUser.setEmail("goingToDeactivated@example.com");
        goingToDeactivatedTestUser.setRole(Role.WORKER);
        goingToDeactivatedTestUser.setIsActive(true);

        User returnedgoingToBeDeactivatedTestUser = userRepositoryy.saveAndFlush(goingToDeactivatedTestUser);

        userRepositoryy.flush();

        mockMvc.perform(put("/manager/deactivateUser/" + goingToDeactivatedTestUser.getId()))
                .andDo(print())
                .andExpect(content().string(containsString("User Successfully deactivated")))
                .andExpect(status().isOk());

    }


    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:get"})
    void  shouldGetThePerformanceTableForSupervizor() throws  Exception{
        User mockSupervizorTestUser = new User();
        mockSupervizorTestUser.setFirstName("mockSupervizor");
        mockSupervizorTestUser.setEmail("mockSupervizor@example.com");
        mockSupervizorTestUser.setRole(Role.SUPERVISOR);

        List<Task> assignedTasks = new ArrayList<>();

        Task task1 = new Task();
        task1.setCreatedAt(LocalDateTime.now().minusDays(2));
        task1.setTaskCompletedTime(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));

        Task task2 = new Task();
        task2.setCreatedAt(LocalDateTime.now().minusHours(3));
        task2.setTaskCompletedTime(Date.from(Instant.now().minus(1,ChronoUnit.HOURS)));

        assignedTasks.add(task1);
        assignedTasks.add(task2);

        mockSupervizorTestUser.setTasksIAssigned(assignedTasks);


        User mockSupervizorTestUserSaved = userRepositoryy.saveAndFlush(mockSupervizorTestUser);

        userRepositoryy.flush();

        mockMvc.perform(get("/manager/getTheSupervizorPerformanceCharts" ))
                .andDo(print())
                .andExpect(jsonPath("$[0].SuperVizorGivenCompletedTasksAverageMinLastMonth.mockSupervizor").value(119))
                .andExpect(jsonPath("$[1].SupervizorGivenTasksTotalCount.mockSupervizor").value(2))
                .andExpect(jsonPath("$[2].SupervizorGivenTaskCountOfLastDay.mockSupervizor").value(1))

                .andExpect(status().isOk());

    }


    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:get"})
    void  shouldgetAllSuperVizor() throws  Exception{
        User mockSupervizorTestUser = new User();
        mockSupervizorTestUser.setFirstName("mockSupervizor");
        mockSupervizorTestUser.setEmail("mockSupervizor@example.com");
        mockSupervizorTestUser.setRole(Role.SUPERVISOR);

        List<Task> assignedTasks = new ArrayList<>();

        User mockSupervizorTestUserSaved = userRepositoryy.saveAndFlush(mockSupervizorTestUser);

        userRepositoryy.flush();

        mockMvc.perform(get("/manager/getAllSupervizors" ))
                .andDo(print())
               .andExpect(jsonPath("$[0].firstName").value("mockSupervizor"))
                .andExpect(jsonPath("$[0].role").value("SUPERVISOR"))
                .andExpect(status().isOk());

    }


    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:get"})
    void shouldgetAllSupervizorsAndUsers() throws Exception{
        User mockSupervizorTestUser = new User();
        mockSupervizorTestUser.setFirstName("mockSupervizor");
        mockSupervizorTestUser.setEmail("mockSupervizor@example.com");
        mockSupervizorTestUser.setRole(Role.SUPERVISOR);

        User mockWorkerTestUser = new User();
        mockWorkerTestUser.setFirstName("mockWorker");
        mockWorkerTestUser.setEmail("mockWorker@example.com");
        mockWorkerTestUser.setRole(Role.WORKER);

        User mockSupervizorTestUserSaved = userRepositoryy.saveAndFlush(mockSupervizorTestUser);
        User mockWorkerTestUserSaved = userRepositoryy.saveAndFlush(mockWorkerTestUser);

        userRepositoryy.flush();

        mockMvc.perform(get("/manager/getAllSupervizorsAndUsers" ))
                .andDo(print())
                .andExpect(jsonPath("$[*].firstName", hasItem("mockSupervizor")))
                .andExpect(jsonPath("$[*].firstName", hasItem("mockWorker")))
                .andExpect(status().isOk());


    }

    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:get"})
    void shouldgetAllSensorForManagerUse() throws Exception{
        SensorIconImage icon = new SensorIconImage();
        icon.setImage(ImageUtil.compressImage("mockİmage".getBytes()));
        icon.setName("icon.png");
        icon.setType("image/png");
        sensorImageIconRepository.saveAndFlush(icon);


        Sensor mockSensor = new Sensor();

        mockSensor.setId(1L);
        mockSensor.setSensorName("Mock Sensor");
        mockSensor.setSensorIconImage(icon);
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());

        mockSensor.setCurrentSensorSession(null);

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
        mockLocation.setLocation(point);

        mockSensor.setSensorLocation(mockLocation);

        sensorRepository.saveAndFlush(mockSensor);

        userRepositoryy.flush();

        mockMvc.perform(get("/manager/getAllSensorForManagerUse" ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].sensorName", hasItem("Mock Sensor")))
        ;


    }

    @Test
    @WithMockUser(username = "ManagerUser")
    void shouldgetJustDetailOfSensorForManagerManageSensorUsage() throws Exception{
        SensorIconImage icon = new SensorIconImage();
        icon.setImage(ImageUtil.compressImage("mockİmage".getBytes()));
        icon.setName("icon.png");
        icon.setType("image/png");
        sensorImageIconRepository.saveAndFlush(icon);


        Sensor mockSensor = new Sensor();

        mockSensor.setId(1L);
        mockSensor.setSensorName("Mock Sensor");
        mockSensor.setSensorIconImage(icon);
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());

        mockSensor.setCurrentSensorSession(null);

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
        mockLocation.setLocation(point);

        mockSensor.setSensorLocation(mockLocation);

        sensorRepository.saveAndFlush(mockSensor);

        userRepositoryy.flush();

        mockMvc.perform(get("/sensors/sensormanagement/"+ mockSensor.getId()))
                .andDo(print())
                .andExpect(content().string(containsString("Successfully got sensor for manager sensor management page")))
                .andExpect(status().isAccepted());

    }

    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:write"})
    void shouldManagerCreateSensor() throws Exception{
        String mockSensorName = "mock Sensor Name";
        byte[] mockImageData = "mock-image-data".getBytes();

        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "sensor-icon.png",
                MediaType.IMAGE_PNG_VALUE,
                "test-data".getBytes()
        );
        MockHttpServletRequestBuilder requestBuilder =    multipart("/manager/managerCreateSensor")

                .file(mockFile)
                .param("sensorName", "Test Sensor")
                .contentType(MediaType.MULTIPART_FORM_DATA);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(jsonPath("$.sensorName").value("Test Sensor"))
                .andExpect(status().isAccepted());

    }


    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:write"})
    void shouldManagerUpdateSensorLocation() throws Exception{

        Sensor mockSensor = new Sensor();

        mockSensor.setSensorName("Mock Sensor");
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());

        mockSensor.setCurrentSensorSession(null);

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
        mockLocation.setLocation(point);

        mockSensor.setSensorLocation(mockLocation);



        Sensor savedSensor = sensorRepository.save(mockSensor);

        CreateSensorLocationRequestDTO dto = new CreateSensorLocationRequestDTO();
        System.out.println(savedSensor.getId());
        dto.setId(savedSensor.getId());
        dto.setLatitude(mockSensor.getSensorLocation().getLocation().getX());
        dto.setLongitude(mockSensor.getSensorLocation().getLocation().getY());

        String jsonBody = new ObjectMapper().writeValueAsString(dto);


        mockSensor.setSensorLocation(mockLocation);
        mockMvc.perform(post("/manager/updateSensorLocations" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.latitude").value(20.0))
                .andExpect(jsonPath("$.longitude").value(25.0))

        ;

    }


    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:write"})
    void shouldmanagerUpdateSensor() throws Exception{
        SensorIconImage icon = new SensorIconImage();
        icon.setImage(ImageUtil.compressImage("mockİmage".getBytes()));
        icon.setName("icon.png");
        icon.setType("image/png");
        sensorImageIconRepository.saveAndFlush(icon);


        Sensor mockSensor = new Sensor();

        mockSensor.setId(1L);
        mockSensor.setSensorName("Mock Sensor");
        mockSensor.setSensorIconImage(icon);
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());

        mockSensor.setCurrentSensorSession(null);

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
        mockLocation.setLocation(point);

        mockSensor.setSensorLocation(mockLocation);

        sensorRepository.saveAndFlush(mockSensor);

        String mockSensorName = "mock Sensor Name";
        byte[] mockImageData = "mock-image-data".getBytes();

        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "sensor-icon.png",
                MediaType.IMAGE_PNG_VALUE,
                "test-data".getBytes()
        );
        MockHttpServletRequestBuilder requestBuilder =    multipart("/manager/managerUpdateSensor")

                .file(mockFile)
                .param("sensorName", "UpdatedMockSensor")
                .param("sensorId", String.valueOf(mockSensor.getId()))
                .contentType(MediaType.MULTIPART_FORM_DATA);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(jsonPath("$.sensorName").value("UpdatedMockSensor"))
                .andExpect(status().isAccepted());

    }



    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:delete"})
    void shouldManagerDeleteSensor() throws Exception{
        SensorIconImage icon = new SensorIconImage();
        icon.setImage(ImageUtil.compressImage("mockİmage".getBytes()));
        icon.setName("icon.png");
        icon.setType("image/png");
        sensorImageIconRepository.saveAndFlush(icon);


        Sensor mockSensor = new Sensor();

        mockSensor.setId(1L);
        mockSensor.setSensorName("Mock Sensor");
        mockSensor.setSensorIconImage(icon);
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());

        Sensor savedSensor = sensorRepository.saveAndFlush(mockSensor);

        mockMvc.perform(delete("/manager/deleteSensorById/" + savedSensor.getId()))
                .andDo(print())
                .andExpect(content().string(containsString("Sensor Successfully deleted")))
                .andExpect(status().isOk());


    }

    @Test
    @WithMockUser(username = "ManagerUser")
    void shouldGetNotificationById() throws Exception{
        User goingToBeDeletedMockManager = new User();
        goingToBeDeletedMockManager.setFirstName("mockManager");
        goingToBeDeletedMockManager.setEmail("mockManager@example.com");
        goingToBeDeletedMockManager.setRole(Role.MANAGER);
        userRepositoryy.save(goingToBeDeletedMockManager);


        User mockSupervizorTestUser = new User();
        mockSupervizorTestUser.setFirstName("mockSupervizor");
        mockSupervizorTestUser.setEmail("mockSupervizor@example.com");
        mockSupervizorTestUser.setRole(Role.SUPERVISOR);

        userRepositoryy.save(mockSupervizorTestUser);

        PdfReports pdfReports = new PdfReports();
        pdfReports.setSensorName("Su Seviyesi Sensörü");
        pdfReports.setTechnicianNote("Acme Su Deposu İçin Kurulum");
        pdfReports.setStartTime("2023-05-15T09:00:00");
        pdfReports.setCompletedTime("2023-05-15T12:30:00");
        pdfReports.setLatitude(41.0082);
        pdfReports.setLongitude(28.9784);
        pdfReports.setManager(goingToBeDeletedMockManager);
        pdfReports.setSupervisor(mockSupervizorTestUser);
        pdfRepository.save(pdfReports);

        Notification mockNotification = new Notification();
        mockNotification.setIsRead(false);
        mockNotification.setType(NotificationType.TASK);
        mockNotification.setReceiverId(goingToBeDeletedMockManager.getId());
        mockNotification.setCreatedAt(LocalDateTime.now());
        mockNotification.setPdfReportId(pdfReports.getId());
        mockNotification.setSuperVizorDeadline(LocalDateTime.now().plusDays(7));
        notificationRepository.save(mockNotification);




        mockMvc.perform(get("/notifications/getNotificationOfManager/" + goingToBeDeletedMockManager.getId()))
                .andDo(print())
                .andExpect(jsonPath("$[0].sensorName").value(pdfReports.getSensorName()))
//                .andExpect(content().string(containsString(goingToBeDeletedMockManager.getNotifications())))
                .andExpect(status().isOk());


    }


    @Test
    @WithMockUser(username = "ManagerUser")
    void shouldGetAllSupervizorReports() throws Exception{
        User goingToBeDeletedMockManager = new User();
        goingToBeDeletedMockManager.setFirstName("mockManager");
        goingToBeDeletedMockManager.setEmail("mockManager@example.com");
        goingToBeDeletedMockManager.setRole(Role.MANAGER);
        userRepositoryy.save(goingToBeDeletedMockManager);


        User mockSupervizorTestUser = new User();
        mockSupervizorTestUser.setFirstName("mockSupervizor");
        mockSupervizorTestUser.setEmail("mockSupervizor@example.com");
        mockSupervizorTestUser.setRole(Role.SUPERVISOR);

        System.out.println(mockSupervizorTestUser.getId());

        Sensor sensor = new Sensor();
        sensor.setSensorName("Su Seviyesi Sensörü");
        sensorRepository.save(sensor);



        PdfReports pdfReports = new PdfReports();
        pdfReports.setSensorName("Su Seviyesi Sensörü");
        pdfReports.setTechnicianNote("Acme Su Deposu İçin Kurulum");
        pdfReports.setStartTime("2023-05-15T09:00:00");
        pdfReports.setCompletedTime("2023-05-15T12:30:00");
        pdfReports.setLatitude(41.0082);
        pdfReports.setLongitude(28.9784);
        pdfReports.setSupervisor(mockSupervizorTestUser);
        pdfReports.setManager(goingToBeDeletedMockManager);
        pdfReports.setSensor(sensor);
        pdfRepository.save(pdfReports);

        userRepositoryy.save(mockSupervizorTestUser);


        mockMvc.perform(get("/manager/getAllSupervizorPdfReport/" + mockSupervizorTestUser.getId()))
                .andDo(print())
                .andExpect(jsonPath("$[0].sensorName").value(pdfReports.getSensorName()))
                .andExpect(jsonPath("$[0].note").value(pdfReports.getTechnicianNote()))
                .andExpect(jsonPath("$[0].latitude").value(pdfReports.getLatitude()))
                .andExpect(jsonPath("$[0].longitude").value(pdfReports.getLongitude()))
                .andExpect(status().isOk());


    }
        //burada  yapmadığım manager controllera özel 3 endpointin entegrasyon testi kaldı herhalde sadece
//    @Test
//    @WithMockUser(username = "ManagerUser")
//    void shouldGetPdfReportInduvualSensor() throws Exception{
//        String mockSensorId = "1";
//
//
//        Sensor mockSensor = new Sensor();
//
//        mockSensor.setId(1L);
//        mockSensor.setSensorName("Mock Sensor");
//        mockSensor.setInstallationDate(new Date());
//        mockSensor.setLastUpdatedAt(new Date());
//
//        mockSensor.setCurrentSensorSession(null);
//
//        SensorLocation mockLocation = new SensorLocation();
//        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
//        mockLocation.setLocation(point);
//
//        mockSensor.setSensorLocation(mockLocation);
//
//
//
//
//        sensorRepository.saveAndFlush(mockSensor);
//        User goingToBeDeletedMockManager = new User();
//        goingToBeDeletedMockManager.setFirstName("mockManager");
//        goingToBeDeletedMockManager.setEmail("mockManager@example.com");
//        goingToBeDeletedMockManager.setRole(Role.MANAGER);
//        userRepositoryy.save(goingToBeDeletedMockManager);
//
//
//        User mockSupervizorTestUser = new User();
//        mockSupervizorTestUser.setFirstName("mockSupervizor");
//        mockSupervizorTestUser.setEmail("mockSupervizor@example.com");
//        mockSupervizorTestUser.setRole(Role.SUPERVISOR);
//
//        System.out.println(mockSupervizorTestUser.getId());
//
//        Sensor sensor = new Sensor();
//        sensor.setSensorName("Su Seviyesi Sensörü");
//        sensorRepository.save(sensor);
//
//
//
//        PdfReports pdfReports = new PdfReports();
//        pdfReports.setSensorName("Su Seviyesi Sensörü");
//        pdfReports.setTechnicianNote("Acme Su Deposu İçin Kurulum");
//        pdfReports.setStartTime("2023-05-15T09:00:00");
//        pdfReports.setCompletedTime("2023-05-15T12:30:00");
//        pdfReports.setLatitude(41.0082);
//        pdfReports.setLongitude(28.9784);
//        pdfReports.setSupervisor(mockSupervizorTestUser);
//        pdfReports.setManager(goingToBeDeletedMockManager);
//        pdfReports.setSensor(sensor);
//        pdfRepository.save(pdfReports);
//
//        userRepositoryy.save(mockSupervizorTestUser);
//
//        System.out.println(pdfReports.getSupervisor().getId());
//        System.out.println(userRepositoryy.findAll());
//
//        mockMvc.perform(get("/manager/getAllSupervizorPdfReport/" + mockSupervizorTestUser.getId()))
//                .andDo(print())
//                .andExpect(jsonPath("$[0].sensorName").value(pdfReports.getSensorName()))
//                .andExpect(jsonPath("$[0].note").value(pdfReports.getTechnicianNote()))
//                .andExpect(jsonPath("$[0].latitude").value(pdfReports.getLatitude()))
//                .andExpect(jsonPath("$[0].longitude").value(pdfReports.getLongitude()))
//                .andExpect(status().isOk());
//
//
//    }
//








}
