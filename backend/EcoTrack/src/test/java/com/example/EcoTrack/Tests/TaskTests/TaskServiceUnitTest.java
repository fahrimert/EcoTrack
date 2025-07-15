package com.example.EcoTrack.Tests.TaskTests;


import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.sensors.model.*;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.task.repository.TaskRepository;
import com.example.EcoTrack.task.service.TaskImageService;
import com.example.EcoTrack.task.service.TaskService;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.repository.UserRepository;
import jakarta.transaction.Transactional;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.ACCEPTED;

@ExtendWith(MockitoExtension.class)
@Transactional
public class TaskServiceUnitTest {

    @Mock
    private UserRepository userRepository;


    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SensorRepository sensorRepository;
    @Mock
    private NotificationRepository notificationRepository;



    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskImageService taskImageService;



    @Test
    void returnSuccess_whenUpdateTaskForOnRoad(){
    Long mockTaskId = 10L;
    String mockTaskWorkerOnRoadNote = "Im at there at 15 minutes";

    Task mockTask = new Task();
    mockTask.setId(mockTaskId);
    mockTask.setWorkerArriving(false);
    mockTask.setWorkerOnRoadNote(mockTaskWorkerOnRoadNote);

    User assignedBy = new User();
    assignedBy.setId(10L);
    assignedBy.setFirstName("mockassignedby");

    User assignedTo = new User();
    assignedTo.setId(15L);
    assignedBy.setFirstName("mockassignedto");

        Sensor mockSensor = new Sensor();
        mockSensor.setId(3L);
        mockSensor.setSensorName("Sensor-1");

        SensorLocation sensorLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(39.9, 32.8));
        sensorLocation.setLocation(point);
        mockSensor.setSensorLocation(sensorLocation);

        mockTask.setAssignedTo(assignedTo);
        mockTask.setAssignedBy(assignedBy);
        mockTask.setSensor(mockSensor);

        when(taskRepository.findAll()).thenReturn(List.of(mockTask));
    when(taskRepository.findById(mockTaskId)).thenReturn(Optional.of(mockTask));
    when(taskRepository.save(any(Task.class))).thenReturn(mockTask);


    ResponseEntity<?> response = taskService.workerUpdateTasksOnRoadNote(mockTaskId,mockTaskWorkerOnRoadNote);
        ApiResponse<?> apiResponse = (ApiResponse<?>) response.getBody();
    String apiResponseMessage = apiResponse.getMessage();

    assertEquals(ACCEPTED,response.getStatusCode());
    assertEquals("Success", apiResponseMessage);
    }

    @Test
    void returnTaskSensorUpdated_whenUpdateTaskForFinishing() throws IOException {
        String taskMockSolvingNote = "Solving the task named mock";
        SensorStatus statusId = SensorStatus.ACTIVE;
        Long mockTaskId = 1L;
        List<MultipartFile> files = new ArrayList<>();


        Task mockTask = new Task();
        mockTask.setId(mockTaskId);
        mockTask.setWorkerArriving(false);

        User assignedBy = new User();
        assignedBy.setId(10L);
        assignedBy.setFirstName("mockassignedby");

        User assignedTo = new User();
        assignedTo.setId(15L);
        assignedBy.setFirstName("mockassignedto");

        Sensor mockSensor = new Sensor();
        mockSensor.setId(1L);
        mockSensor.setSensorName("Sensor3Mock");

        mockTask.setSensor(mockSensor);
        SensorLocation mockLocation = new SensorLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(39.9, 32.8));
        mockLocation.setLocation(point);
        mockSensor.setSensorLocation(mockLocation);



        mockTask.setSensor(mockSensor);
        mockTask.setAssignedTo(assignedTo);
        mockTask.setAssignedBy(assignedBy);

        mockTask.setSolvingNote(taskMockSolvingNote);
        mockTask.setFinalStatus(statusId);

        when(taskRepository.findById(mockTaskId)).thenReturn(Optional.of(mockTask));
        when(sensorRepository.findById(mockTask.getSensor().getId())).thenReturn(Optional.of(mockTask.getSensor()));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);
        when(sensorRepository.save(any(Sensor.class))).thenReturn(mockSensor);

        when(taskImageService.uploadTaskImage(anyList(), eq(mockTaskId))).thenReturn(List.of());


        ResponseEntity<?> response = taskService.workerUpdateTaskToFinal(taskMockSolvingNote,statusId, mockTaskId,files);
        System.out.println(response);

        assertEquals(ACCEPTED,response.getStatusCode());
        assertEquals("Task Sensor Updated" +mockTask.getSensor().getSensorName(), response.getBody());

        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Task Sensor Updated" + mockTask.getSensor().getSensorName(), response.getBody());
        verify(taskImageService).uploadTaskImage(files, mockTaskId);
        }
}
