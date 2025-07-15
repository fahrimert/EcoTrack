package com.example.EcoTrack.Tests.SuperVizorBasedTests;

import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.manager.service.ManagerService;
import com.example.EcoTrack.pdfReports.dto.PdfRequestDTO;
import com.example.EcoTrack.pdfReports.model.PdfReports;
import com.example.EcoTrack.pdfReports.service.PdfReportService;
import com.example.EcoTrack.security.config.SecurityConfig;
import com.example.EcoTrack.security.customUserDetail.CustomUserDetailService;
import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.sensors.model.SensorLocation;
import com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.shared.dto.SensorDTO;
import com.example.EcoTrack.shared.dto.SensorFixDTO;
import com.example.EcoTrack.shared.dto.SensorLocationDTO;
import com.example.EcoTrack.supervizor.controller.SuperVizorBasedController;
import com.example.EcoTrack.supervizor.dto.SensorCountDTO;
import com.example.EcoTrack.supervizor.dto.SensorDateCountDTO;
import com.example.EcoTrack.supervizor.service.SuperVizorService;
import com.example.EcoTrack.task.dto.SensorTaskDTO;
import com.example.EcoTrack.task.dto.TaskDTO;
import com.example.EcoTrack.task.dto.UserTaskDTO;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO;
import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.model.UserOnlineStatus;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SuperVizorBasedController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = true)
public class    SuperVizorControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

        @MockitoBean
    private SuperVizorService superVizorService;


    @MockitoBean
    private PdfReports pdfReports;


    @MockitoBean
    private PdfReportService pdfReportService;


    @MockitoBean
    private ManagerService managerService;


    @MockitoBean
    private UserService userService;


//    @Test
//    @WithMockUser(authorities = "supervisor:get")
//    void shouldgetMetricsValuesForDoughnutComponent() throws  Exception{
//
//                Map<SensorStatus,Long> statusCounts = new HashMap<>();
//                statusCounts.put(SensorStatus.ACTIVE,2L);
//                statusCounts.put(SensorStatus.FAULTY,1L);
//
//        when(superVizorService.getAllSensorStatusMetricValuesForDoughnutComponent()).thenReturn(ResponseEntity.ok(statusCounts));
//        ResponseEntity<Map<SensorStatus,Long>>  result = controller.getAllSensorStatusMetricValuesForDoughnutComponent();
//
//        assertEquals(statusCounts, result.getBody());
//    }
    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private GeometryFactory geometryFactory;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(authorities = "supervisor:get")
    void shouldReturnSuccessWhenGetReturnMetricsValuesForDougnhutComponent() throws  Exception{

        Map<SensorStatus,Long> statusCounts = new HashMap<>();
        statusCounts.put(SensorStatus.ACTIVE,2L);
        statusCounts.put(SensorStatus.FAULTY,1L);


        mockMvc.perform(get("/superVizor/getAllSensorStatusMetricValuesForDoughnutComponent"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(authorities = "supervisor:put")
    void shouldReturnForbiddenWhenGetReturnMetricsValuesForDougnhutComponent() throws  Exception{

        Map<SensorStatus,Long> statusCounts = new HashMap<>();
        statusCounts.put(SensorStatus.ACTIVE,2L);
        statusCounts.put(SensorStatus.FAULTY,1L);


        mockMvc.perform(get("/superVizor/getAllSensorStatusMetricValuesForDoughnutComponent"))
                .andExpect(status().isForbidden());
    }



    @Test
    @WithMockUser(authorities = "supervisor:get")
    void shouldReturnSuccess_WhenGetReturnMetricsValuesFor_BarChartComponent() throws  Exception{
        List<Map<String, Map<String, Long>>> result = new ArrayList<>();


        Map<String,   Map<String, Long>> weekdata = new HashMap<>();
        Map<String, Map<String, Long> > monthData = new HashMap<>();

        Map<String, Map<String, Long> > dailydata = new HashMap<>();

        Map<String,Long> dailyDataWorkerMockStat  = new HashMap<>();
        dailyDataWorkerMockStat.put("Mert",13L);
        dailyDataWorkerMockStat.put("Ahmet",14L);
        dailyDataWorkerMockStat.put("Buğra",15L);

        Map<String,Long> weeeklyDataWorkerMockStat  = new HashMap<>();
        weeeklyDataWorkerMockStat.put("Mert",18L);
        weeeklyDataWorkerMockStat.put("Ahmet",32L);
        weeeklyDataWorkerMockStat.put("Buğra",45L);

        Map<String,Long> monthlyDataWorkerMockStat  = new HashMap<>();
        monthlyDataWorkerMockStat.put("Mert",50L);
        monthlyDataWorkerMockStat.put("Ahmet",100L);
        monthlyDataWorkerMockStat.put("Buğra",200L);


        monthData.put("last_month",monthlyDataWorkerMockStat);

        weekdata.put("last_week",weeeklyDataWorkerMockStat);

        dailydata.put("last_day",dailyDataWorkerMockStat);
        result.add(monthData);

        result.add(weekdata);
        result.add(dailydata);


        result.add(Map.of("last_day", dailyDataWorkerMockStat));
        result.add(Map.of("last_week", weeeklyDataWorkerMockStat));
        result.add(Map.of("last_month", monthlyDataWorkerMockStat));


        when(superVizorService.getTimeBasedSessionWorkerStatsForBarChartData())
                .thenReturn(result);

      mockMvc.perform(get("/superVizor/getTimeBasedSessionWorkerStatsForBarChartData"))
              .andDo(print())
                .andExpect(status().isOk())
             .andExpect(jsonPath("$[0].last_month.Mert").value(50))
                .andExpect(jsonPath("$[1].last_week.Ahmet").value(32))
                .andExpect(jsonPath("$[2].last_day.Buğra").value(15));
    }

    @Test
    @WithMockUser(authorities = "supervisor:get")
    void shouldReturnSuccess_getFaultyLocationsForSupervizorDashboardHeatmapComponent() throws  Exception{
        SensorLocationDTO sensorLocationDTO = new SensorLocationDTO();
        sensorLocationDTO.setId(10L);
        sensorLocationDTO.setLatitude(14.0);
        sensorLocationDTO.setLongitude(25.0);

        List<SensorLocationDTO> sensorLocationDTOS = List.of(sensorLocationDTO);


        when(superVizorService.getFaultyLocationsForSupervizorDashboardHeatmapComponent())
                .thenReturn(sensorLocationDTOS);

        mockMvc.perform(get("/superVizor/getFaultyLocationsForSupervizorDashboardHeatmapComponent"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].latitude").value(14.0))
                .andExpect(jsonPath("$[0].longitude").value(25.0));

    }

    @Test
    @WithMockUser(authorities = "supervisor:get")
    void shouldReturnSuccess_getAllWorkers() throws  Exception {

        UserOnlineStatusDTO userOnlineStatusDTO2 = new UserOnlineStatusDTO();
        userOnlineStatusDTO2.setId(15L);
        userOnlineStatusDTO2.setFirstName("Mock User 2");
        userOnlineStatusDTO2.setSurName("Mock Surname 2");
        userOnlineStatusDTO2.setRole(Role.WORKER);


        when(superVizorService.getAllWorker()).thenReturn(List.of(userOnlineStatusDTO2));

        mockMvc.perform(get("/supervizor/getAllWorker"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(15L))
                .andExpect(jsonPath("$[0].firstName").value("Mock User 2"))
                .andExpect(jsonPath("$[0].surName").value("Mock Surname 2"))
                .andExpect(jsonPath("$[0].role").value(Role.WORKER.getDisplayName()));
    }


    @Test
    @WithMockUser(authorities = "supervisor:get")
    void shouldReturnSuccess_getScatterPlotGraphDataOfWorkerTasks() throws  Exception {
        Long mockUserId = 1L;


        Map<String,Object> formatted = new HashMap<>();
        formatted.put("formatted",
                "01 gün 12 saat 20 dakika 25 saniye");

        Map<Long, Map<String, Object>> mockCompletedLastTime  = new HashMap<>();
        mockCompletedLastTime.put(1L,formatted);

        when(superVizorService.getSensorSessionsOfLastMonth(mockUserId)).thenReturn(mockCompletedLastTime);

        mockMvc.perform(get("/supervizor/getScatterPlotGraphDataOfWorkerTasks/{userId}",1L))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(authorities = "supervisor:get")
    void shouldReturnSuccess_getNonTaskSessionSolvingSensorNames() throws  Exception {
        Long mockUserId = 1L;

        User mockUser = new User();
        mockUser.setId(mockUserId);
        mockUser.setFirstName("mock mock");
        when(userService.findById(mockUserId)).thenReturn(mockUser);

        Map<String,Long> nameMockCounts = new HashMap<>();
        nameMockCounts.put("sensor 3",15L);
        nameMockCounts.put("sensor 5",20L);


        List<SensorCountDTO>  mockNameCounts = nameMockCounts.entrySet().stream().map(a -> new SensorCountDTO(a.getKey(),a.getValue())).collect(Collectors.toList());
        when(superVizorService.getNonTaskSessionSolvingSensorNames(mockUserId)).thenReturn(mockNameCounts);

        mockMvc.perform(get("/supervizor/getNonTaskSessionSolvingSensorNames/{userId}",1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                 .andExpect(jsonPath("$[0].name").value("sensor 5"))
                .andExpect(jsonPath("$[0].count").value(20))
                .andExpect(jsonPath("$[1].name").value("sensor 3"))
                .andExpect(jsonPath("$[1].count").value(15));
    }

    @Test
    @WithMockUser(authorities = "supervisor:get")
    void shouldReturnSuccess_getDatesAndBasedOnTheirSessionCounts() throws  Exception {
        Long mockUserId = 1L;

        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(100L);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);

        Map<LocalDate, Long> countsByDate = new HashMap<>();
        countsByDate.put(LocalDate.of(2025,10,20),10L);
        countsByDate.put(LocalDate.of(2025,5,10),2L);
        List<SensorDateCountDTO> mockSensorDateCount =   countsByDate.entrySet().stream()
                .map(entry -> new SensorDateCountDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());


        when(superVizorService.getDatesAndBasedOnTheirSessionCounts(mockUserId)).thenReturn(mockSensorDateCount);

        mockMvc.perform(get("/superVizorSensors/getSensorDatesAndSessionCounts/{userId}",mockUserId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value("2025-05-10"))
                .andExpect(jsonPath("$[0].count").value(2))
                .andExpect(jsonPath("$[1].date").value("2025-10-20"))
                .andExpect(jsonPath("$[1].count").value(10));
    }


    @Test
    @WithMockUser(authorities = "supervisor:get")
    void shouldReturnSuccess_getNonTaskHeatmapComponentForSupervizorDashboardPage() throws  Exception {
        Long mockUserId = 1L;

        User mockUser = new User();
        mockUser.setId(mockUserId);
        mockUser.setFirstName("mock mock");
        when(userService.findById(mockUserId)).thenReturn(mockUser);

        SensorLocationDTO sensorLocationDTO = new SensorLocationDTO();
        sensorLocationDTO.setId(10L);
        sensorLocationDTO.setLatitude(14.0);
        sensorLocationDTO.setLongitude(25.0);

        List<SensorLocationDTO> sensorLocationDTOS = List.of(sensorLocationDTO);


        when(superVizorService.getNonTaskHeatmapComponentForSupervizorDashboardPage(mockUserId))
                .thenReturn(sensorLocationDTOS);

        mockMvc.perform(get("/supervizor/getNonTaskHeatmapComponent/{userId}",mockUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].latitude").value(14.0))
                .andExpect(jsonPath("$[0].longitude").value(25.0));
        ;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    @WithMockUser(authorities = "supervisor:write")
    void supervizorShouldCreateTaskSuccessfully() throws  Exception {
        Long userId = 1L;
        Long sensorId = 5L;

        User assignedBy = new User();
        assignedBy.setId(10L);
        assignedBy.setFirstName("mockassignedby");
        assignedBy.setSurName("mockassignedbysurname");

        User assignedTo = new User();
        assignedTo.setId(15L);
        assignedBy.setFirstName("mockassignedto");
        assignedBy.setSurName("mockassignedtosurname");

        Sensor sensor = new Sensor();
        sensor.setId(sensorId);
        sensor.setStatus(SensorStatus.ACTIVE);
        sensor.setSensorName("Sensor-1");
        Point location = geometryFactory.createPoint(new Coordinate(41.0, 29.0));
        SensorLocation sensorLocation = new SensorLocation();
        sensorLocation.setLocation(location);

        sensor.setSensorLocation(sensorLocation);



        Task mockTask = new Task();
        mockTask.setAssignedBy(assignedBy);
        mockTask.setAssignedTo(assignedTo);

        when(userService.findByUsername("supervizor")).thenReturn(assignedBy);
            TaskDTO taskDTO = TaskDTO.builder()
                .Id(100L)
                .superVizorDescription("Sensor calibration needed")
                .superVizorDeadline(LocalDateTime.now().plusDays(2))
                .assignedTo(UserTaskDTO.builder()
                        .id(1L)
                        .firstName("Mehmet")
                        .surName("Yılmaz")
                        .build())
                .assignedBy(UserTaskDTO.builder()
                        .id(2L)
                        .firstName("Ayşe")
                        .surName("Demir")
                        .build())
                .sensorDTO(SensorTaskDTO.builder()
                        .id(10L)
                        .sensorName("Humidity Sensor-5")
                        .latitude(40.7128)
                        .longitude(-74.0060)
                        .build())
                .workerArriving(false)
                .workerArrived(false)

                .build();
        when(superVizorService.supervizorCreateTaskForWorker(any())).thenReturn(ResponseEntity.ok(taskDTO));

        mockMvc.perform(post("/superVizor/createTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockTask)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.superVizorDescription").value("Sensor calibration needed"))
                .andExpect(jsonPath("$.assignedTo.id").value(userId))
        ;
    }

    @Test
    @WithMockUser(authorities = "supervisor:get")
    void supervizorGetTasksOfIAssigned() throws  Exception {
        Long mockUserId = 1L;
        Long sensorId = 5L;
        Long mockTaskId = 4L;



        Task mockTask = new Task();
        mockTask.setId(mockTaskId);
        mockTask.setWorkerArriving(false);

        when(userService.getAllTask(mockUserId)).thenReturn(List.of(mockTask));


        TaskDTO taskDTO = TaskDTO.builder()
                .Id(100L)
                .superVizorDescription("Sensor calibration needed")
                .superVizorDeadline(LocalDateTime.now().plusDays(2))
                .assignedTo(UserTaskDTO.builder()
                        .id(1L)
                        .firstName("Mehmet")
                        .surName("Yılmaz")
                        .build())
                .assignedBy(UserTaskDTO.builder()
                        .id(2L)
                        .firstName("Ayşe")
                        .surName("Demir")
                        .build())
                .sensorDTO(SensorTaskDTO.builder()
                        .id(10L)
                        .sensorName("Humidity Sensor-5")
                        .latitude(40.7128)
                        .longitude(-74.0060)
                        .build())
                .workerArriving(false)
                .workerArrived(false)

                .build();
        when(superVizorService.getTasksOfIAssigned()).thenReturn(ResponseEntity.ok(List.of(taskDTO)));

        mockMvc.perform(get("/supervizor/getTasksOfIAssigned"))
                .andDo(print())
        ;
    }

    @Test
    @WithMockUser(authorities = "supervisor:get")
    void supervizorGetAllAvailableSensors() throws  Exception {
        List<SensorDTO> mockSensorDTOList = List.of((SensorDTO.builder()
                .id(10L)
                .sensorName("c")
                .status(String.valueOf(SensorStatus.ACTIVE))
                .color_code("color")
                .latitude(40.32)
                .longitude(30.32)
                .currentSensorSession(SensorFixDTO.builder()
                        .id(20L)
                        .sensorName("mock")
                        .displayName(SensorStatus.ACTIVE.getDisplayName())
                        .color_code("color")
                        .note("Mock Note")
                        .startTime(new Date())
                        .completedTime(new Date(System.currentTimeMillis() + 3600000))
                               .latitude(40.32)
                        .longitude(30.32)
                        .build()).build())) ;





        when(superVizorService.getAllAvailableSensorsForAssigningTaskSelectComponent()).thenReturn(mockSensorDTOList);

        mockMvc.perform(get("/supervizor/getAllAvailableSensorsForAssigningTaskSelectComponent"))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].sensorName").value("c"))
        ;
    }

    @Test
    @WithMockUser(authorities = "supervisor:post")
    void supervizorcreatePdfReportAndSendToManager() throws  Exception {

        PdfRequestDTO request = new PdfRequestDTO();
        request.setOriginalSensorId(10L);
        request.setSensorName("Test Sensor");
        request.setManagerId(10L);
        request.setSupervizorId(15L);

        User mockManager = new User();
        mockManager.setId(10L);
        mockManager.setFirstName("manager");

        User mockSupervizor = new User();
        mockSupervizor.setId(15L);
        mockSupervizor.setFirstName("supervizor");

        when(userService.findById(mockManager.getId())).thenReturn(mockManager);
        when(userService.findById(mockSupervizor.getId())).thenReturn(mockSupervizor);


        PdfReports pdfReports = new PdfReports();

        pdfReports.setId(1L); // ID set edildi
        pdfReports.setSensorName("Test Sensor"); // Diğer gerekli alanlar



        when(pdfReportService.createPdfAndSendNotificationToManager(any())).thenReturn(ResponseEntity.ok(pdfReports));

        // şunun testi lazımmı acaba sendNotificationToManager(notificationn,createdPdfReport.getId());

        mockMvc.perform(post("/supervizor/createPdfReportAndSendToManager").contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
        ;
    }

    @Test
    @WithMockUser(authorities = "supervisor:get")
    void getAllManager() throws  Exception {
        User mockUser = new User();
        mockUser.setId(15L);
        mockUser.setFirstName("mock mock");

        when(userService.getAllManagers()).thenReturn(List.of(UserOnlineStatusDTO.builder()
                .id(15L)
                .firstName("Mock Manager")
                .surName("Manager Surname")
                .role(Role.MANAGER)
                .userOnlineStatus(UserOnlineStatus.builder()
                        .id(15L)
                        .isOnline(true)
                        .user(mockUser)
                        .lastOnlineTime(LocalDateTime.now())
                        .createdAt(new Date())
                        .build()
                )
                .build()
        ));
        mockMvc.perform(get("/supervizor/getAllManager").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].id").value(15))
                .andExpect(jsonPath("$[0].role").value(Role.MANAGER.getDisplayName()))

                .andExpect(jsonPath("$[0].firstName").value("Mock Manager"));
    }


}
