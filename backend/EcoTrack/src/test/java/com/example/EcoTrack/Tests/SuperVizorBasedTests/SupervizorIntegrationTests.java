package com.example.EcoTrack.Tests.SuperVizorBasedTests;

import com.example.EcoTrack.auth.controller.AuthController;
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
import com.example.EcoTrack.pdfReports.service.PdfReportService;
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
import com.example.EcoTrack.shared.dto.SensorLocationDTO;
import com.example.EcoTrack.shared.dto.SensorWithUserProjection;
import com.example.EcoTrack.supervizor.service.SuperVizorService;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.task.repository.TaskRepository;
import com.example.EcoTrack.task.service.TaskService;
import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.model.UserOnlineStatus;
import com.example.EcoTrack.user.repository.UserOnlineStatusRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.PlatformTransactionManager;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
public class SupervizorIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AuthService authService;

    @Autowired
      UserRepository userRepository;

    @Autowired
    SensorImageIconRepository sensorImageIconRepository;

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    SensorSessionRepository sensorSessionRepository;


    @Autowired
    SensorSessionImagesRepository sensorSessionImagesRepository;



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


    @MockitoBean
    UserService userServicee;



    @Autowired
    UserLocationService userLocationService;


    @Autowired
    NotificationService notificationService;

    @Autowired
    JwtService jwtService;

    @Autowired
    EntityManager entityManager;


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
    private SuperVizorService superVizorService;

    @Autowired
    private PdfReportService pdfReportService;




    @AfterEach
    void cleanPdfReportAfterTestsStatic() {}
    @Test
    @WithMockUser(username = "SupervizorUser",authorities = {"supervisor:get"})
    void  shouldGetAllSensorStatusMetricValuesForDoughnutComponent() throws  Exception{
        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);

        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setNote("MockatoMock Note");
        mockSensorFix2.setFinalStatus(SensorStatus.FAULTY);

        sensorSessionRepository.save(mockSensorFix);
        sensorSessionRepository.save(mockSensorFix2);

        mockMvc.perform(get("/superVizor/getAllSensorStatusMetricValuesForDoughnutComponent" ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.FAULTY").value(2))
                .andDo(print());


    }


    @Test
    @WithMockUser(username = "SupervizorUser", authorities = {"supervisor:get"})
    void shouldGetTimeBasedSessionWorkerStatsForBarChartData() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@mail.com");
        mockUser.setFirstName("Test User");
        mockUser.setPassword("encodedPass");
        mockUser.setRole(Role.WORKER);


        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix.setUser(mockUser);


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -6);
        Date sixDaysAgo = calendar.getTime();


        mockSensorFix.setCompletedTime(sixDaysAgo);
        mockSensorFix.setFinalStatus(SensorStatus.ACTIVE);

        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setNote("MockatoMock Note");
        mockSensorFix2.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix2.setUser(mockUser);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_YEAR, -6);
        Date tweightdaysAgo = calendar2.getTime();

        mockSensorFix2.setCompletedTime(tweightdaysAgo);


        SensorFix mockSensorFix3 = new SensorFix();
        mockSensorFix3.setNote("MockatoMock Note");
        mockSensorFix3.setFinalStatus(SensorStatus.FAULTY);

        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.HOUR, -10);
        Date tenhoursago = calendar3.getTime();
        mockSensorFix3.setCompletedTime(tenhoursago);
        mockSensorFix3.setUser(mockUser);
        mockUser.setSensorSessions(List.of(mockSensorFix, mockSensorFix2, mockSensorFix3));

        userRepository.save(mockUser);


        mockMvc.perform(get("/superVizor/getTimeBasedSessionWorkerStatsForBarChartData"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].last_month['Test User']").value(1))
                .andExpect(jsonPath("$[1].last_week['Test User']").value(1))
                .andExpect(jsonPath("$[2].last_day['Test User']").value(1));


    }



    @Test
    @WithMockUser(username = "SupervizorUser",authorities = {"supervisor:get"})
    //burada bence direkt final statusunu faulty vesaire yapmakla alakalı sıkıntı var çünkü alttaki heatmaple bi farkı yok pekte
    void  shouldgetFaultyLocationsForSupervizorDashboardHeatmapComponent() throws  Exception{
        User workeruser66 = userRepository.findByFirstName("WorkerUser66");

        Sensor mockSensor = new Sensor();
        mockSensor.setSensorName("Test Sensor 1");
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());
        mockSensor.setStatus(SensorStatus.FAULTY);

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(10, 35));
        mockLocation.setLocation(point);
        mockSensor.setSensorLocation(mockLocation);

        Sensor mockSensor2 = new Sensor();
        mockSensor2.setSensorName("Test Sensor 2");
        mockSensor2.setInstallationDate(new Date());
        mockSensor2.setLastUpdatedAt(new Date());
        mockSensor2.setStatus(SensorStatus.FAULTY);



        SensorLocation mockLocation2 = new SensorLocation();
        Point point2 = new GeometryFactory().createPoint(new Coordinate(13, 28));
        mockLocation2.setLocation(point);
        mockSensor2.setSensorLocation(mockLocation2);


        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_YEAR, -6);
        Date sixDaysAgo = calendar2.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedSixDaysAgo = formatter.format(sixDaysAgo);



        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        SimpleDateFormat formatterTen = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTenDaysAgo = formatter.format(tenDaysAgo);

        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setUser(workeruser66);
        mockSensorFix.setSensor(mockSensor);
        mockSensorFix.setCompletedTime(sixDaysAgo);
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);


        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setNote("MockatoMock Note");
        mockSensorFix2.setUser(workeruser66);
        mockSensorFix2.setSensor(mockSensor2);
        mockSensorFix2.setCompletedTime(tenDaysAgo);
        mockSensorFix2.setFinalStatus(SensorStatus.FAULTY);

        List<SensorFix> sensorFixList = new ArrayList<>();
        sensorFixList.add(mockSensorFix);
        sensorFixList.add(mockSensorFix2);


        workeruser66.setSensorSessions(sensorFixList);

        userRepository.saveAndFlush(workeruser66);
        sensorSessionRepository.saveAndFlush(mockSensorFix);
        sensorSessionRepository.saveAndFlush(mockSensorFix2);


        mockMvc.perform(get("/superVizor/getFaultyLocationsForSupervizorDashboardHeatmapComponent"  ))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[*].latitude",
//                        hasItems(mockSensor.getSensorLocation().getLocation().getX(), mockSensor2.getSensorLocation().getLocation().getX())))
//                .andExpect(jsonPath("$[*].longitude",
//                        hasItems(mockSensor.getSensorLocation().getLocation().getY(), mockSensor2.getSensorLocation().getLocation().getY())))
//

                .andDo(print());
    }


    @Test
    @Transactional
    @WithMockUser(username = "SupervizorUser",authorities = {"supervisor:get"})
    void  shouldgetWorkersPastSensors() throws  Exception{
        User mockUser = new User();
        mockUser.setEmail("test@mail.com");
        mockUser.setFirstName("Test User");
        mockUser.setPassword("encodedPass");
        mockUser.setRole(Role.WORKER);


        Sensor mockSensor = new Sensor();

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
        mockLocation.setLocation(point);

        mockSensor.setSensorLocation(mockLocation);
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());

        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setSensor(mockSensor);
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix.setCompletedTime(new Date());

        mockUser.setSensorSessions(List.of(mockSensorFix));

        sensorRepository.save(mockSensor);
        userRepository.saveAndFlush(mockUser);
        sensorSessionRepository.saveAndFlush(mockSensorFix);

        List<SensorWithUserProjection> results = sensorSessionRepository
                .findCompletedSensorsWithUserDetails(mockUser.getRole().name());


        mockMvc.perform(get("/superVizor/getWorkersPastSensors" ))
                .andExpect(status().isOk())
                .andDo(print());

    }


    @Test
    @Transactional
    @WithMockUser(username = "SupervizorUser",authorities = {"supervisor:get"})
    void  shouldgetAllWorkers() throws  Exception{
        User mockWorker = new User();
        mockWorker.setEmail("workerTest15@mail.com");
        mockWorker.setFirstName("Test Worker User");
        mockWorker.setRole(Role.WORKER);

        User mockWorker2 = new User();
        mockWorker2.setEmail("workerTest16@mail.com");
        mockWorker2.setFirstName("Test Worker User 16");
        mockWorker2.setRole(Role.WORKER);

       User mockWorkersaved1 =  userRepository.saveAndFlush(mockWorker);
        User mockWorkersaved2 =   userRepository.saveAndFlush(mockWorker2);


        entityManager.flush();
        entityManager.clear();


        mockMvc.perform(get("/supervizor/getAllWorker" ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].firstName",
                        hasItems(mockWorkersaved1.getFirstName(), mockWorkersaved2.getFirstName())))
                .andDo(print());

    }


    @Test
    @Transactional
    @WithMockUser(username = "SupervizorUser",authorities = {"supervisor:get"})
    void  shouldgetDatesAndBasedOnTheirSessionCounts() throws  Exception{
        User workeruser66 = userRepository.findByFirstName("WorkerUser66");

        Sensor mockSensor = new Sensor();
        mockSensor.setSensorName("Test Sensor 1");
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());

        Sensor mockSensor2 = new Sensor();
        mockSensor2.setSensorName("Test Sensor 2");
        mockSensor2.setInstallationDate(new Date());
        mockSensor2.setLastUpdatedAt(new Date());

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_YEAR, -6);
        Date sixDaysAgo = calendar2.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedSixDaysAgo = formatter.format(sixDaysAgo);



        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        SimpleDateFormat formatterTen = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTenDaysAgo = formatter.format(tenDaysAgo);


        System.out.println(formattedSixDaysAgo);
        System.out.println(formattedTenDaysAgo);

        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setUser(workeruser66);
        mockSensorFix.setSensor(mockSensor);
        mockSensorFix.setCompletedTime(sixDaysAgo);

        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setNote("MockatoMock Note");
        mockSensorFix2.setUser(workeruser66);
        mockSensorFix2.setSensor(mockSensor2);
        mockSensorFix2.setCompletedTime(tenDaysAgo);

        List<SensorFix> sensorFixList = new ArrayList<>();
        sensorFixList.add(mockSensorFix);
        sensorFixList.add(mockSensorFix2);


        userRepository.saveAndFlush(workeruser66);
        sensorSessionRepository.saveAndFlush(mockSensorFix);
        sensorSessionRepository.saveAndFlush(mockSensorFix2);


        mockMvc.perform(get("/superVizorSensors/getSensorDatesAndSessionCounts/" + workeruser66.getId() ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].date").value(formattedSixDaysAgo))
                .andExpect(jsonPath("[0].count").value(1))
                .andExpect(jsonPath("[2].date").value(formattedTenDaysAgo))
                .andExpect(jsonPath("[2].count").value(1))


//                .andExpect(jsonPath("[0].count").value(1))
//                .andExpect(jsonPath("[1].name").value(mockSensor.getSensorName()))
//                .andExpect(jsonPath("[1].count").value(1))

//                .andExpect(jsonPath("$[*].firstName",
//                        hasItems(mockWorkersaved1.getFirstName(), mockWorkersaved2.getFirstName())))
                .andDo(print());



    }



    @Test
    @Transactional
    @WithMockUser(username = "SupervizorUser",authorities = {"supervisor:get"})
    void  shouldgetNonTaskHeatmapComponent() throws  Exception{
        User workeruser66 = userRepository.findByFirstName("WorkerUser66");

        Sensor mockSensor = new Sensor();
        mockSensor.setSensorName("Test Sensor 1");
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(10, 35));
        mockLocation.setLocation(point);
        mockSensor.setSensorLocation(mockLocation);

        Sensor mockSensor2 = new Sensor();
        mockSensor2.setSensorName("Test Sensor 2");
        mockSensor2.setInstallationDate(new Date());
        mockSensor2.setLastUpdatedAt(new Date());

        SensorLocation mockLocation2 = new SensorLocation();
        Point point2 = new GeometryFactory().createPoint(new Coordinate(13, 28));
        mockLocation2.setLocation(point);
        mockSensor2.setSensorLocation(mockLocation2);


        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_YEAR, -6);
        Date sixDaysAgo = calendar2.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedSixDaysAgo = formatter.format(sixDaysAgo);



        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        SimpleDateFormat formatterTen = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTenDaysAgo = formatter.format(tenDaysAgo);

        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setUser(workeruser66);
        mockSensorFix.setSensor(mockSensor);
        mockSensorFix.setCompletedTime(sixDaysAgo);

        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setNote("MockatoMock Note");
        mockSensorFix2.setUser(workeruser66);
        mockSensorFix2.setSensor(mockSensor2);
        mockSensorFix2.setCompletedTime(tenDaysAgo);

        List<SensorFix> sensorFixList = new ArrayList<>();
        sensorFixList.add(mockSensorFix);
        sensorFixList.add(mockSensorFix2);


        workeruser66.setSensorSessions(sensorFixList);

        userRepository.saveAndFlush(workeruser66);
        sensorSessionRepository.saveAndFlush(mockSensorFix);
        sensorSessionRepository.saveAndFlush(mockSensorFix2);


        mockMvc.perform(get("/supervizor/getNonTaskHeatmapComponent/" + workeruser66.getId() ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].latitude",
                        hasItems(mockSensor.getSensorLocation().getLocation().getX(), mockSensor2.getSensorLocation().getLocation().getX())))
                .andExpect(jsonPath("$[*].longitude",
                        hasItems(mockSensor.getSensorLocation().getLocation().getY(), mockSensor2.getSensorLocation().getLocation().getY())))
                .andDo(print());



    }



    @Test
    @Transactional
    @WithMockUser(username = "SupervizorUser",authorities = {"supervisor:get"})
    void  shouldgetThePerformanceTableForWorker() throws  Exception{
        User workeruser66 = userRepository.findByFirstName("WorkerUser66");

        Sensor mockSensor = new Sensor();
        mockSensor.setSensorName("Test Sensor 1");
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());

        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(10, 35));
        mockLocation.setLocation(point);
        mockSensor.setSensorLocation(mockLocation);

        Sensor mockSensor2 = new Sensor();
        mockSensor2.setSensorName("Test Sensor 2");
        mockSensor2.setInstallationDate(new Date());
        mockSensor2.setLastUpdatedAt(new Date());

        SensorLocation mockLocation2 = new SensorLocation();
        Point point2 = new GeometryFactory().createPoint(new Coordinate(13, 28));
        mockLocation2.setLocation(point);
        mockSensor2.setSensorLocation(mockLocation2);


        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_YEAR, -6);
        Date sixDaysAgo = calendar2.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedSixDaysAgo = formatter.format(sixDaysAgo);



        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR, -10);
        Date tenDaysAgo = calendar3.getTime();

        Calendar calendarBase = Calendar.getInstance();


        Calendar calendar4 = Calendar.getInstance();
        calendar4.add(Calendar.DAY_OF_YEAR, -14);
        Date forteenDaysAgo = calendar4.getTime();

        Calendar calendarTenHoursAgo  =  calendarBase.getInstance();
        calendarTenHoursAgo .add(Calendar.HOUR, -10);
        Date tenhoursago = calendarTenHoursAgo .getTime();

        Calendar calendarTwelveHoursAgo = (Calendar) calendarBase.clone();
        calendarTwelveHoursAgo.add(Calendar.HOUR, -12);
        Date twelvehoursago = calendarTwelveHoursAgo.getTime();



        SimpleDateFormat formatterTen = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTenDaysAgo = formatter.format(tenDaysAgo);

        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setUser(workeruser66);
        mockSensorFix.setSensor(mockSensor);
        mockSensorFix.setStartTime(tenDaysAgo);
        mockSensorFix.setCompletedTime(sixDaysAgo);

        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setNote("MockatoMock Note");
        mockSensorFix2.setUser(workeruser66);
        mockSensorFix2.setSensor(mockSensor2);
        mockSensorFix2.setStartTime(forteenDaysAgo);
        mockSensorFix2.setCompletedTime(tenDaysAgo);

        SensorFix mockSensorFix3 = new SensorFix();
        mockSensorFix3.setNote("MockatoMock Note");
        mockSensorFix3.setUser(workeruser66);
        mockSensorFix3.setSensor(mockSensor2);
        mockSensorFix3.setStartTime(twelvehoursago);
        mockSensorFix3.setCompletedTime(tenhoursago);

        List<SensorFix> sensorFixList = new ArrayList<>();
        sensorFixList.add(mockSensorFix);
        sensorFixList.add(mockSensorFix2);
        sensorFixList.add(mockSensorFix3);


        workeruser66.setSensorSessions(sensorFixList);

        userRepository.saveAndFlush(workeruser66);
        sensorSessionRepository.saveAndFlush(mockSensorFix);
        sensorSessionRepository.saveAndFlush(mockSensorFix2);


        mockMvc.perform(get("/superVizor/getTheLeaderBoardTable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].averageChartData.WorkerUser66").value(120))
                .andExpect(jsonPath("$[1].totalSensorChartData.WorkerUser66").value(3))
                .andExpect(jsonPath("$[2].last_day.WorkerUser66").value(1))
                .andDo(print());


    }



    @Test
    @Transactional
    @WithMockUser(username = "SupervizorUser",authorities = {"supervisor:write"})
    void  shouldSupervizorCreateTaskForWorker() throws  Exception{


        User assignedTo = new User();
        assignedTo.setFirstName("mockassignedto");
        assignedTo.setSurName("mockassignedtosurname");
        assignedTo.setRole(Role.WORKER);

        userRepository.saveAndFlush(assignedTo);

        User assignedBy = new User();
        assignedBy.setFirstName("MockSupervizorAssignedBy");
        assignedBy.setSurName("MockSupervizorAssignedBySurname");
        assignedBy.setRole(Role.SUPERVISOR);

        userRepository.saveAndFlush(assignedBy);


        Sensor sensor = new Sensor();
        sensor.setStatus(SensorStatus.ACTIVE);
        sensor.setSensorName("Sensor-1");


        SensorLocation mockLocation2 = new SensorLocation();
        Point point2 = new GeometryFactory().createPoint(new Coordinate(13, 28));
        mockLocation2.setLocation(point2);
        sensor.setSensorLocation(mockLocation2);

        sensorRepository.saveAndFlush(sensor);

        when(userServicee.findByUsername("SupervizorUser")).thenReturn(assignedBy);
        when(userService.findById(assignedTo.getId())).thenReturn(assignedTo);


        Task mockTask = new Task();
        mockTask.setAssignedTo(assignedTo);
        mockTask.setSensor(sensor);
        mockTask.setWorkerArrived(false);
        mockTask.setWorkerArriving(true);


        mockMvc.perform(post("/superVizor/createTask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mockTask)))
                .andExpect(jsonPath("$.assignedBy.firstName").value(assignedBy.getFirstName()))
                .andExpect(jsonPath("$.workerArriving").value(mockTask.getWorkerArriving()))
                .andExpect(jsonPath("$.workerArrived").value(mockTask.getWorkerArrived()))

                .andDo(print());


    }

    //bu olmadı kalsın bir
    @Test
    @Transactional
    @WithMockUser(username = "SupervisorUserMock",authorities = {"supervisor:get"})
    //bu bir kalsın
        void  shouldSupervizorGetTasksOfIAssigned() throws  Exception{


            User assignedTo = new User();
            assignedTo.setFirstName("mockassignedto");
            assignedTo.setSurName("mockassignedtosurname");
            assignedTo.setRole(Role.WORKER);

            userRepository.save(assignedTo);

            User assignedBy = new User();
            assignedBy.setFirstName("SupervisorUserMock");
            assignedBy.setSurName("SupervisorSurname");
            assignedBy.setEmail("supervisor@example.com");
            assignedBy.setRole(Role.SUPERVISOR);

            userRepository.save(assignedBy);

            Sensor sensor = new Sensor();
            sensor.setStatus(SensorStatus.ACTIVE);
            sensor.setSensorName("Sensor-1");

            SensorLocation mockLocation2 = new SensorLocation();
            Point point2 = new GeometryFactory().createPoint(new Coordinate(13, 28));
            mockLocation2.setLocation(point2);
            sensor.setSensorLocation(mockLocation2);

            sensor = sensorRepository.saveAndFlush(sensor);

            Task mockTask = new Task();
            mockTask.setAssignedTo(assignedTo);
            mockTask.setAssignedBy(assignedBy);
            mockTask.setSensor(sensor);
            mockTask.setWorkerArrived(false);
            mockTask.setWorkerArriving(true);
            taskRepositoryy.save(mockTask);

            assignedBy.setTasksIAssigned(List.of(mockTask));

            when(userRepository.findByFirstName(assignedBy.getFirstName())).thenReturn(assignedBy);


 userRepository.save(assignedBy);




            mockMvc.perform(get("/supervizor/getTasksOfIAssigned"))
    //                .andExpect(jsonPath("$.assignedBy.firstName").value(assignedBy.getFirstName()))
    //                .andExpect(jsonPath("$.workerArriving").value(mockTask.getWorkerArriving()))
    //                .andExpect(jsonPath("$.workerArrived").value(mockTask.getWorkerArrived()))

                    .andDo(print());


        }

    @Test
    @Transactional
    @WithMockUser(username = "SupervizorUser",authorities = {"supervisor:get"})
        //bu bir kalsın
    void  shouldGetAllAvailableSensorsForAssigningTaskSelectComponent() throws  Exception{
        Sensor sensor = new Sensor();
        sensor.setStatus(SensorStatus.ACTIVE);
        sensor.setSensorName("Sensor-mock-for-test");


        sensor = sensorRepository.saveAndFlush(sensor);


        mockMvc.perform(get("/supervizor/getAllAvailableSensorsForAssigningTaskSelectComponent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].sensorName",
                        hasItems(sensor.getSensorName())))
                .andDo(print());


    }


    @Test
    @Transactional
    @WithMockUser(username = "SupervizorUser",authorities = {"supervisor:get"})
    void  shouldCreatePdfReportAndSendToManager() throws  Exception{


        Sensor mockSensor = new Sensor();
        mockSensor.setSensorName("Test Sensor 1");
        mockSensor.setInstallationDate(new Date());
        mockSensor.setLastUpdatedAt(new Date());
        sensorRepository.saveAndFlush(mockSensor);

        SensorLocation mockLocation2 = new SensorLocation();
        Point point2 = new GeometryFactory().createPoint(new Coordinate(41.0082, 28.9784));
        mockLocation2.setLocation(point2);

        mockSensor.setSensorLocation(mockLocation2);


        User managerUser = userRepository.findById(30L).get();

        User supervizorUser = userRepository.findById(3L).get();



        PdfRequestDTO pdfRequestDTO = new PdfRequestDTO();
        pdfRequestDTO.setNote("Acme Su Deposu İçin Kurulum");
        pdfRequestDTO.setStartTime("2023-05-15T09:00:00");
        pdfRequestDTO.setCompletedTime("2023-05-15T12:30:00");
        pdfRequestDTO.setLatitude(41.0082);
        pdfRequestDTO.setLongitude(28.9784);
        pdfRequestDTO.setManagerId(managerUser.getId());


        pdfRequestDTO.setSupervizorId(supervizorUser.getId());
        pdfRequestDTO.setOriginalSensorId(mockSensor.getId());
        pdfRequestDTO.setSensorName(mockSensor.getSensorName());
        pdfRequestDTO.setNote("Test Note");


        //integrasyon testleri mocklama yapmamalı biliyorum ama bu kısımda normalde supervizorü bulmasına rağmen testte
        //             Body = Report Couldn be createdCannot invoke "com.example.EcoTrack.user.model.User.getFirstName()" because the return value of "com.example.EcoTrack.pdfReports.model.PdfReports.getSupervisor()" is null erorü alıyorum

        when( userService.findById(pdfRequestDTO.getSupervizorId())) .thenReturn(supervizorUser);

        mockMvc.perform((MockMvcRequestBuilders.multipart("/supervizor/createPdfReportAndSendToManager")
                .param("originalSensorId", pdfRequestDTO.getOriginalSensorId().toString())
                .param("sensorName", pdfRequestDTO.getSensorName().toString())
                .param("note", pdfRequestDTO.getNote())
                .param("startTime", pdfRequestDTO.getStartTime())
                .param("completedTime", pdfRequestDTO.getCompletedTime())
                .param("latitude", String.valueOf(pdfRequestDTO.getLatitude()))
                .param("longitude", String.valueOf(pdfRequestDTO.getLongitude()))
                .param("managerId", managerUser.getId().toString())
                        .param("supervizorId", supervizorUser.getId().toString())
                .contentType(MediaType.MULTIPART_FORM_DATA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sensor.sensorName" ).value(mockSensor.getSensorName()))
                .andExpect(jsonPath("$.technicianNote" ).value(pdfRequestDTO.getNote()))
                .andExpect(jsonPath("$.latitude" ).value(mockSensor.getSensorLocation().getLocation().getX()))
                .andExpect(jsonPath("$.longitude" ).value(mockSensor.getSensorLocation().getLocation().getY()))
                .andDo(print());


    }


    
    //burası çalışmıyor daha sonra bakacğaım
    @Test
    @Transactional
    @WithMockUser(username = "SupervizorUser",authorities = {"supervisor:get"})
    void  shouldGetAllManager() throws  Exception{

        User mockManager = new User();
        mockManager.setFirstName("Mock Manager");
        mockManager.setSurName("managersurnamemock");
        mockManager.setRole(Role.MANAGER);


        UserOnlineStatus userOnlineStatus = new UserOnlineStatus();
        userOnlineStatus.setId(mockManager.getId());
        userOnlineStatus.setIsOnline(true);
        userOnlineStatus.setLastOnlineTime(LocalDateTime.now());
        userOnlineStatus.setCreatedAt(new Date());


        mockManager.setUserOnlineStatus(userOnlineStatus);
        userOnlineStatus.setUser(mockManager);


        User savedManager = userRepository.saveAndFlush(mockManager);

        UserOnlineStatus savedOnlineStatus = userOnlineStatusRepository.saveAndFlush(userOnlineStatus);


        System.out.println(savedManager.getFirstName());
        System.out.println(savedOnlineStatus.getUser().getFirstName());

        mockMvc.perform(get("/supervizor/getAllManager"))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[*].sensorName",
//                        hasItems(sensor.getSensorName())))
                .andDo(result -> {
                    System.out.println("Response Body: " + result.getResponse().getContentAsString());
                });

    }



}

