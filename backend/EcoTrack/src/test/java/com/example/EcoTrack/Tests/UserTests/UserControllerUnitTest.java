package com.example.EcoTrack.Tests.UserTests;
import com.example.EcoTrack.notification.dto.NotificationDTO;

import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.shared.dto.ApiResponse;

import com.example.EcoTrack.shared.dto.SensorSessionDTO;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.task.repository.TaskRepository;
import com.example.EcoTrack.task.service.TaskService;
import com.example.EcoTrack.user.controller.UserController;
import com.example.EcoTrack.user.dto.UserAndSessionSensorDTO;
import com.example.EcoTrack.user.dto.UserLocationDTO;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO;
import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

    @ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {
    @InjectMocks
    private UserController controller;

    @Mock
    private  User user;

    @Mock
    private Sensor sensor;



    @Mock
    private UserService userService;

    @Mock
    private TaskService taskService;


    @Mock
    private UserLocationService userLocationService;


    @Mock
    private SensorService sensorService;

    @Mock
    private SensorSessionRepository sensorSessionRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;



        @Test
        void getNotifications_shouldReturnNotificationByUserId() throws Exception {
            Long mockId = 1L;
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setId(101L);
            notificationDTO.setSupervizorDescription("Make sure it handled in 15");
            notificationDTO.setReceiverId(mockId);

            NotificationDTO notificationDTO2 = new NotificationDTO();
            notificationDTO2.setId(102L);
            notificationDTO2.setSupervizorDescription(" 7 minutes deadline has given ");
            notificationDTO.setReceiverId(mockId);



            List<NotificationDTO> expectedDTO = new ArrayList<>();
            expectedDTO.add(notificationDTO);
            expectedDTO.add(notificationDTO2);

            when(userService.getNotificationById(mockId)).thenReturn(ResponseEntity.ok(expectedDTO));

            ResponseEntity<List<NotificationDTO>> result = controller.getNotificationById(mockId);
            assertEquals(2, result.getBody().size());
            verify(userService).getNotificationById(mockId);
        }

        @Test
        void getWorkers_shouldReturnWorkersByUserId() throws  Exception {
            UserOnlineStatusDTO userOnlineStatusDTO1 = new UserOnlineStatusDTO();
            userOnlineStatusDTO1.setId(15L);
            userOnlineStatusDTO1.setFirstName("Mock User 1");
            userOnlineStatusDTO1.setSurName("Mock Surname 1");
            userOnlineStatusDTO1.setRole(Role.WORKER);

            UserOnlineStatusDTO userOnlineStatusDTO2 = new UserOnlineStatusDTO();
            userOnlineStatusDTO2.setId(15L);
            userOnlineStatusDTO2.setFirstName("Mock User 2");
            userOnlineStatusDTO2.setSurName("Mock Surname 2");
            userOnlineStatusDTO2.setRole(Role.SUPERVISOR);

            List<UserOnlineStatusDTO> userOnlineStatusDTOList = new ArrayList<>();
            userOnlineStatusDTOList.add(userOnlineStatusDTO1);
            userOnlineStatusDTOList.add(userOnlineStatusDTO2);

            when(userService.getProfilesOfAllWorkers(userOnlineStatusDTOList.stream().map(a ->a.getId()).toList())).thenReturn(userOnlineStatusDTOList);

            List<UserOnlineStatusDTO> result = controller.getProfilesOfWorkers(userOnlineStatusDTOList.stream().map(a ->a.getId()).toList());
            assertEquals(2, result.size());
            verify(userService).getProfilesOfAllWorkers(userOnlineStatusDTOList.stream().map(a ->a.getId()).toList());

        }

        @Test
        void saveLocation_ShouldSaveLocationAndReturnSuccess() throws Exception {
            Double mockLat = 24.0;
            Double mockLang = 25.0;

            String mockUsername = "testUser";

            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(mockUsername);
            SecurityContextHolder.setContext(securityContext);


            String MockResponse = "User location details saved successfully";

            when(userLocationService.saveUserLocation(mockUsername, mockLat, mockLang))
                    .thenReturn(MockResponse);

            String result = controller.saveLocation(mockLat,mockLang);

            assertEquals(MockResponse, result);
            verify(userLocationService).saveUserLocation(mockUsername,mockLat,mockLang);

        }

        @Test
        void getUserLocation_ShouldReturnLocationDTO() {
            String username = "testUser";
            UserLocationDTO expectedDTO = new UserLocationDTO(120L, 25.0, 25.0);

            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(username);
            SecurityContextHolder.setContext(securityContext);

            when(userLocationService.getLocation(username)).thenReturn(expectedDTO);

            UserLocationDTO result = controller.getUserLocation();

            assertNotNull(result);
            assertEquals(expectedDTO, result);
            verify(userLocationService).getLocation(username);
        }



        //burdan devam
        @Test
        void getPastSensors_shouldReturnWorkersPastSensors() throws Exception {
            Long mockId = 1L;
            String mockUsername = "TestUser";


            User user1 = new User();
            user1.setId(102L);
            user1.setFirstName("Mock User");


            SensorFix sensorFix = new SensorFix();
            sensorFix.setId(101L);
            sensorFix.setUser(user1);

            SensorFix sensorFix2 = new SensorFix();
            sensorFix2.setId(102L);
            sensorFix2.setUser(user1);

            List<SensorFix> expectedList = new ArrayList<>();
            expectedList.add(sensorFix);
            expectedList.add(sensorFix2);



            when(sensorService.getPastSensorsOfWorker()).thenReturn(expectedList);
            List<SensorFix> result = controller.getWorkerPastSensors();
            assertEquals(2, result.size());
            assertEquals(expectedList,result);
            assertEquals(user.getId(),result.get(0).getUser().getId());
            verify(sensorService).getPastSensorsOfWorker();
        }

        @Test
        void  getPastSensors_shouldHandleServiceException() throws Exception {
            when(sensorService.getPastSensorsOfWorker()).thenThrow(new RuntimeException());
            assertThrows(RuntimeException.class, () -> controller.getWorkerPastSensors());
        }
        @Test
        void getPastSensors_shouldReturnEmptyListWhenNoSensors() throws  Exception{
            when(sensorService.getPastSensorsOfWorker()).thenReturn(List.of());
            List<SensorFix> result = controller.getWorkerPastSensors();

            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnPastNonTaskSensorDetail_whenValidSensorIdProvided() throws Exception {
            Long sensorMockId = 1L;
            Date oneDayLater = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

            SensorSessionDTO sensorSessionDTO = new SensorSessionDTO();
            sensorSessionDTO.setId(101l);
            sensorSessionDTO.setSensorName("sensorname");
            sensorSessionDTO.setDisplayName(SensorStatus.ACTIVE.getDisplayName());
            sensorSessionDTO.setStartTime(new Date());
            sensorSessionDTO.setFinalStatus(SensorStatus.ACTIVE);
            sensorSessionDTO.setCompletedTime(new Date(System.currentTimeMillis() + 3600000)); // 1 saat sonrası
            sensorSessionDTO.setLatitude(41.0082);
            sensorSessionDTO.setLongitude(28.9784);
            sensorSessionDTO.setImageResponseDTO(null);
            sensorSessionDTO.setNote("Mock data note");
    //        sensorAllAndTaskDTO1.setTaskCompletedTime(oneDayLater);

            sensorSessionDTO.setCompletedTime(oneDayLater);
            sensorSessionDTO.setStartTime(new Date());
            sensorSessionDTO.setSensorIconImage(null);

            PastNonTaskSensorApiResponse expectedResponse = new PastNonTaskSensorApiResponse(true, "Successfully got sensor", sensorSessionDTO, null, 200);
            when(sensorService.getWorkersPastNonTaskSensorDetail(sensorMockId)).thenReturn(
                    ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse(true,"Successfully got sensor",sensorSessionDTO   ,null,200)));
            ResponseEntity<ApiResponse> result = controller.getWorkersPastNonTaskSensorDetail(sensorMockId);
            assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(expectedResponse.isSuccess(), result.getBody().isSuccess());
            assertEquals(expectedResponse.getMessage(), result.getBody().getMessage());
            verify(sensorService).getWorkersPastNonTaskSensorDetail(sensorMockId);
        }


        @Test
        void shouldReturnNotFound_whenInValidSensorIdProvided() throws Exception {
            Long ınvalidSensorMockId = - 1L;

            when(sensorService.getWorkersPastNonTaskSensorDetail(ınvalidSensorMockId)).thenReturn(ResponseEntity.status(NOT_FOUND).body(new ApiResponse(false,"Invalid Sensor id.",null,null,404)));
            ResponseEntity<ApiResponse> result = controller.getWorkersPastNonTaskSensorDetail(ınvalidSensorMockId);
            assertEquals(NOT_FOUND, result.getStatusCode());
            verify(sensorService).getWorkersPastNonTaskSensorDetail(ınvalidSensorMockId);
        }

        @Test
        void  shouldReturnNotFound_whenNullSensorIdProvided(){
        ResponseEntity<ApiResponse> result = controller.getWorkersPastNonTaskSensorDetail(null);

            assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
            verify(sensorService, never()).getWorkersPastNonTaskSensorDetail(any());
        }

        @Test
        void shouldReturnSuccessWithEmptyData_whenNoSensorFound() {
            Long sensorId = 999L;
            when(sensorService.getWorkersPastNonTaskSensorDetail(sensorId))
                    .thenReturn(ResponseEntity.ok(new ApiResponse(true, "No data", null, null, 200)));
            ResponseEntity<ApiResponse> result = controller.getWorkersPastNonTaskSensorDetail(sensorId);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNull(result.getBody().getData());
            assertEquals("No data", result.getBody().getMessage());
        }

        @Test
        void shouldReturnAllWorkersWithSensorSessionsAndLocations() {
            UserAndSessionSensorDTO userAndSessionSensorDTO = new UserAndSessionSensorDTO();
            UserAndSessionSensorDTO userAndSessionSensorDTO2 = new UserAndSessionSensorDTO();

            userAndSessionSensorDTO.setId(101L);

            Sensor sensor1 = new Sensor();
            sensor1.setId(1L);
            sensor1.setSensorName("mock");

            userAndSessionSensorDTO.setId(103L);
            userAndSessionSensorDTO.setSensor(sensor1);
            userAndSessionSensorDTO.setLatitude(25.0);
            userAndSessionSensorDTO.setLongitude(20.0);
            userAndSessionSensorDTO.setSensorlatitude(30.0);
            userAndSessionSensorDTO.setSensorlongitude(32.0);

            userAndSessionSensorDTO.setId(104L);
            userAndSessionSensorDTO2.setSensor(sensor1);
            userAndSessionSensorDTO2.setLatitude(29.0);
            userAndSessionSensorDTO2.setLongitude(15.0);
            userAndSessionSensorDTO2.setSensorlatitude(31.0);
            userAndSessionSensorDTO2.setSensorlongitude(42.0);


            List<UserAndSessionSensorDTO> expectedDTO = new ArrayList<>();
            expectedDTO.add(userAndSessionSensorDTO);
            expectedDTO.add(userAndSessionSensorDTO2);

            when(userLocationService.getAllWorkersSessionSensorAndTheirLocation()).thenReturn(expectedDTO);

            List<UserAndSessionSensorDTO> result = controller.getAllWorkersSessionSensorAndTheirLocation();
            assertEquals(2, result.size());
            verify(userLocationService).getAllWorkersSessionSensorAndTheirLocation();

            assertThat(result)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyInAnyOrderElementsOf(expectedDTO);

            result.forEach(dto -> {
                assertTrue(dto.getLatitude() >= -90 && dto.getLatitude() <= 90);
                assertTrue(dto.getLongitude() >= -180 && dto.getLongitude() <= 180);
            });
        }

        @Test
        void  shouldReturnEmptyListWhenNoWorkersFound(){
            when(userLocationService.getAllWorkersSessionSensorAndTheirLocation())
                    .thenReturn(Collections.emptyList());

            List<UserAndSessionSensorDTO> result = controller.getAllWorkersSessionSensorAndTheirLocation();

            assertTrue(result.isEmpty());
        }


        @Test
        void  shouldUpdate_NonTaskSensorSolvingComponent(){
            String mockNote = "Mock Note" ;
            String mockUsername = "TestUser";
            SensorStatus mockSensorStatus = SensorStatus.ACTIVE;
            Long mockId = 15L;
            String mockSensorName = "TestSensor";
            List<MultipartFile> files = new ArrayList<>();


            when(sensorService.updateNonTaskSensorFinalState(
                    mockNote,
                    mockSensorStatus,
                    mockId,
                    files
            )).thenReturn(ResponseEntity.accepted().body("Sensor Updated" + mockSensorName));


            ResponseEntity<String> result = controller.updateNonTaskSensorToFinal(
                    mockNote,
                    mockSensorStatus,
                    mockId,
                    files
            );

            assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
            assertEquals("Sensor Updated" + mockSensorName, result.getBody());

            verify(sensorService).updateNonTaskSensorFinalState(
                    mockNote,
                    mockSensorStatus,
                    mockId,
                    files
            );

        }


        //bu bi calsın edge case kısmında
        @Test
        void  shouldUpdateNonTaskSensorToFinal_ShouldReturnInternalServerError_WhenServiceThrowsException(){
    //        when(sensorService.updateNonTaskSensorFinalState(
    //                any(),
    //                any(),
    //                any(),
    //                any()
    //
    //        )).thenThrow(new RuntimeException("Sensor Not Found"));

            ResponseEntity<String> resultController = controller.updateNonTaskSensorToFinal(
                    null,
                    null,
                    null,
                    null
            );

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resultController.getStatusCode());
        }


        @Test
        void  shouldgoToTheSensorSessionNotTheTask(){
            Long mockSensorId = 15L;
            String mockSensorName = "TestSensor";

            User mockUser =  new User();
            mockUser.setFirstName("mockname");
            mockUser.setId(120L);

            Sensor sensor1 = new Sensor();
            sensor1.setId(mockSensorId);
            sensor1.setSensorName("mock");

            SensorFix sensorSession = new SensorFix();

            sensorSession.setSensor(sensor1);

            when(sensorService.goToThesensorSessionNotTheTask(mockSensorId)).thenReturn(ResponseEntity.accepted().body("Now you are repairing" + mockSensorName));

            ResponseEntity<String> result = controller.goToThesensorSessionNotTheTask(
                    mockSensorId
            );
            assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
            assertEquals("Now you are repairing" + mockSensorName, result.getBody());


        }

    @Test
    void goToSensorSessionNotTheTask_shouldReturnConflictWhenActiveSessionExists(){
        Long mockSensorId = 15L;

        when(sensorService.goToThesensorSessionNotTheTask(mockSensorId))
                .thenReturn(ResponseEntity.status(CONFLICT).body("You already have an active repair session."));

        ResponseEntity<String> response = controller.goToThesensorSessionNotTheTask(mockSensorId);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("You already have an active repair session.", response.getBody());
    }


    @Test
    void updateTaskForOnRoad_shouldReturnSuccessFullyUpdated(){
        String taskMockOnRoadNote = "im at the location on mock note in 15";
        Long mockTaskId = 1L;

        when(taskService.workerUpdateTasksOnRoadNote(mockTaskId,taskMockOnRoadNote))
                .thenReturn( ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(ApiResponse.success(
                                "Successfully updated"
                        )));

        ResponseEntity<?> response =  controller.workerUpdateTasksOnRoadNote(taskMockOnRoadNote,mockTaskId);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();

        assertEquals("Success" , apiResponse.getMessage());
    }


    @Test
    void updateTaskForFinishing_shouldReturnTaskSensorUpdated(){
        String taskMockSolvingNote = "Solving the task named mock";
        SensorStatus statusId = SensorStatus.SOLVED;
        Long mockTaskId = 1L;
        List<MultipartFile> files = new ArrayList<>();
        Task mockTask = new Task();

        mockTask.setSolvingNote(taskMockSolvingNote);
        when(taskService.workerUpdateTaskToFinal(taskMockSolvingNote, statusId, mockTaskId, files))
                .thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body("Task Sensor Updated"));

        ResponseEntity<?> response =  controller.workerUpdateTaskToFinal(taskMockSolvingNote , statusId ,mockTaskId ,files);


        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Task Sensor Updated", response.getBody());
    }





}
