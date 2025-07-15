package com.example.EcoTrack.Tests.ManagerTests;

import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.manager.controller.ManagerController;
import com.example.EcoTrack.manager.dto.TaskCountDTO;
import com.example.EcoTrack.manager.service.ManagerService;
import com.example.EcoTrack.notification.dto.ManagerNotificationDTO;
import com.example.EcoTrack.notification.dto.SinglePdfReportDTO;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.pdfReports.dto.PdfRequestDTO;
import com.example.EcoTrack.pdfReports.model.PdfReports;
import com.example.EcoTrack.pdfReports.service.PdfReportService;
import com.example.EcoTrack.security.config.SecurityConfig;
import com.example.EcoTrack.security.customUserDetail.CustomUserDetailService;
import com.example.EcoTrack.sensors.dto.AllSensorForManagerDTO;
import com.example.EcoTrack.sensors.dto.CreateSensorLocationDTO;
import com.example.EcoTrack.sensors.dto.CreateSensorLocationRequestDTO;
import com.example.EcoTrack.sensors.dto.SensorDetailForManagerDTO;
import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorIconImage;
import com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.shared.dto.ImageResponseDTO;
import com.example.EcoTrack.supervizor.service.SuperVizorService;
import com.example.EcoTrack.user.dto.UserAndSupervizorsDTO;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO;
import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagerController.class)
@Import({SecurityConfig.class })
@AutoConfigureMockMvc(addFilters = true)

public class ManagerControllerTest {


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
    @MockitoBean
    private SensorService sensorService;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    @WithMockUser(authorities = "manager:get")

    void  getSuperVizorTasks() throws  Exception {

        List<Map<String, Map<String, Long>>> result = new ArrayList<>();


        Map<String, Map<String, Long>> weekdata = new HashMap<>();
        Map<String, Map<String, Long>> monthData = new HashMap<>();

        Map<String, Map<String, Long>> dailydata = new HashMap<>();

        Map<String, Long> dailyDataSupervizorMockStat = new HashMap<>();
        dailyDataSupervizorMockStat.put("Supervizor1", 13L);
        dailyDataSupervizorMockStat.put("Supervizor2", 14L);
        dailyDataSupervizorMockStat.put("Supervizor3", 15L);

        Map<String, Long> weeeklyDataSupervizorMockStat = new HashMap<>();
        weeeklyDataSupervizorMockStat.put("Supervizor1", 18L);
        weeeklyDataSupervizorMockStat.put("Supervizor2", 32L);
        weeeklyDataSupervizorMockStat.put("Supervizor3", 45L);

        Map<String, Long> monthlyDataSupervizorMockStat = new HashMap<>();
        monthlyDataSupervizorMockStat.put("Supervizor1", 50L);
        monthlyDataSupervizorMockStat.put("Supervizor2", 100L);
        monthlyDataSupervizorMockStat.put("Supervizor3", 200L);


        monthData.put("last_month", monthlyDataSupervizorMockStat);

        weekdata.put("last_week", weeeklyDataSupervizorMockStat);

        dailydata.put("last_day", dailyDataSupervizorMockStat);
        result.add(monthData);

        result.add(weekdata);
        result.add(dailydata);


        result.add(Map.of("last_day", dailyDataSupervizorMockStat));
        result.add(Map.of("last_week", weeeklyDataSupervizorMockStat));
        result.add(Map.of("last_month", monthlyDataSupervizorMockStat));

        when(managerService.getSuperVizorTasks())
                .thenReturn(result);

        mockMvc.perform(get("/manager/getSuperVizorTasks"))
                .andDo(resultt -> {
                    System.out.println("Response body: " + resultt.getResponse().getContentAsString());
                })
                .andExpect(jsonPath("$[0].last_month.Supervizor2").value(100))
                .andExpect(jsonPath("$[1].last_week.Supervizor1").value(18))
                .andExpect(jsonPath("$[2].last_day.Supervizor2").value(14))
                .andExpect(status().isOk());
        verify(managerService, times(1)).getSuperVizorTasks();
    }

        @Test
        @WithMockUser(authorities = "manager:get")
        void getAllSupervizorAssignedTaskNamesValuesForDoughnutComponent() throws  Exception{

            Map<SensorStatus,Long> statusCounts = new HashMap<>();
            statusCounts.put(SensorStatus.ACTIVE,2L);
            statusCounts.put(SensorStatus.FAULTY,1L);

            TaskCountDTO mocktaskCountDTO = new TaskCountDTO();
            mocktaskCountDTO.setName(SensorStatus.ACTIVE);
            mocktaskCountDTO.setCount(10L);

            TaskCountDTO mocktaskCountDTOsecond = new TaskCountDTO();
            mocktaskCountDTOsecond.setName(SensorStatus.ACTIVE);
            mocktaskCountDTOsecond.setCount(10L);

            when(managerService.getSensorNameCounts())
                    .thenReturn(List.of(mocktaskCountDTO,mocktaskCountDTOsecond));

            mockMvc.perform(get("/manager/getAllAssignedTaskStatusValuesForDoughnutComponent"))
                    .andDo(resultt -> {
                        System.out.println("Response body: " + resultt.getResponse().getContentAsString());})
                    .andExpect(jsonPath("$[0].name").value("ACTIVE"))
                    .andExpect(jsonPath("$[0].count").value(10))
                    .andExpect(status().isOk());
        }


        @Test
        @WithMockUser(authorities = "manager:get")
        void shouldReturnSuccess_getScatterPlotGraphDataOfWorkerTasks() throws  Exception {


            Map<String,Long> formatted = new HashMap<>();
            formatted.put("NewUserr",
                   0L);

            Map<String, Map<String, Long>> mockCompletedLastTime  = new HashMap<>();
            mockCompletedLastTime.put("averageChartData",formatted);

            when(managerService.getTheAverageTaskCompletedTimeForWorkerChart()).thenReturn(mockCompletedLastTime);

            mockMvc.perform(get("/manager/getAverageTaskMinsOfLastMonth"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }



        @Test
        @WithMockUser(authorities = "manager:delete")
        void shouldDeletUserByID() throws  Exception{
            User mockUser = new User();
            mockUser.setId(10L);
            mockUser.setIsActive(false);
            mockUser.setFirstName("test");

            when(userService.deleteUserById(mockUser.getId())).thenReturn( ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(
                            "User Successfully deleted"
                    )));


            mockMvc.perform(delete("/manager/deleteUserById/{userId}",mockUser.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data").value("User Successfully deleted"))            ;

        }

    @Test
    @WithMockUser(authorities = "manager:put")
    void shouldDectivateUserByID() throws  Exception{
        User mockUser = new User();
        mockUser.setId(10L);
        mockUser.setIsActive(true);
        mockUser.setFirstName("test");

        when(userService.deactivateUserById(mockUser.getId())).thenReturn( ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "User Successfully deactivated"
                )));


        mockMvc.perform(put("/manager/deactivateUser/{userId}",mockUser.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("User Successfully deactivated"))            ;
        ;

    }

    @Test
    @WithMockUser(authorities = "manager:put")
    void shouldNotDectivateUserByID() throws  Exception{
        User mockUser = new User();
        mockUser.setId(10L);
        mockUser.setIsActive(false);
        mockUser.setFirstName("test");

        when(userService.deactivateUserById(mockUser.getId())).thenReturn( ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        "Error",
                        List.of("User already deactivated"),
                        HttpStatus.CONFLICT
                )));


        mockMvc.perform(put("/manager/deactivateUser/{userId}",mockUser.getId()))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[0]").value("User already deactivated"))            ;
        ;

    }

    @Test
    @WithMockUser(authorities = "manager:put")
    void shouldNotDectivateUserByIDBecauseOfUserNotFound() throws  Exception{
        User mockUser = new User();
        mockUser.setId(10L);
        mockUser.setIsActive(false);
        mockUser.setFirstName("test");
        when(userService.findById(mockUser.getId())).thenReturn(null);

        when(userService.deactivateUserById(mockUser.getId())).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        "Error",
                        List.of("No user Found"),
                        HttpStatus.NOT_FOUND
                )));


        mockMvc.perform(put("/manager/deactivateUser/{userId}",mockUser.getId()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[0]").value("No user Found"))
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.errors[0]").value("User already deactivated"))            ;
        ;

    }

    @Test
    @WithMockUser(authorities = "manager:get")
    void shouldReturnSupervizorPerformanceCharts() throws  Exception{
        List<Map<String, Map<String, Long>>> result = new ArrayList<>();

        Map<String,   Map<String, Long>> averageTimeChartData = new HashMap<>();
        Map<String, Map<String, Long> > totalSensorChartData = new HashMap<>();
        Map<String, Map<String, Long> > dailydata = new HashMap<>();

        long sessionCountOfMertMock = 10;
        long sessionCountOfAhmetMock = 20;
        long sessionCountOfBuğraMock = 15;



        Map<String,Long> monthlyDataWorkerMockStat  = new HashMap<>();
        monthlyDataWorkerMockStat.put("Mert",50L/ (sessionCountOfMertMock * (60 * 1000)));
        monthlyDataWorkerMockStat.put("Ahmet",100L/ (sessionCountOfAhmetMock * (60 * 1000)));
        monthlyDataWorkerMockStat.put("Buğra",200L/ (sessionCountOfBuğraMock * (60 * 1000)));


        averageTimeChartData.put("SuperVizorGivenCompletedTasksAverageMinLastMonth",monthlyDataWorkerMockStat);

        Map<String,Long> superVizorGivenTasksTotalCount  = new HashMap<>();

        superVizorGivenTasksTotalCount.put("Mert",sessionCountOfMertMock);
        superVizorGivenTasksTotalCount.put("Ahmet",sessionCountOfAhmetMock);
        superVizorGivenTasksTotalCount.put("Buğra",sessionCountOfBuğraMock);
        totalSensorChartData.put("SupervizorGivenTasksTotalCount",superVizorGivenTasksTotalCount);

        Map<String,Long> dailyDataWorkerMockStat  = new HashMap<>();
        dailyDataWorkerMockStat.put("Mert",20L);
        dailyDataWorkerMockStat.put("Ahmet",12L);
        dailyDataWorkerMockStat.put("Buğra",5L);



        dailydata.put("SupervizorGivenTaskCountOfLastDay",dailyDataWorkerMockStat);

        result.add(averageTimeChartData);

        result.add(totalSensorChartData);
        result.add(dailydata);

        when(managerService.getThePerformanceTableForSupervizor()).thenReturn(result);


        mockMvc.perform(get("/manager/getTheSupervizorPerformanceCharts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].SuperVizorGivenCompletedTasksAverageMinLastMonth.Buğra").value(0))
                .andExpect(jsonPath("$[1].SupervizorGivenTasksTotalCount.Buğra").value(15))
                .andExpect(jsonPath("$[2].SupervizorGivenTaskCountOfLastDay.Buğra").value(5))
        ;




    }

    @Test
    @WithMockUser(authorities = "manager:get")
    void shouldReturnAllSupervizors() throws  Exception{
        List<User> userList = new ArrayList<>();
        User mockSupervizor = new User();
        mockSupervizor.setId(15L);
        mockSupervizor.setFirstName("supervizormock");
        mockSupervizor.setRole(Role.SUPERVISOR);

        User mockSupervizor2 = new User();
        mockSupervizor2.setId(20L);
        mockSupervizor2.setFirstName("supervizormock2");
        mockSupervizor2.setRole(Role.SUPERVISOR);

        userList.add(mockSupervizor);
        userList.add(mockSupervizor2);

        List<UserOnlineStatusDTO> dtoList = userList.stream()
                .map(userItem -> {

                    UserOnlineStatusDTO dto = new UserOnlineStatusDTO();
                    dto.setId(userItem.getId());
                    dto.setFirstName(userItem.getFirstName());
                    dto.setSurName(userItem.getSurName());
                    dto.setRole(userItem.getRole());
                    dto.setUserOnlineStatus(userItem.getUserOnlineStatus());
                    return dto;
                })
                .collect(Collectors.toList());

        when(userService.getAllSuperVizor()).thenReturn(dtoList);


        mockMvc.perform(get("/manager/getAllSupervizors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(15))
                .andExpect(jsonPath("$[0].role").value(Role.SUPERVISOR.getDisplayName()))

                .andExpect(jsonPath("$[1].firstName").value("supervizormock2"))
                .andExpect(jsonPath("$[1].role").value(Role.SUPERVISOR.getDisplayName()))

//                .andExpect(jsonPath("$[2].SupervizorGivenTaskCountOfLastDay.Buğra").value(5))
        ;

    }

    @Test
    @WithMockUser(authorities = "manager:get")
    void shouldReturnAllSupervizorsAndUsers() throws  Exception{
        List<User> userList = new ArrayList<>();
        User mockSupervizor = new User();
        mockSupervizor.setId(15L);
        mockSupervizor.setFirstName("supervizormock");
        mockSupervizor.setRole(Role.SUPERVISOR);

        User mockWorker = new User();
        mockWorker.setId(20L);
        mockWorker.setFirstName("workermock2");
        mockWorker.setRole(Role.WORKER);

        userList.add(mockSupervizor);
        userList.add(mockWorker);

        List<UserAndSupervizorsDTO> dtoList = userList.stream()
                .map(userItem -> {
                    UserAndSupervizorsDTO dto = new UserAndSupervizorsDTO();
                    dto.setId(userItem.getId());
                    dto.setFirstName(userItem.getFirstName());
                    dto.setSurName(userItem.getSurName());
                    dto.setRole(userItem.getRole());
                    dto.setEmail(userItem.getEmail());
                    dto.setLastLoginTime(userItem.getLastLoginTime());
                    dto.setLastOnlineTime(
                            userItem.getUserOnlineStatus() != null && userItem.getUserOnlineStatus().getLastOnlineTime() != null
                                    ? userItem.getUserOnlineStatus().getLastOnlineTime()
                                    : LocalDateTime.now()
                    );
                    return dto;
                })
                .collect(Collectors.toList());

        when(userService.getAllSupervizorAndWorker()).thenReturn(dtoList);


        mockMvc.perform(get("/manager/getAllSupervizorsAndUsers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(15))
                .andExpect(jsonPath("$[0].firstName").value("supervizormock"))
                .andExpect(jsonPath("$[0].role").value(mockSupervizor.getRole().getDisplayName()))
                .andExpect(jsonPath("$[1].firstName").value("workermock2"))
                .andExpect(jsonPath("$[1].role").value(Role.WORKER.getDisplayName()))

//                .andExpect(jsonPath("$[2].SupervizorGivenTaskCountOfLastDay.Buğra").value(5))
        ;

    }


    @Test
    @WithMockUser(authorities = "manager:get")
    void  shouldReturnAllSensorsForManagerUse() throws Exception{

        AllSensorForManagerDTO allSensorForManagerDTO = new AllSensorForManagerDTO();
        allSensorForManagerDTO.setId(10L);
        allSensorForManagerDTO.setSensorName("mock Sensor");
        allSensorForManagerDTO.setImageResponseDTO(null);
        allSensorForManagerDTO.setStatus(SensorStatus.ACTIVE.getDisplayName());
        allSensorForManagerDTO.setColor_code(null);
        allSensorForManagerDTO.setLatitude(20.45);
        allSensorForManagerDTO.setLongitude(30);
        allSensorForManagerDTO.setInstallationDate(new Date());
        allSensorForManagerDTO.setLastUpdatedAt(new Date());
        allSensorForManagerDTO.setCurrentSensorSession(null);


        when(sensorService.getAllSensorForManagerUse()).thenReturn(List.of(allSensorForManagerDTO));

        mockMvc.perform(get("/manager/getAllSensorForManagerUse"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].status").value(SensorStatus.ACTIVE.getDisplayName()))
                .andExpect(jsonPath("$[0].latitude").value("20.45"))
                .andExpect(jsonPath("$[0].longitude").value("30.0"))
                .andExpect(jsonPath("$[0].sensorName").value("mock Sensor"));



    }




    @Test
    @WithMockUser(authorities = "manager:get")
    void  shouldReturnJustDetailOfSensorForManagerManageSensorUsage() throws Exception{
        Long mockSensorId = 10L;
        byte[] mockImageData = "mock-image-data".getBytes();

        ImageResponseDTO imageResponseDTO = new ImageResponseDTO();
        imageResponseDTO.setName("mockimage.png");
        imageResponseDTO.setType("image/png");
        String base64 = Base64.getEncoder().encodeToString(mockImageData);
        imageResponseDTO.setBase64Image(base64);


        SensorDetailForManagerDTO mockSensorDetailForManagerDTO = new SensorDetailForManagerDTO();
        mockSensorDetailForManagerDTO.setSensorName("mockSensor");
        mockSensorDetailForManagerDTO.setImageResponseDTO(imageResponseDTO);



        when(sensorService.getJustDetailOfSensorForManagerManageSensorUsage(mockSensorId)).thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(new ApiResponse(true,"Successfully got sensor for manager sensor management page",
                                mockSensorDetailForManagerDTO,null,200))

                );

        mockMvc.perform(get("/sensors/sensormanagement/{sensorId}",mockSensorId))
                .andDo(print())
                .andExpect(jsonPath("$.data.sensorName").value("mockSensor"))
                .andExpect(jsonPath("$.data.imageResponseDTO.name").value("mockimage.png"))
                .andExpect(status().isAccepted());


    }

    @Test
    @WithMockUser(authorities = "manager:write")
    void  shouldManagerCreateSensor() throws Exception {
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
        Sensor sensor = new Sensor();
        sensor.setSensorName("mockSensor");
        Date now = new Date();
        sensor.setStatus(SensorStatus.ACTIVE);
        sensor.setLastUpdatedAt(now);
        sensor.setInstallationDate(now);

        when(sensorService.managerCreateSensor(anyString(),any(MultipartFile.class))).thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED).body(sensor));

        mockMvc.perform(
                requestBuilder
        )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.sensorName").value("mockSensor"));
    }

    @Test
    @WithMockUser(authorities = "manager:write")
    void  shouldManagerUpdateSensorLocation() throws Exception {
        CreateSensorLocationRequestDTO createSensorLocationRequestDTO = new CreateSensorLocationRequestDTO();
        createSensorLocationRequestDTO.setId(5L);
        createSensorLocationRequestDTO.setLatitude(20.40);
        createSensorLocationRequestDTO.setLongitude(25.40);


        CreateSensorLocationDTO createSensorLocationDTO = new CreateSensorLocationDTO();
        createSensorLocationDTO.setLatitude(createSensorLocationRequestDTO.getLatitude());
        createSensorLocationDTO.setLongitude(createSensorLocationRequestDTO.getLongitude());


        when(sensorService.managerUpdateSensorLocation(any(CreateSensorLocationRequestDTO.class))).thenReturn( ResponseEntity.status(HttpStatus.ACCEPTED).body(createSensorLocationDTO));
        mockMvc.perform(post("/manager/updateSensorLocations"))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.latitude").value(20.40))
                .andExpect(jsonPath("$.longitude").value(25.40));

        ;
//                .andExpect(status().isAccepted());


    }

    @Test
    @WithMockUser(authorities = "manager:write")
    void  shouldManagerUpdateInduvualSensor() throws Exception {
        Long mockSensorId = 10L;
        String mockSensorName = "mockSensorName";

        Sensor sensor = new Sensor();
        sensor.setSensorName("mockSensor");
        Date now = new Date();
        sensor.setStatus(SensorStatus.ACTIVE);
        sensor.setLastUpdatedAt(now);
        sensor.setInstallationDate(now);

        SensorIconImage sensorIconImage = new SensorIconImage();
        sensorIconImage.setSensor(sensor);


        when(sensorService.managerUpdateInduvualSensor(
                any(String.class),any(String.class),any(MultipartFile.class ))).thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED).body(sensor));

        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "sensor-icon.png",
                MediaType.IMAGE_PNG_VALUE,
                "test-data".getBytes()
        );
        MockHttpServletRequestBuilder validRequest = multipart("/manager/managerUpdateSensor")
                .file(mockFile)
                .param("sensorId", String.valueOf(mockSensorId))
                .param("sensorName", mockSensorName)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        mockMvc.perform(validRequest)
                .andDo(print())
                .andExpect(status().isAccepted())

                .andExpect(jsonPath("$.sensorName").value("mockSensor"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))

        ;
    }

    @Test
    @WithMockUser(authorities = "manager:delete")
    void shouldManagerDeleteSensorById() throws Exception{

        Sensor mockSensor = new Sensor();
        mockSensor.setId(10L);
        mockSensor.setSensorName("mockSensor");

        when(sensorService.deleteSensorById(mockSensor.getId())).thenReturn( ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Sensor Successfully deleted"
                )));


        mockMvc.perform(delete("/manager/deleteSensorById/{sensorId}",mockSensor.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("Sensor Successfully deleted"))            ;

    }


    @Test
    @WithMockUser(authorities = "manager:delete")
    void shouldManagergetAllSupervizorPdfReportBySupervizorId() throws Exception{
        Long mockSupervizorId = 3L;

        PdfRequestDTO pdfRequestDTO = new PdfRequestDTO();
        pdfRequestDTO.setOriginalSensorId(2L);
        pdfRequestDTO.setManagerId(30L);
        pdfRequestDTO.setSensorName("Su Seviyesi Sensörü");

        pdfRequestDTO.setNote("Acme Su Deposu İçin Kurulum");
        pdfRequestDTO.setStartTime("2023-05-15T09:00:00");
        pdfRequestDTO.setCompletedTime("2023-05-15T12:30:00");
        pdfRequestDTO.setLatitude(41.0082);
        pdfRequestDTO.setLongitude(28.9784);
        pdfRequestDTO.setSupervizorId(45L);

        when(managerService.getAllPdfReportsBasedOnSupervizor(mockSupervizorId)).thenReturn(ResponseEntity.ok(List.of(pdfRequestDTO)));

        mockMvc.perform(get("/manager/getAllSupervizorPdfReport/{supervisorId}",mockSupervizorId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sensorName").value("Su Seviyesi Sensörü"))
                  .andExpect(jsonPath("$[0].note").value("Acme Su Deposu İçin Kurulum"))
//
        ;

    }



    @Test
    @WithMockUser(authorities = "manager:get")
    void shouldManagergetNotificationById() throws Exception{
        Long mockManagerId = 10L;

        ManagerNotificationDTO managerNotificationDTO = new ManagerNotificationDTO();

        managerNotificationDTO.setId(10L);
        managerNotificationDTO.setIsread(false);
        managerNotificationDTO.setSenderName("mock sender");
        managerNotificationDTO.setSensorName("mocksensor");

        when( notificationService.getNotificationByManagerId(mockManagerId)).thenReturn(ResponseEntity.ok(List.of(managerNotificationDTO)));

        mockMvc.perform(get("/notifications/getNotificationOfManager/{userId}",mockManagerId))
                .andDo(print())
                .andExpect(jsonPath("$[0].sensorName").value("mocksensor"))
                .andExpect(jsonPath("$[0].senderName").value("mock sender"))

                .andExpect(status().isOk()) ;

    }

    @Test
    @WithMockUser(authorities = "manager:get")
    void shouldManagergetPdfReportInduvualSensor() throws Exception{
        SinglePdfReportDTO singlePdfReportDTO = new SinglePdfReportDTO();
        Long mockSensorId = 10L;

        singlePdfReportDTO.setId(352L);
        singlePdfReportDTO.setColor_code("null");
        singlePdfReportDTO.setSensorName("mockSensor");
        singlePdfReportDTO.setId(10L);

        byte[] mockImageData = "mock-image-data".getBytes();

        ImageResponseDTO imageResponseDTO = new ImageResponseDTO();

        imageResponseDTO.setName("mockimage.png");
        imageResponseDTO.setType("image/png");
        String base64 = Base64.getEncoder().encodeToString(mockImageData);
        imageResponseDTO.setBase64Image(base64);
        singlePdfReportDTO.setImageResponseDTO(List.of(imageResponseDTO));


        when( sensorService.getPdfReportInduvualSensor(String.valueOf(mockSensorId))).thenReturn( ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse(true,"Successfully got sensor",

                singlePdfReportDTO   ,null,200)));

        mockMvc.perform(get("/manager/getPdfReportInduvualSensor/{sensorId}",mockSensorId))
                .andDo(print())
//                .andExpect(jsonPath("$[0].originalSensorId").value(13))
//                .andExpect(jsonPath("$[0].sensorName").value("mockSensor"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully got sensor"))
                .andExpect(status().isAccepted()) ;

    }

    @Test
    @WithMockUser(authorities = "manager:write")
    void  shouldcreatePdfReportAndSendToManager() throws  Exception{
        PdfRequestDTO pdfRequestDTO = new PdfRequestDTO();
        pdfRequestDTO.setNote("Acme Su Deposu İçin Kurulum");
        pdfRequestDTO.setStartTime("2023-05-15T09:00:00");
        pdfRequestDTO.setCompletedTime("2023-05-15T12:30:00");
        pdfRequestDTO.setLatitude(41.0082);
        pdfRequestDTO.setLongitude(28.9784);
        pdfRequestDTO.setManagerId(30L);
        pdfRequestDTO.setSupervizorId(3L);
        pdfRequestDTO.setSensorName("TestSensor");

        PdfReports pdfReports = new PdfReports();
        pdfReports.setTechnicianNote(pdfRequestDTO.getNote());
        pdfReports.setSensorName(pdfRequestDTO.getSensorName());

        Long mockSensorId = 15L;

        Sensor mockSensor = new Sensor();
        mockSensor.setId(mockSensorId);


        User mockSupervizor = new User();
        mockSupervizor.setId(3L);
        mockSupervizor.setFirstName("supervizormock");
        mockSupervizor.setRole(Role.SUPERVISOR);

        User mockManager = new User();
        mockManager.setId(10L);
        mockManager.setFirstName("manager");


        when(userService.findById(pdfRequestDTO.getSupervizorId())).thenReturn(mockSupervizor);
        when(userService.findById(pdfRequestDTO.getManagerId())).thenReturn(mockManager);

        when(pdfReportService.createPdfAndSendNotificationToManager(pdfRequestDTO)).thenReturn(ResponseEntity.ok(pdfReports));

        mockMvc.perform(post("/manager/createAnnonucement")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("note", pdfRequestDTO.getNote())
                        .param("startTime", pdfRequestDTO.getStartTime())
                        .param("completedTime", pdfRequestDTO.getCompletedTime())
                        .param("latitude", String.valueOf(pdfRequestDTO.getLatitude()))
                        .param("longitude", String.valueOf(pdfRequestDTO.getLongitude()))
                        .param("managerId", String.valueOf(pdfRequestDTO.getManagerId()))
                        .param("supervizorId", String.valueOf(pdfRequestDTO.getSupervizorId()))
                        .param("sensorName", pdfRequestDTO.getSensorName()))
                .andDo(print())
                .andExpect(jsonPath("$.sensorName").value("TestSensor"))
                .andExpect(jsonPath("$.technicianNote").value("Acme Su Deposu İçin Kurulum"))
                .andExpect(status().isOk()) ;
    }



}
