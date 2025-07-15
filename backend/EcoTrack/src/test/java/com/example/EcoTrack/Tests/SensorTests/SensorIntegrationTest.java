package com.example.EcoTrack.Tests.SensorTests;


import com.example.EcoTrack.auth.controller.AuthController;
import com.example.EcoTrack.auth.dto.UserRequestDTO;
import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.auth.service.RefreshTokenService;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.pdfReports.repository.PdfRepository;
import com.example.EcoTrack.security.config.SecurityConfig;
import com.example.EcoTrack.sensors.model.*;
import com.example.EcoTrack.sensors.repository.SensorImageIconRepository;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionImagesRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
import com.example.EcoTrack.sensors.service.SensorService;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
    import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
public class SensorIntegrationTest {

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
    SensorSessionRepository sensorSessionRepository;


    @Autowired
    SensorSessionImagesRepository sensorSessionImagesRepository;




    @Autowired
    UserRepository userRepositoryy;

    @Autowired
    static TaskRepository taskRepository;
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


    private String accessTokenForThisTest;

    @BeforeEach
    public void getTheAccessToken() throws Exception {
        UserRequestDTO validRequest = new UserRequestDTO();
        validRequest.setEmail("manageruser@example.com");
        validRequest.setFirstName("ManagerUser");
        validRequest.setPassword("test1234");

        String requestBody = new ObjectMapper().writeValueAsString(validRequest);

        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(status().isOk())
                        .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(response);
        String accessToken = jsonNode.path("data").path("accessToken").asText();
        System.out.println(accessToken);
        assertNotNull(accessToken);

        this.accessTokenForThisTest = accessToken;

    }



    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:get"})
    void  shouldGetSensors() throws  Exception{
        Sensor mockSensor = new Sensor();

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
        mockLocation.setLocation(point);


        mockSensor.setId(1L);
        mockSensor.setSensorName("Mock Sensor For This Test");
        mockSensor.setStatus(SensorStatus.ACTIVE);
        mockSensor.setSensorLocation(mockLocation);
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());

        mockSensor.setCurrentSensorSession(null);

        sensorRepository.save(mockSensor);

        mockMvc.perform(get("/sensors" ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].sensorName", hasItem(mockSensor.getSensorName())))


                .andDo(print());
//                .andExpect(jsonPath("$[0].SuperVizorGivenCompletedTasksAverageMinLastMonth.mockSupervizor").value(119))
//                .andExpect(jsonPath("$[1].SupervizorGivenTasksTotalCount.mockSupervizor").value(2))
//                .andExpect(jsonPath("$[2].SupervizorGivenTaskCountOfLastDay.mockSupervizor").value(1))

//                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:get"})
    void  shouldGetInduvualSensorLocations() throws  Exception{
        Sensor mockSensor = new Sensor();

        mockSensor.setId(1L);
        mockSensor.setSensorName("Mock Sensor For This Test");
        mockSensor.setStatus(SensorStatus.ACTIVE);

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
        mockLocation.setLocation(point);
        mockSensor.setSensorLocation(mockLocation);
        mockLocation.setSensor(mockSensor);
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());


        sensorRepository.save(mockSensor);

        mockMvc.perform(get("/sensors/location/" + mockSensor.getId() ))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Successfully got sensor"))
                .andExpect(jsonPath("$.data.latitude").value(20.0))
                .andExpect(jsonPath("$.data.longitude").value(25.0))


                .andDo(print());
//                .andExpect(jsonPath("$[0].SuperVizorGivenCompletedTasksAverageMinLastMonth.mockSupervizor").value(119))
//                .andExpect(jsonPath("$[1].SupervizorGivenTasksTotalCount.mockSupervizor").value(2))
//                .andExpect(jsonPath("$[2].SupervizorGivenTaskCountOfLastDay.mockSupervizor").value(1))

//                .andExpect(status().isOk());

    }


    @Test
    @WithMockUser(username = "WorkerUser66",authorities = {"worker:get"})
    void  shouldGetInduvualSensor() throws  Exception{
        Sensor mockSensor = new Sensor();

        mockSensor.setId(1L);
        mockSensor.setSensorName("Mock Sensor For This Test");
        mockSensor.setStatus(SensorStatus.ACTIVE);

        User presentWorkerUserIndatabase = userService.findByUsername("WorkerUser66");

        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(100L);
        mockSensorFix.setUser(presentWorkerUserIndatabase);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
        mockLocation.setLocation(point);
        mockSensor.setSensorLocation(mockLocation);
        mockLocation.setSensor(mockSensor);
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());


        sensorRepository.save(mockSensor);

        mockMvc.perform(get("/sensors/" + mockSensor.getId()
                ))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Successfully got sensor"))
                .andExpect(jsonPath("$.data.id").value(mockSensor.getId()))
                .andExpect(jsonPath("$.data.sensorName").value(mockSensor.getSensorName()))
                .andExpect(jsonPath("$.data.latitude").value(mockSensor.getSensorLocation().getLocation().getX()))
                .andExpect(jsonPath("$.data.longitude").value(mockSensor.getSensorLocation().getLocation().getY()))
                .andDo(print());


    }



    @Test
    @WithMockUser(username = "WorkerUser66",authorities = {"worker:get"})
    void  shouldReturnSensorNotFound_WhenGettingInduvualSensor() throws  Exception{

        mockMvc.perform(get("/sensors/" + 9999 ))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Sensor not found"))
                .andDo(print());


    }

    @Test
    @WithMockUser(username = "NotFoundUser",authorities = {"manager:get"})
    void  shouldReturnUserNotFound_WhenGettingInduvualSensor() throws  Exception{

        mockMvc.perform(get("/sensors/" + 1 ))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User not Found"))
                .andDo(print());


    }

    @Test
    @WithMockUser(username = "john")
    void  shouldReturnUserDontHaveSessions_WhenGettingInduvualSensor() throws  Exception{

        User testUser = new User();
        testUser.setFirstName("john");
        testUser.setEmail("john@example.com");
        testUser.setRole(Role.WORKER);
        testUser.setSensorSessions(null);

        User returnedUser = userRepositoryy.save(testUser);

        mockMvc.perform(get("/sensors/" + 1 ))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User dont have sensor sessions"))
                .andDo(print());


    }


    @Test
    @WithMockUser(username = "WorkerUser66")
    void  shouldgetInduvualSensorWithProtectionForWorker_WhenGettingInduvualSensor() throws  Exception{

        User testUser = new User();
        testUser.setFirstName("john");
        testUser.setEmail("john@example.com");
        testUser.setRole(Role.WORKER);

        Sensor mockSensor = new Sensor();

        mockSensor.setId(11L);
        mockSensor.setSensorName("Mock Sensor For This Test");
        mockSensor.setStatus(SensorStatus.ACTIVE);


        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(100L);
        mockSensorFix.setUser(testUser);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);

        testUser.setSensorSessions(List.of(mockSensorFix));

        sensorRepository.save(mockSensor);

        mockMvc.perform(get("/sensors/" + mockSensor.getId() ))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("You are not Authorized to enter here ."))
                .andDo(print());


    }


    @Test
    @Transactional
    @WithMockUser(username = "WorkerUser66")
    void  shouldgetImageBySessionId() throws  Exception{

        User testUser = userRepositoryy.findByFirstName("WorkerUser66");


        Sensor mockSensor = new Sensor();
        mockSensor.setSensorName("Mock Sensor For This Test");
        mockSensor.setStatus(SensorStatus.ACTIVE);


        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setUser(testUser);
        mockSensorFix.setNote("MockatoMock Note");

        byte[] mockImageData = "mock-image-data".getBytes();
        SensorSessionImages sensorSessionImages = new SensorSessionImages();
        sensorSessionImages.setName("mockımage.png");
        sensorSessionImages.setType("image/png");
        sensorSessionImages.setImage(ImageUtil.compressImage(mockImageData));
        sensorSessionImages.setSensorSessions(mockSensorFix);

        mockSensorFix.setSensorSessionImages(List.of(sensorSessionImages));
        mockSensorFix.setSensor(mockSensor);


        mockSensorFix.setSensorSessionImages(List.of(sensorSessionImages));
        mockSensor.setCurrentSensorSession(mockSensorFix);


        testUser.setSensorSessions(List.of(mockSensorFix));

        sensorSessionRepository.save(mockSensorFix);

        mockMvc.perform(get("/imagess/" + mockSensorFix.getId() ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(sensorSessionImages.getName()))
                .andExpect(jsonPath("$[0].type").value(sensorSessionImages.getType()))
                .andDo(print());


    }

    @Test
    @Transactional
    @WithMockUser(username = "WorkerUser66")
    void  shouldCreateIconImage() throws  Exception{

        Sensor testSensor = new Sensor();
        testSensor.setSensorName("Test Sensor");
        testSensor.setStatus(SensorStatus.ACTIVE);
        Sensor savedSensor = sensorRepository.save(testSensor);
        Long sensorId = savedSensor.getId();

        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "sensor-icon.png",
                MediaType.IMAGE_PNG_VALUE,
                "test-data".getBytes()
        );

        MockHttpServletRequestBuilder requestBuilder =    multipart("/sensor/" + testSensor.getId() + "/create-icon-image")

                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA);


        mockMvc.perform(requestBuilder)
                .andDo(print())
//                .andExpect(jsonPath("$.sensorName").value("UpdatedMockSensor"))
                .andExpect(status().isOk());

        Optional<Sensor> goingtoBeTestingSensor = sensorRepository.findById(testSensor.getId());

        assertEquals(mockFile.getContentType(), goingtoBeTestingSensor.get().getSensorIconImage().getType());

        assertEquals(mockFile.getOriginalFilename(), goingtoBeTestingSensor.get().getSensorIconImage().getName());



    }

    @Test
    @Transactional
    @WithMockUser(username = "WorkerUser66")
    void  shouldUpdateIconImage() throws  Exception{

        Sensor testSensor = new Sensor();
        testSensor.setSensorName("Updated test Sensor");
        testSensor.setStatus(SensorStatus.ACTIVE);

        SensorIconImage sensorIconImage = new SensorIconImage();
        sensorIconImage.setId(testSensor.getId());
        sensorIconImage.setName("mockimage.png");
        sensorIconImage.setType("image/png");
        sensorIconImage.setImage(ImageUtil.compressImage("updated-data".getBytes()));

        testSensor.setSensorIconImage(sensorIconImage);


        sensorImageIconRepository.save(sensorIconImage);
        sensorRepository.save(testSensor);


        MockMultipartFile mockUpdatedFile = new MockMultipartFile(
                "image",
                "sensor-updated.png",
                MediaType.IMAGE_PNG_VALUE,
                "updated-data".getBytes()
        );

        MockHttpServletRequestBuilder requestBuilder =    multipart("/sensor/" + testSensor.getId() + "/update-icon-image")

                .file(mockUpdatedFile)
                .contentType(MediaType.MULTIPART_FORM_DATA);



        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Sensor> goingtoBeTestingSensor = sensorRepository.findById(testSensor.getId());
        SensorIconImage updatedImage = goingtoBeTestingSensor.get().getSensorIconImage();


        assertEquals(testSensor.getSensorIconImage().getName(),goingtoBeTestingSensor.get().getSensorIconImage().getName());
        assertEquals(testSensor.getSensorIconImage().getType(),goingtoBeTestingSensor.get().getSensorIconImage().getType());
        assertEquals(MediaType.IMAGE_PNG_VALUE, updatedImage.getType());
        assertArrayEquals(ImageUtil.compressImage("updated-data".getBytes()),
                updatedImage.getImage());
    }

    //bunların hepsi happy path bu arada herhalde çoğu happy path yani


}
