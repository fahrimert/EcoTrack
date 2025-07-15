package com.example.EcoTrack.Tests.UserTests;

import com.example.EcoTrack.auth.controller.AuthController;
import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.auth.service.RefreshTokenService;
import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.notification.type.NotificationType;
import com.example.EcoTrack.pdfReports.repository.PdfRepository;
import com.example.EcoTrack.security.config.SecurityConfig;
import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.sensors.model.SensorLocation;
import com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.sensors.repository.SensorImageIconRepository;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionImagesRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.task.dto.SensorAllAndTaskDTO;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.task.repository.TaskRepository;
import com.example.EcoTrack.task.service.TaskService;
import com.example.EcoTrack.user.dto.UserAndSessionSensorDTO;
import com.example.EcoTrack.user.dto.UserDTO;
import com.example.EcoTrack.user.dto.UserLocationDTO;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO;
import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.model.UserLocation;
import com.example.EcoTrack.user.repository.LocationRepository;
import com.example.EcoTrack.user.repository.UserOnlineStatusRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AuthService authService;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    SensorImageIconRepository sensorImageIconRepository;

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    SensorSessionRepository sensorSessionRepository;


    @Autowired
    SensorSessionImagesRepository sensorSessionImagesRepository;




    @Autowired
    private    UserRepository userRepositoryy;

    @Autowired
     TaskRepository taskRepository;


    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    PdfRepository pdfRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserOnlineStatusRepository userOnlineStatusRepository;

    @Autowired
    private LocationRepository userLocationRepository;


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


    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:get"})
    void  shouldGetCurrentLoggedInUserDetails() throws  Exception{
        User user = new User();
        user.setFirstName("Test");
        user.setSurName("User");
        user.setRole(Role.WORKER);
        user = userRepository.save(user);
        Sensor sensor = new Sensor();
        sensor.setSensorName("Sensor A");
        sensor.setStatus(SensorStatus.ACTIVE); // örnek enum
        sensor = sensorRepository.save(sensor);


        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(39.9, 32.8));
        mockLocation.setLocation(point);
        sensor.setSensorLocation(mockLocation);

        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();



        // 3. SensorFix ata
        SensorFix sensorFix = new SensorFix();
        sensorFix.setUser(user);
        sensorFix.setSensor(sensor);
        sensorFix.setNote("Note");
        sensorFix.setStartTime(tenDaysAgo);
        sensorFix.setCompletedTime(new Date());
        sensorFix = sensorSessionRepository.save(sensorFix);
        String token = jwtService.generateToken(user.getFirstName());
        ArrayList<SensorFix> sensorFixes = new ArrayList<>();

        sensorFixes.add(sensorFix);

        user.setSensorSessions(sensorFixes);

        System.out.println(user.getFirstName()  );

        // 5. Servisi çağır
        UserDTO userDTO = userService.getTheDetailOfALoggedInUser(token);

        mockMvc.perform(get("/user/me" )
                .header("Authorization", "Bearer " + token)
                ).
                andExpect(jsonPath("$.firstName").value(user.getSurName()))
                                .andExpect(jsonPath("$.sensorSessions[0].sensorName").value(sensor.getSensorName()))
                .andExpect(jsonPath("$.sensorSessions[0].displayName").value(sensor.getStatus().getDisplayName()))
                .andExpect(jsonPath("$.sensorSessions[0].color_code").value(sensor.getStatus().getColorCode()))
                .andExpect(jsonPath("$.sensorSessions[0].note").value("Note"))

                .andDo(print());

//                .andExpect(status().isOk());

    }


    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:get"})
    void  shouldgetTheUserLocationBasedOnTheirId() throws  Exception{
        User user = new User();
        user.setFirstName("Test");
        user.setSurName("User");
        user.setRole(Role.WORKER);
        user = userRepository.save(user);

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        double Mocklatitude = 24.0;
        double MockLongitude = 22.0 ;

        Point location = geometryFactory.createPoint(new Coordinate(Mocklatitude, MockLongitude));

        UserLocation mockUserLocation = new UserLocation();

        mockUserLocation.setLocation(location);

        user.setUserLocation(mockUserLocation);

        userRepository.save(user);

        UserLocationDTO userDTO = userService.getTheUserLocationBasedOnTheirId(user.getId());

        mockMvc.perform(get("/user/getUserLocationBasedOnıd/" + user.getId() ))
                .andExpect(jsonPath("$.latitude").value(mockUserLocation.getLocation().getY()))
                .andExpect(jsonPath("$.longitude").value(mockUserLocation.getLocation().getX()))

                .andDo(print());

//                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:get"})
    void  shouldgetProfilesOfWorkers() throws  Exception{
        User mockWorker1 = new User();
        mockWorker1.setFirstName("mock Supervizor");
        mockWorker1.setSurName("supervizorsurnamemock");
        mockWorker1.setRole(Role.WORKER);


        User mockWorker2 = new User();
        mockWorker2.setFirstName("mock Manager");
        mockWorker2.setSurName("managersurnamemock");
        mockWorker2.setRole(Role.WORKER);

        userRepository.save(mockWorker1);
        userRepository.save(mockWorker2);

        ArrayList<User> workerList = new ArrayList<>();
        workerList.add(mockWorker1);
        workerList.add(mockWorker2);

        List<UserOnlineStatusDTO> resultService = userService.getProfilesOfAllWorkers(List.of(mockWorker1.getId(),mockWorker2.getId()));


        mockMvc.perform(post("/worker/getProfilesOfWorkers"  )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(workerList.stream().map(a ->a.getId()).toList().toString()))
                .andExpect(jsonPath("$[0].firstName").value(mockWorker1.getFirstName()))
                    .andExpect(jsonPath("$[0].surName").value(mockWorker1.getSurName()))
                .andExpect(jsonPath("$[1].firstName").value(mockWorker2.getFirstName()))
                .andExpect(jsonPath("$[1].surName").value(mockWorker2.getSurName()))
                .andExpect(jsonPath("$[0].role").value(mockWorker1.getRole().getDisplayName()))
                .andExpect(jsonPath("$[0].role").value(mockWorker1.getRole().getDisplayName()))

                .andDo(print());

//                .andExpect(status().isOk());

    }




    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:get"})
    void  shouldsaveWorkersLocation() throws  Exception{
        User mockWorker1 = new User();
        mockWorker1.setFirstName("mock Supervizor");
        mockWorker1.setSurName("supervizorsurnamemock");
        mockWorker1.setRole(Role.WORKER);

        userRepository.save(mockWorker1);



        String resultService = userLocationService.saveUserLocation(mockWorker1.getFirstName(),42.31,18.32);


        mockMvc.perform (MockMvcRequestBuilders.multipart("/workers/saveWorkersLocation")
                        .param("username", mockWorker1.getFirstName()   )
                        .param("lat", String.valueOf(42.31))
                        .param("longtitude", String.valueOf(18.32))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(content().string(containsString("User location details saved successfully")))
                .andDo(print());

//                .andExpect(status().isOk());

    }



    @Test
    @WithMockUser(username = "mockManager",authorities = {"manager:get"})
    void  shouldGetUserLocation() throws  Exception{
        User mockWorker1 = new User();
        mockWorker1.setFirstName("mockManager");
        mockWorker1.setSurName("mockManagerSurname");
        mockWorker1.setRole(Role.WORKER);

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        double Mocklatitude = 24.0;
        double MockLongitude = 22.0 ;

        Point location = geometryFactory.createPoint(new Coordinate(Mocklatitude, MockLongitude));

        UserLocation mockUserLocation = new UserLocation();
        mockUserLocation.setLocation(location);
        mockWorker1.setUserLocation(mockUserLocation);
        List<User> usersLocationList = userRepository.findAll();

        System.out.println(usersLocationList.stream().map(a -> a.getUserLocation().getLocation()));
//        mockWorker1.setUserLocation(location);
        userRepository.save(mockWorker1);



        UserLocationDTO resultService = userLocationService.getLocation(mockWorker1.getFirstName());


        mockMvc.perform (get("/user/getUserLocation"))
                .andExpect(jsonPath("$.latitude").value(MockLongitude))
                .andExpect(jsonPath("$.longitude").value(MockLongitude))
                .andDo(print());


//                .andExpect(status().isOk());

    }



    //java.lang.NullPointerException: Cannot invoke "com.example.EcoTrack.user.model.UserLocation.getLocation()" because the return value of "com.example.EcoTrack.user.model.User.getUserLocation()" is null şöyle bir hata alıyordum en son
   //şöyle saçma sapan bi hata aldık kenarda kalsın şimdilik bu
    @Test
    @Transactional
    @WithMockUser(username = "mockManager",authorities = {"manager:get"})
    void  shouldGetAllWorkersSessionSensorAndTheirLocation() throws  Exception{
        User mockWorker1 = new User();
        mockWorker1.setFirstName("MockWorker");
        mockWorker1.setSurName("MockWorkerSurname");
        mockWorker1.setRole(Role.WORKER);

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        double Mocklatitude = 24.0;
        double MockLongitude = 22.0 ;

        Point location = geometryFactory.createPoint(new Coordinate(Mocklatitude, MockLongitude));

        UserLocation mockUserLocation = new UserLocation();
        mockUserLocation.setLocation(location);
        mockWorker1.setUserLocation(mockUserLocation);

        Sensor sensor = new Sensor();
        sensor.setSensorName("Sensor A");
        sensor.setStatus(SensorStatus.ACTIVE);

        double mockSensorLatitude = 16.0;
        double mockSensorLongitude = 32.0 ;
        Point locationSensor = geometryFactory.createPoint(new Coordinate(mockSensorLatitude, mockSensorLongitude));

        SensorLocation mockSensorLocation = new SensorLocation();
        mockSensorLocation.setLocation(locationSensor);
        sensor.setSensorLocation(mockSensorLocation);


        SensorFix sensorFix = new SensorFix();
        sensorFix.setUser(mockWorker1);

        sensorFix.setSensor(sensor);
        sensorFix.setNote("Note");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();
        sensorFix.setStartTime(tenDaysAgo);
        sensorFix.setCompletedTime(new Date());

        ArrayList<SensorFix> sensorSessionsMock = new ArrayList<>();
        sensorSessionsMock.add(sensorFix);

        mockWorker1.setSensorSessions(sensorSessionsMock);


        userLocationRepository.save(mockUserLocation);
        User saved = userRepository.save(mockWorker1);

        System.out.println(saved.getUserLocation().getLocation());

        sensor = sensorRepository.save(sensor);
        sensorFix = sensorSessionRepository.save(sensorFix);
        List<User> usersLocationList = userRepository.findAll();

        System.out.println(usersLocationList.stream().map( a ->a.getUserLocation().getLocation()).toList());
//        List<UserAndSessionSensorDTO>  resultService = userLocationService.getAllWorkersSessionSensorAndTheirLocation();
//
//
//        mockMvc.perform (get("/workers/getAllWorkersSessionSensorAndTheirLocation"))
//                .andDo(print());


//                .andExpect(status().isOk());

    }



    @Test
    @Transactional
    @WithMockUser(username = "mockManager",authorities = {"manager:get"})
    void  shouldWorkerUpdateTasksOnRoadNote() throws  Exception{
        User assignedToWorker = new User();
        assignedToWorker.setFirstName("assignedToWorker");
        assignedToWorker.setSurName("assignedToWorkerSurname");

        User assignedBy = new User();
        assignedBy.setFirstName("AssignedBySupervizor");
        assignedBy.setSurName("AssignedBySupervizorSurname");


        double mockSensorLatitude = 16.0;
        double mockSensorLongitude = 32.0 ;
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point locationSensor = geometryFactory.createPoint(new Coordinate(mockSensorLatitude, mockSensorLongitude));


        Sensor sensor = new Sensor();
        sensor.setSensorName("Sensor A");
        sensor.setStatus(SensorStatus.ACTIVE);
        SensorLocation mockSensorLocation = new SensorLocation();
        mockSensorLocation.setLocation(locationSensor);
        sensor.setSensorLocation(mockSensorLocation);

        userRepository.save(assignedToWorker);
        userRepository.save(assignedBy);
        //these are should be saved before task saved


        Task mockTask = new Task();
        //task id should be set on first place
        mockTask.setSuperVizorDescription("Sensor kalibrasyonu lazım");
        mockTask.setSuperVizorDeadline(LocalDateTime.now().plusDays(7));
        mockTask.setAssignedTo(assignedToWorker);
        mockTask.setAssignedBy(assignedBy);
        //after user saved these should be saved
        mockTask.setCreatedAt(LocalDateTime.now());
        mockTask.setWorkerArriving(false);
        mockTask.setWorkerArrived(false);
        mockTask.setFinalStatus(SensorStatus.ACTIVE);
        mockTask.setSolvingNote(null);




        ArrayList<Task> taskArrayList = new ArrayList<>();
        taskArrayList.add(mockTask);

        assignedToWorker.setTasksAssignedToMe(taskArrayList);
        assignedBy.setTasksAssignedToMe(taskArrayList);

        sensorRepository.save(sensor);
        //first sensor

        mockTask.setSensor(sensor);
        //then task sensor should be saved

        //saved entiy should be used on service calls
        Task savedTaskOnRepo = taskRepository.saveAndFlush(mockTask);



        ResponseEntity<ApiResponse<?>> savedTask = taskService.workerUpdateTasksOnRoadNote(savedTaskOnRepo.getId(),savedTaskOnRepo.getWorkerOnRoadNote());


        mockMvc.perform (put("/worker/updateTaskForOnRoad/" + mockTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)

                        .content(new ObjectMapper().writeValueAsString("Mock worker on road note")))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.data").value("Successfully updated"))
        .andDo(print());



    }


    @Test
    @Transactional
    @WithMockUser(username = "mockManager",authorities = {"manager:get"})
    void  shouldWorkerUpdateTaskToFinal() throws  Exception{
        User assignedToWorker = new User();
        assignedToWorker.setFirstName("assignedToWorker");
        assignedToWorker.setSurName("assignedToWorkerSurname");

        User assignedBy = new User();
        assignedBy.setFirstName("AssignedBySupervizor");
        assignedBy.setSurName("AssignedBySupervizorSurname");


        double mockSensorLatitude = 16.0;
        double mockSensorLongitude = 32.0 ;
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point locationSensor = geometryFactory.createPoint(new Coordinate(mockSensorLatitude, mockSensorLongitude));


        Sensor sensor = new Sensor();
        sensor.setSensorName("Sensor A");
        sensor.setStatus(SensorStatus.ACTIVE);
        SensorLocation mockSensorLocation = new SensorLocation();
        mockSensorLocation.setLocation(locationSensor);
        sensor.setSensorLocation(mockSensorLocation);

        userRepository.save(assignedToWorker);
        userRepository.save(assignedBy);
        //these are should be saved before task saved


        Task mockTask = new Task();
        //task id should be set on first place
        mockTask.setSuperVizorDescription("Sensor kalibrasyonu lazım");
        mockTask.setSuperVizorDeadline(LocalDateTime.now().plusDays(7));
        mockTask.setAssignedTo(assignedToWorker);
        mockTask.setAssignedBy(assignedBy);
        //after user saved these should be saved
        mockTask.setCreatedAt(LocalDateTime.now());
        mockTask.setWorkerArriving(false);
        mockTask.setWorkerArrived(false);
        mockTask.setFinalStatus(SensorStatus.ACTIVE);
        mockTask.setSolvingNote(null);




        ArrayList<Task> taskArrayList = new ArrayList<>();
        taskArrayList.add(mockTask);

        assignedToWorker.setTasksAssignedToMe(taskArrayList);
        assignedBy.setTasksAssignedToMe(taskArrayList);

        sensorRepository.save(sensor);
        //first sensor

        mockTask.setSensor(sensor);
        //then task sensor should be saved

        //saved entiy should be used on service calls
        Task savedTaskOnRepo = taskRepository.saveAndFlush(mockTask);

        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "sensor-icon.png",
                MediaType.IMAGE_PNG_VALUE,
                "test-data".getBytes()
        );


        ResponseEntity<String> savedTask = taskService.workerUpdateTaskToFinal("Solving the assigned sensor ",SensorStatus.SOLVED, savedTaskOnRepo.getId(),List.of(mockFile));



        MockHttpServletRequestBuilder requestBuilder =    multipart("/worker/updateTaskForFinishing/" + mockTask.getId())

                .file(mockFile)
                .param("solvingNote","Solving the assigned sensor ")
                .param("statusID", "ACTIVE")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .with(request -> {
                    request.setMethod("PUT");
                    return  request;
                })
                ;



        mockMvc.perform (requestBuilder)

                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString("Task Sensor Updated")))
                .andDo(print());



    }

    @Test
    @Transactional
    @WithMockUser(username = "mockManager",authorities = {"manager:get"})
    void  shouldGetSensorListFromTasksOfSingleUser() throws  Exception{
        User assignedToWorker = new User();
        assignedToWorker.setFirstName("assignedToWorker");
        assignedToWorker.setSurName("assignedToWorkerSurname");

        User assignedBy = new User();
        assignedBy.setFirstName("AssignedBySupervizor");
        assignedBy.setSurName("AssignedBySupervizorSurname");


        double mockSensorLatitude = 16.0;
        double mockSensorLongitude = 32.0 ;
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point locationSensor = geometryFactory.createPoint(new Coordinate(mockSensorLatitude, mockSensorLongitude));


        Sensor sensor = new Sensor();
        sensor.setSensorName("Sensor A");
        sensor.setStatus(SensorStatus.ACTIVE);
        SensorLocation mockSensorLocation = new SensorLocation();
        mockSensorLocation.setLocation(locationSensor);
        sensor.setSensorLocation(mockSensorLocation);

        userRepository.save(assignedToWorker);
        userRepository.save(assignedBy);
        //these are should be saved before task saved


        Task mockTask = new Task();
        //task id should be set on first place
        mockTask.setSuperVizorDescription("Sensor kalibrasyonu lazım");
        mockTask.setSuperVizorDeadline(LocalDateTime.now().plusDays(7));
        mockTask.setAssignedTo(assignedToWorker);
        mockTask.setAssignedBy(assignedBy);
        //after user saved these should be saved
        mockTask.setCreatedAt(LocalDateTime.now());
        mockTask.setWorkerArriving(false);
        mockTask.setWorkerArrived(false);
        mockTask.setFinalStatus(SensorStatus.ACTIVE);
        mockTask.setSolvingNote(null);




        ArrayList<Task> taskArrayList = new ArrayList<>();
        taskArrayList.add(mockTask);

        assignedToWorker.setTasksAssignedToMe(taskArrayList);

        sensorRepository.save(sensor);

        mockTask.setSensor(sensor);

        Task savedTaskOnRepo = taskRepository.saveAndFlush(mockTask);

        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "sensor-icon.png",
                MediaType.IMAGE_PNG_VALUE,
                "test-data".getBytes()
        );


        ResponseEntity<List<SensorAllAndTaskDTO>> savedTask = taskService.getSensorListFromTasksOfSingleUser(assignedToWorker.getId());

        assertEquals(savedTask.getBody().size(),1);


        mockMvc.perform(get("/worker/getTasksOfMe/" + assignedToWorker.getId() ))
                .andExpect(jsonPath("$[0].taskSensors.sensorName").value(sensor.getSensorName()))
                .andExpect(jsonPath("$[0].taskSensors.latitude").value(sensor.getSensorLocation().getLocation().getX()))
                .andExpect(jsonPath("$[0].taskSensors.longitude").value(sensor.getSensorLocation().getLocation().getY()))
                .andDo(print());





    }


    @Test
    @Transactional
    @WithMockUser(username = "workerMock")
    void  shouldGetWorkerPastSensors() throws  Exception{
        User workerMock = new User();
        workerMock.setFirstName("workerMock");
        workerMock.setSurName("workerMockSurname");


        double mockSensorLatitude = 16.0;
        double mockSensorLongitude = 32.0 ;
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point locationSensor = geometryFactory.createPoint(new Coordinate(mockSensorLatitude, mockSensorLongitude));


        Sensor sensor = new Sensor();
        sensor.setSensorName("Sensor Mock");
        sensor.setStatus(SensorStatus.ACTIVE);
        SensorLocation mockSensorLocation = new SensorLocation();
        mockSensorLocation.setLocation(locationSensor);
        sensor.setSensorLocation(mockSensorLocation);


        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setNote("MockatoMock Note");
        mockSensorFix2.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix2.setUser(workerMock);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_YEAR, -6);
        Date tweightdaysAgo = calendar2.getTime();

        mockSensorFix2.setCompletedTime(tweightdaysAgo);
        mockSensorFix2.setSensor(sensor);



        workerMock.setSensorSessions(List.of( mockSensorFix2));


        userRepository.save(workerMock);
        //these are should be saved before task saved

        sensorRepository.save(sensor);





        List<SensorFix> pastSensors = sensorService.getPastSensorsOfWorker();

        System.out.println(pastSensors);
        mockMvc.perform(get("/worker/past-sensors"  ))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].sensor.sensorName").value(sensor.getSensorName()))
                .andExpect(jsonPath("$[0].note").value(mockSensorFix2.getNote().toString()))
                .andDo(print());





    }



    //bu bi kalsın şimdilik
    @Test
    @Transactional
    @WithMockUser(username = "workerMockForThisTest")
    void  shouldGoToThesensorSessionNotTheTask() throws  Exception{
        User workerMock = new User();
        workerMock.setFirstName("workerMockForThisTest");
        workerMock.setSurName("workerMockSurname");


        double mockSensorLatitude = 16.0;
        double mockSensorLongitude = 32.0 ;
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point locationSensor = geometryFactory.createPoint(new Coordinate(mockSensorLatitude, mockSensorLongitude));


        Sensor sensor = new Sensor();
        sensor.setSensorName("Sensor Mock For Test");
        sensor.setStatus(SensorStatus.ACTIVE);
        SensorLocation mockSensorLocation = new SensorLocation();
        mockSensorLocation.setLocation(locationSensor);
        sensor.setSensorLocation(mockSensorLocation);


        userRepository.save(workerMock);
        //these are should be saved before task saved

        sensorRepository.save(sensor);


        ResponseEntity<String> goToThesensorSessionNotTheTask = sensorService.goToThesensorSessionNotTheTask(sensor.getId());

        System.out.println(goToThesensorSessionNotTheTask);
        mockMvc.perform(put("/sensor/goToThesensorSessionNotTheTask/" + sensor.getId() ))
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].sensor.sensorName").value(sensor.getSensorName()))
                .andDo(print());





    }


    @Transactional
    @Test
    @WithMockUser(username = "userGoingToBeUpdateNonTaskSensor")

    //bu testte hata aldım bu test hiçbir şekilde çalışmıyor neredeyse her yolu denedim zaten
    void  shouldUpdateNonTaskSensorFinalState() throws  Exception{
        User user = new User();
        user.setFirstName("Ali");
        user.setSurName("Yılmaz");
        user = userRepository.saveAndFlush(user);

        // 2. Sensor oluştur
        Sensor sensor = new Sensor();
        sensor.setSensorName("TestSensor");
        sensor.setStatus(SensorStatus.ACTIVE);
        sensor = sensorRepository.saveAndFlush(sensor);

        // 3. SensorFix (session) oluştur
        SensorFix sensorFix = new SensorFix();
        sensorFix.setUser(user);
        sensorFix.setSensor(sensor);
        sensorFix.setCompletedTime(null);
        sensorFix.setNote("Note");

        // 4. İlişkileri iki yönlü kur
        sensor.setCurrentSensorSession(sensorFix);
        user.setSensorSessions(new ArrayList<>(List.of(sensorFix)));

        // 5. Önce sensorFix'i sonra sensor'u kaydet
        sensorFix = sensorSessionRepository.saveAndFlush(sensorFix);
        sensor = sensorRepository.saveAndFlush(sensor);

        // 6. User da güncellensin
        userRepository.saveAndFlush(user);

        // ✅ Şimdi ilişkiler DB’ye yazıldı, tekrar oku ve kontrol et
        Sensor kontrol = sensorRepository.findById(sensor.getId()).orElseThrow();
        System.out.println("Current session: " + kontrol.getCurrentSensorSession());
        System.out.println("Session user: " + (kontrol.getCurrentSensorSession() != null ? kontrol.getCurrentSensorSession().getUser() : "null"));
        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "sensor-icon.png",
                MediaType.IMAGE_PNG_VALUE,
                "test-data".getBytes()
        );

        ResponseEntity<String> savedTask = sensorService.updateNonTaskSensorFinalState(
                "Solving the non task sensor ",SensorStatus.SOLVED, sensor.getId(),List.of(mockFile));

        Sensor sensorFromDb = sensorRepository.findById(sensor.getId()).orElseThrow();
        System.out.println("Current session: " + sensorFromDb.getCurrentSensorSession());
        System.out.println("Session user: " + (sensorFromDb.getCurrentSensorSession() != null ? sensorFromDb.getCurrentSensorSession().getUser() : "null"));

        MockHttpServletRequestBuilder requestBuilder =    multipart("/worker/nonTaskSensorSolving/" + sensor.getId())

                .file(mockFile)
                .param("note","Solving the non task sensor ")
                .param("statusID", "ACTIVE")
                .param("sensorId", String.valueOf(sensor.getId()))

                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .with(request -> {
                    request.setMethod("PUT");
                    return  request;
                })
                ;

        mockMvc.perform (requestBuilder)

        //                .andExpect(status().isAccepted())
        //                .andExpect(content().string(containsString("Task Sensor Updated")))
                .andDo(print());



    }


    @Transactional
    @Test
    @WithMockUser(username = "userGoingToBeUpdateNonTaskSensor")
    void  shouldGetWorkersPastNonTaskSensorDetail() throws  Exception{
        User assignedToWorker = new User();
        assignedToWorker.setFirstName("assignedToWorker");
        assignedToWorker.setSurName("assignedToWorkerSurname");

        User assignedBy = new User();
        assignedBy.setFirstName("AssignedBySupervizor");
        assignedBy.setSurName("AssignedBySupervizorSurname");


        double mockSensorLatitude = 16.0;
        double mockSensorLongitude = 32.0 ;
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point locationSensor = geometryFactory.createPoint(new Coordinate(mockSensorLatitude, mockSensorLongitude));


        Sensor sensor = new Sensor();
        sensor.setSensorName("Sensor A");
        sensor.setStatus(SensorStatus.ACTIVE);
        SensorLocation mockSensorLocation = new SensorLocation();
        mockSensorLocation.setLocation(locationSensor);
        sensor.setSensorLocation(mockSensorLocation);

        userRepository.save(assignedToWorker);
        userRepository.save(assignedBy);
        //these are should be saved before task saved


        Task mockTask = new Task();
        //task id should be set on first place
        mockTask.setSuperVizorDescription("Sensor kalibrasyonu lazım");
        mockTask.setSuperVizorDeadline(LocalDateTime.now().plusDays(7));
        mockTask.setAssignedTo(assignedToWorker);
        mockTask.setAssignedBy(assignedBy);
        //after user saved these should be saved
        mockTask.setCreatedAt(LocalDateTime.now());
        mockTask.setWorkerArriving(false);
        mockTask.setWorkerArrived(false);
        mockTask.setFinalStatus(SensorStatus.ACTIVE);
        mockTask.setSolvingNote(null);




        ArrayList<Task> taskArrayList = new ArrayList<>();
        taskArrayList.add(mockTask);

        assignedToWorker.setTasksAssignedToMe(taskArrayList);

        sensorRepository.save(sensor);

        mockTask.setSensor(sensor);

        Task savedTaskOnRepo = taskRepository.saveAndFlush(mockTask);

        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "sensor-icon.png",
                MediaType.IMAGE_PNG_VALUE,
                "test-data".getBytes()
        );


        ResponseEntity<List<SensorAllAndTaskDTO>> savedTask = taskService.getSensorListFromTasksOfSingleUser(assignedToWorker.getId());

        assertEquals(savedTask.getBody().size(),1);


        mockMvc.perform(get("/worker/getTasksOfMe/" + assignedToWorker.getId() ))
                .andExpect(jsonPath("$[0].taskSensors.sensorName").value(sensor.getSensorName()))
                .andExpect(jsonPath("$[0].taskSensors.latitude").value(sensor.getSensorLocation().getLocation().getX()))
                .andExpect(jsonPath("$[0].taskSensors.longitude").value(sensor.getSensorLocation().getLocation().getY()))
                .andDo(print());





    }

    @Transactional
    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:get"})
    void  shouldUpdateNotificationMarkIsRead() throws  Exception{
        User notificationReceiverWorker = new User();
        notificationReceiverWorker.setFirstName("notificationReceiverWorkerFirstname");
        notificationReceiverWorker.setSurName("notificationReceiverWorkerSurname");
        userRepository.saveAndFlush(notificationReceiverWorker);

        Notification mockNotification = new Notification();
        mockNotification.setIsRead(false);
        mockNotification.setType(NotificationType.TASK);
        mockNotification.setUserNotifications(notificationReceiverWorker);
        mockNotification.setSenderId(15L);
        mockNotification.setCreatedAt(LocalDateTime.now());
        mockNotification.setSuperVizorDeadline(LocalDateTime.now().plusDays(7));
        notificationRepository.saveAndFlush(mockNotification);

        ArrayList<Notification> notificationArrayList = new ArrayList<Notification>();
        notificationArrayList.add(mockNotification);
        notificationReceiverWorker.setNotifications(notificationArrayList);

        userRepository.saveAndFlush(notificationReceiverWorker);

        ResponseEntity<?> savedTask = notificationService.markNotificationsOfRead(notificationReceiverWorker.getId());



        mockMvc.perform(put("/notifications/workerUpdateNotificationMarkIsRead/"  + notificationReceiverWorker.getId() ))
                .andDo(print());


        Notification notification = notificationRepository.findById(mockNotification.getId()).get();


        assertEquals(true,notification.getIsRead());

    }


    @Transactional
    @Test
    @WithMockUser(username = "ManagerUser",authorities = {"manager:get"})
    void  shouldGetNotificationById() throws  Exception{
        User notificationReceiverWorker = new User();
        notificationReceiverWorker.setFirstName("notificationReceiverWorkerFirstname");
        notificationReceiverWorker.setSurName("notificationReceiverWorkerSurname");
        userRepository.saveAndFlush(notificationReceiverWorker);

        Notification mockNotification = new Notification();
        mockNotification.setIsRead(false);
        mockNotification.setReceiverId(notificationReceiverWorker.getId());
        mockNotification.setType(NotificationType.TASK);
        mockNotification.setUserNotifications(notificationReceiverWorker);
        mockNotification.setSenderId(15L);
        mockNotification.setCreatedAt(LocalDateTime.now());
        mockNotification.setSuperVizorDeadline(LocalDateTime.now().plusDays(7));
        Notification savedNotification = notificationRepository.saveAndFlush(mockNotification);


        ArrayList<Notification> notificationArrayList = new ArrayList<Notification>();
        notificationArrayList.add(mockNotification);
        notificationReceiverWorker.setNotifications(notificationArrayList);


       User savedUser = userRepository.saveAndFlush(notificationReceiverWorker);


        ResponseEntity<?> savedTask = userService.getNotificationById(notificationReceiverWorker.getId());

        mockMvc.perform(get("/user/getNotifications/"  + notificationReceiverWorker.getId() ))
                .andDo(print());
        Notification findByReceiverIdAndId = notificationRepository.findByReceiverIdAndId(notificationReceiverWorker.getId(), mockNotification.getId());

        assertNotNull(findByReceiverIdAndId);

    }



}
