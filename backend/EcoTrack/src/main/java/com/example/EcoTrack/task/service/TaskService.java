package com.example.EcoTrack.task.service;

import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.sensors.model.SensorLocation;
import com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.shared.dto.SensorDTO;
import com.example.EcoTrack.shared.dto.SensorFixDTO;
import com.example.EcoTrack.task.dto.SensorAllAndTaskDTO;
import com.example.EcoTrack.task.dto.SensorTaskDTO;
import com.example.EcoTrack.task.dto.TaskDTO;
import com.example.EcoTrack.task.dto.UserTaskDTO;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.task.repository.TaskRepository;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service

public class TaskService {

    private  final TaskRepository taskRepository;
    private final UserService userService;
    private  final SensorRepository sensorRepository;
    private  final UserRepository userRepository;
    private  final TaskImageService taskImageService;
    private SimpMessagingTemplate messagingTemplate;
    private NotificationRepository notificationRepository;

    public TaskService(TaskRepository taskRepository,   UserService userService, SensorRepository sensorRepository,  UserRepository userRepository1, TaskImageService taskImageService, SimpMessagingTemplate messagingTemplate, NotificationRepository notificationRepository) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.sensorRepository = sensorRepository;
        this.userRepository = userRepository1;
        this.taskImageService = taskImageService;
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    //get the tasks of user based on given id for worker pages use case function
    public  ResponseEntity<?> getSensorListFromTasksOfSingleUser(Long userId){

        User user = userRepository.findById(userId).orElse(null);
        List<Task> usersTask = userService.getAllTaskOfMe(user.getId());

        List<SensorAllAndTaskDTO> taskSensorListDTO  = usersTask.stream().map(a ->
        {
            Sensor sensor = a.getSensor();
            if (sensor ==null){
                return  null;
            }
            SensorFix currentSession = a.getSensor().getCurrentSensorSession();
            SensorStatus status = a.getSensor().getStatus();
            SensorLocation location = a.getSensor().getSensorLocation();

            UserTaskDTO userTaskDTOassignedBy = new UserTaskDTO();
            userTaskDTOassignedBy.setId(a.getAssignedBy().getId());
            userTaskDTOassignedBy.setFirstName(a.getAssignedBy().getFirstName());
            userTaskDTOassignedBy.setSurName(a.getAssignedBy().getSurName());
         SensorDTO sensorDTO = new SensorDTO(
                    a.getSensor().getId(),
                    a.getSensor().getSensorName(),
                    status != null ? status.getDisplayName() : null,
                    status != null ? status.getColorCode() : null,
                    location != null && location.getLocation() != null ? location.getLocation().getX() : 0.0,
                    location != null && location.getLocation() != null ? location.getLocation().getY() : 0.0,

                    new SensorFixDTO(
                            currentSession != null ? currentSession.getId() : null,
                            a.getSensor().getSensorName(),
                            status != null ? status.getDisplayName() : null,
                            status != null ? status.getColorCode() : null,
                            currentSession != null ? currentSession.getNote() : null,
                            currentSession != null ? currentSession.getStartTime() : null,
                            currentSession != null ? currentSession.getCompletedTime() : null,
                            location != null && location.getLocation() != null ? location.getLocation().getX() : 0.0,
                            location != null && location.getLocation() != null ? location.getLocation().getY() : 0.0
                    )
            );
         Long taskId = a.getId();
            return new SensorAllAndTaskDTO(sensorDTO,
                    taskId,
                    a.getSuperVizorDescription(),
                    a.getSuperVizorDeadline(),userTaskDTOassignedBy,
                    a.getWorkerArriving(),
                    a.getWorkerArrived(),
                    a.getWorkerOnRoadNote(),
            a.getSolvingNote(),
            a.getTaskImages(),
            a.getTaskCompletedTime()
            );

        }).collect(Collectors.toList());
        return ResponseEntity.ok(taskSensorListDTO);

    }

    //worker update the task and add on road note  function
    public  ResponseEntity<ApiResponse<?>>  workerUpdateTasksOnRoadNote (Long taskId, String workerNote){
        Task task = taskRepository.findById(taskId).orElse(null);

        Boolean idInTaskList = taskRepository.findAll().stream().map(a ->a.getId()).collect(Collectors.toList()).contains(taskId);

        Boolean arriving = task.getWorkerArriving();
        String note = task.getWorkerOnRoadNote();

        if (Boolean.TRUE.equals(arriving) && note != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Task Already Updated",
                            List.of("Task Already Updated"),
                            HttpStatus.FORBIDDEN
                    ));
        }
        if (idInTaskList == false) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Task Not Found",
                            List.of("Task Not Found"),
                            HttpStatus.FORBIDDEN
                    ));

        }
        else {
            task.setWorkerOnRoadNote(workerNote);
            task.setWorkerArriving(true);
           Task savedTask =  taskRepository.save(task);

                        UserTaskDTO userTaskDTOassignedto = new UserTaskDTO();
                        userTaskDTOassignedto.setId(savedTask.getAssignedTo().getId());
                        userTaskDTOassignedto.setFirstName(savedTask.getAssignedTo().getFirstName());
                        userTaskDTOassignedto.setSurName(savedTask.getAssignedTo().getSurName());

                        UserTaskDTO userTaskDTOassignedBy = new UserTaskDTO();
                        userTaskDTOassignedBy.setId(savedTask.getAssignedBy().getId());
                        userTaskDTOassignedBy.setFirstName(savedTask.getAssignedBy().getFirstName());
                        userTaskDTOassignedBy.setSurName(savedTask.getAssignedBy().getSurName());

                        SensorTaskDTO sensorTaskDTO = new SensorTaskDTO();
                        sensorTaskDTO.setId(savedTask.getSensor().getId());
                        sensorTaskDTO.setSensorName(savedTask.getSensor().getSensorName());
                        sensorTaskDTO.setLatitude(savedTask.getSensor().getSensorLocation().getLocation().getX());
                        sensorTaskDTO.setLongitude(savedTask.getSensor().getSensorLocation().getLocation().getY());

            TaskDTO taskDTO =new TaskDTO(
                                savedTask.getId(),
                                savedTask.getSuperVizorDescription(),
                                savedTask.getSuperVizorDeadline(),
                                userTaskDTOassignedto,
                                userTaskDTOassignedBy,
                                sensorTaskDTO,
                                savedTask.getWorkerArriving(),
                                savedTask.getWorkerArrived()
                        );


            messagingTemplate.convertAndSend("/topic/tasks", taskDTO);


       return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(ApiResponse.success(
                            "Successfully updated"
                    ));
        }



    }

    //worker update the task to final function
    @Transactional
    public ResponseEntity<String> workerUpdateTaskToFinal(String workerNote,SensorStatus statusID,  Long taskId, List<MultipartFile> files){

        try {

            Task taskk = taskRepository.findById(taskId).orElseThrow();
            Sensor sensor = sensorRepository.findById(taskk.getSensor().getId()).orElseThrow(() -> new RuntimeException("Sensor Not Found"));

            taskk.setSolvingNote(workerNote);
            taskk.setFinalStatus(statusID);

            taskImageService.uploadTaskImage(files,taskId);
            taskk.setWorkerArrived(true);


            sensor.setStatus(SensorStatus.SOLVED);

            Date now = new Date();
            taskk.setTaskCompletedTime(now);

            sensorRepository.save(sensor);
            Task savedTask = taskRepository.save(taskk);


            UserTaskDTO userTaskDTOassignedto = new UserTaskDTO();
            userTaskDTOassignedto.setId(savedTask.getAssignedTo().getId());
            userTaskDTOassignedto.setFirstName(savedTask.getAssignedTo().getFirstName());
            userTaskDTOassignedto.setSurName(savedTask.getAssignedTo().getSurName());

            UserTaskDTO userTaskDTOassignedBy = new UserTaskDTO();
            userTaskDTOassignedBy.setId(savedTask.getAssignedBy().getId());
            userTaskDTOassignedBy.setFirstName(savedTask.getAssignedBy().getFirstName());
            userTaskDTOassignedBy.setSurName(savedTask.getAssignedBy().getSurName());

            SensorTaskDTO sensorTaskDTO = new SensorTaskDTO();
            sensorTaskDTO.setId(savedTask.getSensor().getId());
            sensorTaskDTO.setSensorName(savedTask.getSensor().getSensorName());
            sensorTaskDTO.setLatitude(savedTask.getSensor().getSensorLocation().getLocation().getX());
            sensorTaskDTO.setLongitude(savedTask.getSensor().getSensorLocation().getLocation().getY());
            TaskDTO taskDTO =new TaskDTO(
                    savedTask.getId(),
                    savedTask.getSuperVizorDescription(),
                    savedTask.getSuperVizorDeadline(),
                    userTaskDTOassignedto,
                    userTaskDTOassignedBy,
                    sensorTaskDTO,
                    savedTask.getWorkerArriving(),
                    savedTask.getWorkerArrived()
            );
             notificationRepository.deleteBytaskId(taskId);

            messagingTemplate.convertAndSend("/topic/tasks", taskDTO);

            return  ResponseEntity.status(HttpStatus.ACCEPTED).body("Task Sensor Updated" + taskk.getSensor().getSensorName());

        }catch (Exception e){
            System.out.println(e.getMessage());
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }


    };



}
