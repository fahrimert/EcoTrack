package com.example.EcoTrack.Tests.ManagerTests;

import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.manager.dto.TaskCountDTO;
import com.example.EcoTrack.manager.service.ManagerService;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.pdfReports.dto.PdfRequestDTO;
import com.example.EcoTrack.pdfReports.model.PdfReports;
import com.example.EcoTrack.pdfReports.repository.PdfRepository;
import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.sensors.repository.SensorImageIconRepository;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionImagesRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.sensors.service.SensorSessionImageService;
import com.example.EcoTrack.task.model.Task;
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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@Transactional
public class ManagerServiceUnitTests {


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
    void shouldgetSensorNameCounts(){
            Task mockTask = new Task();
            mockTask.setFinalStatus(SensorStatus.ACTIVE);


            Task mockTask2 = new Task();
            mockTask2.setFinalStatus(SensorStatus.ACTIVE);

            Task mocktask3 = new Task();
            mocktask3.setFinalStatus(SensorStatus.FAULTY);

            List<Task> mockTasks = List.of(mockTask,mockTask2,mocktask3);
        when(taskRepository.findAll()).thenReturn(mockTasks);

        List<TaskCountDTO> result = managerService.getSensorNameCounts();

        assertEquals(2,result.size());

        assertEquals(SensorStatus.ACTIVE,result.get(0).getName());
        assertEquals(SensorStatus.FAULTY,result.get(1).getName());

        }

//this is wrote by ai
    private  void assertRatioValue(List<Map<String, Long>> result, String key, long expected){
        Optional<Long> value = result.stream()
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> entry.getKey().equals(key))
                .map(Map.Entry::getValue)
                .findFirst();

        assertTrue(value.isPresent(), key + " metrik bulunamadı");
        assertEquals(expected, value.get(), key + " değeri yanlış");
    }


    @Test
    @WithMockUser(username = "testUser")
    void shouldGetSuperVizorPropertiesForRadarChart(){
        Long mockUserId = 5L;
        User mockUser = new User();

        mockUser.setId(mockUserId);
        mockUser.setFirstName("Mock firstname");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,7);
        Date oneWeekLater = calendar.getTime();

        Calendar calendarr = Calendar.getInstance();
        calendarr.add(Calendar.DAY_OF_YEAR,8);
        Date eightDaysLater = calendarr.getTime();


        Calendar calendr = Calendar.getInstance();
        calendr.add(Calendar.DAY_OF_YEAR,8);
        Date nineDaysLater = calendr.getTime();

        Task mockTask = new Task();
        mockTask.setFinalStatus(SensorStatus.ACTIVE);
        mockTask.setTaskCompletedTime(oneWeekLater);
        mockTask.setSuperVizorDeadline(LocalDateTime.now().plusDays(8));

        Task mockTask2 = new Task();
        mockTask2.setFinalStatus(SensorStatus.ACTIVE);
        mockTask2.setTaskCompletedTime(eightDaysLater);
        mockTask.setSuperVizorDeadline(LocalDateTime.now().plusDays(9));

        Task mocktask3 = new Task();
        mocktask3.setFinalStatus(SensorStatus.FAULTY);
        mocktask3.setTaskCompletedTime(nineDaysLater);
        mockTask.setSuperVizorDeadline(LocalDateTime.now().plusDays(4));

        List<Task> mockTasks = List.of(mockTask,mockTask2,mocktask3);

        long countOfTheCompletedTasksAfterDeadline = mockTasks.stream()
                .filter(session ->
                {
                    Date completedTime = session.getTaskCompletedTime();
                    LocalDateTime deadline = session.getSuperVizorDeadline();

                    if (completedTime == null || deadline == null) return  false;

                    Instant deadlineInstant = session.getSuperVizorDeadline().atZone(ZoneId.systemDefault()).toInstant();
                    Date deadlineDate = Date.from(deadlineInstant);
                    return  completedTime.after(deadlineDate);
                }).count();
        long totalTaskNumber = mockTasks.size();
        long takeTheRatioOfAfterDeadline = Math.round((double) countOfTheCompletedTasksAfterDeadline / totalTaskNumber * 100);

        long countOfTheCompletedTasksBeforeDeadline = mockTasks.stream()
                .filter(session ->
                {
                    Date completedTime = session.getTaskCompletedTime();
                    LocalDateTime deadline = session.getSuperVizorDeadline();

                    if (completedTime == null || deadline == null) return  false;

                    Instant deadlineInstant = session.getSuperVizorDeadline().atZone(ZoneId.systemDefault()).toInstant(); // LocalDateTime -> Instant
                    Date deadlineDate = Date.from(deadlineInstant);
                    return  completedTime.after(deadlineDate);
                }).count();

        long takeTheRatioOfBeforeDeadline = Math.round((double) countOfTheCompletedTasksBeforeDeadline / totalTaskNumber * 100);

        long countOfTheActiveTasks = mockTasks.stream()
                .filter(session -> session.getFinalStatus() == SensorStatus.ACTIVE)
                .count();
        long takeTheRatioOfFinishedStatusActiveTasks = Math.round((double) countOfTheActiveTasks / totalTaskNumber * 100);

        long countOfTheFinishedTasks= mockTasks.stream()
                .filter(session -> session.getTaskCompletedTime() != null )
                .count();
        long takeTheRatioOfFinishedTasks = Math.round((double) countOfTheFinishedTasks / totalTaskNumber * 100);
        System.out.println(takeTheRatioOfFinishedTasks);
        Map<String,  Long> takeTheRatioOfAfterDeadlineHashmap = new HashMap<>();

        Map<String, Long > takeTheRatioOfFinishedStatusActiveTasksHashmap = new HashMap<>();
        Map<String, Long > takeTheRatioOfBeforeDeadlineTasksHashmap = new HashMap<>();

        Map<String, Long > takeTheRatioOfFinishedTasksHashmap = new HashMap<>();
        takeTheRatioOfAfterDeadlineHashmap.put("Given Tasks Solved After Deadline Ratio",takeTheRatioOfAfterDeadline);


        takeTheRatioOfBeforeDeadlineTasksHashmap.put("Given Tasks Solved Before Deadline Ratio",takeTheRatioOfBeforeDeadline);

        takeTheRatioOfFinishedStatusActiveTasksHashmap.put("Given Tasks Final Status Actıve Ratio",takeTheRatioOfFinishedStatusActiveTasks);

        takeTheRatioOfFinishedTasksHashmap.put("Given Tasks Finished Task Ratıo",takeTheRatioOfFinishedTasks);


        List<Map<String, Long>> result = new ArrayList<>();

        result.add(takeTheRatioOfAfterDeadlineHashmap);
        result.add(takeTheRatioOfBeforeDeadlineTasksHashmap);
        result.add(takeTheRatioOfFinishedStatusActiveTasksHashmap);
        result.add(takeTheRatioOfFinishedTasksHashmap);
        when(userService.findById(mockUserId)).thenReturn(mockUser);
        mockUser.setTasksIAssigned(mockTasks);

        List<Map<String, Long>> resultRadar = managerService.getSuperVizorPropertiesForRadarChart(mockUserId);

        assertThat(resultRadar).hasSize(4);
            assertRatioValue(result, "Given Tasks Solved After Deadline Ratio", 33L);
        assertRatioValue(result, "Given Tasks Solved Before Deadline Ratio", 33L);
        assertRatioValue(result, "Given Tasks Final Status Actıve Ratio", 67L);
        assertRatioValue(result, "Given Tasks Finished Task Ratıo", 100L);

    }

    @Test
    @WithMockUser(username = "testUser")
    void  shouldgetTheAverageTaskCompletedTimeForWorkerChart(){
        List<User> userList = new ArrayList<>();
        User mockWorker = new User();

        mockWorker.setId(10L);
        mockWorker.setFirstName("Mock worker 1 ");

        User mockWorker2 = new User();

        mockWorker2.setId(12L);
        mockWorker2.setFirstName("Mock worker 2 ");
        List<User> workers = List.of(mockWorker, mockWorker2);

        when(userRepository.findAllByRole(Role.WORKER)).thenReturn(workers);

        Calendar calendarOfLastMonth = Calendar.getInstance();
        calendarOfLastMonth.add(Calendar.DAY_OF_YEAR,-30);
        Date oneMonthAgoa = calendarOfLastMonth.getTime();
        when(userRepository.findAllByRole(Role.WORKER)).thenReturn(userList);

        Map<String, Long> userAverageMinLastMonth = userList.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user ->{
                            List<Task> recentSessions = user.getTasksAssignedToMe().stream()
                                    .filter(task->
                                            task.getTaskCompletedTime() != null &&
                                                    task.getWorkerArrived() == true &&
                                                    task.getTaskCompletedTime().after(oneMonthAgoa)
                                    )
                                    .collect(Collectors.toList());

                            long totalDurationMillis = recentSessions.stream()
                                    .mapToLong(task ->
                                    {
                                        Instant completedTtaskInstant = task.getTaskCompletedTime().toInstant();
                                        Instant createdInstant = task.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant(); // LocalDateTime -> Instant

                                        return Duration.between(createdInstant,completedTtaskInstant).toMillis();
                                    })
                                    .sum();

                            long sessionCount = recentSessions.size();

                            if (sessionCount == 0) return 0L;

                            return totalDurationMillis / (sessionCount * 60 * 1000);

                        }));
        Map<String,   Map<String, Long>> averageTimeChartData = new HashMap<>();



        averageTimeChartData.put("averageChartData",userAverageMinLastMonth);

        Map<String,   Map<String, Long>> result = managerService.getTheAverageTaskCompletedTimeForWorkerChart();

        assertEquals(2,result.get("averageChartData").size());

        assertEquals(averageTimeChartData,result);


    }

    @Test
    @WithMockUser(username = "testUser")
    void  shouldgetAllPdfReportsBasedOnSupervizor(){
        Long mockSupervizorId = 10L;
        PdfReports pdfReports = new PdfReports();

        Sensor mockSensor = new Sensor();
        mockSensor.setId(10L);
        User mockSupervizor = new User();
        mockSupervizor.setId(3L);
        mockSupervizor.setFirstName("supervizormock");
        mockSupervizor.setRole(Role.SUPERVISOR);

        User mockManager = new User();
        mockManager.setId(10L);
        mockManager.setFirstName("manager");

        pdfReports.setSensorName("Su Seviyesi Sensörü");
        pdfReports.setTechnicianNote("Acme Su Deposu İçin Kurulum");
        pdfReports.setStartTime("2023-05-15T09:00:00");
        pdfReports.setCompletedTime("2023-05-15T12:30:00");
        pdfReports.setLatitude(41.0082);
        pdfReports.setLongitude(28.9784);
        pdfReports.setSensor(mockSensor);
        pdfReports.setManager(mockManager);
        pdfReports.setSupervisor(mockSupervizor);
        List<PdfReports> pdfReportsList = new ArrayList<>();

        pdfReportsList.add(pdfReports);

        PdfRequestDTO pdfRequestDTO = new PdfRequestDTO();
        pdfRequestDTO.setOriginalSensorId(mockSensor.getId());
        pdfRequestDTO.setManagerId(mockManager.getId());
        pdfRequestDTO.setSensorName("Su Seviyesi Sensörü");

        pdfRequestDTO.setNote("Acme Su Deposu İçin Kurulum");
        pdfRequestDTO.setStartTime("2023-05-15T09:00:00");
        pdfRequestDTO.setCompletedTime("2023-05-15T12:30:00");
        pdfRequestDTO.setLatitude(41.0082);
        pdfRequestDTO.setLongitude(28.9784);
        pdfRequestDTO.setSupervizorId(mockSupervizor.getId());


        List<PdfRequestDTO> pdfRequestDTOList = new ArrayList<>();
        pdfRequestDTOList.add(pdfRequestDTO);

        when(pdfRepository.findBySupervisorId(mockSupervizorId)).thenReturn(pdfReportsList);

        ResponseEntity result = managerService.getAllPdfReportsBasedOnSupervizor(mockSupervizorId);

        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertEquals(pdfRequestDTOList,result.getBody());


    }





    }

