package com.example.EcoTrack.task.service;

import com.example.EcoTrack.sensors.dto.workerDashboardDtos.WorkerDashboardTaskSensorWithTaskDto;
import com.example.EcoTrack.sensors.dto.workerDashboardDtos.WorkerDashboardTaskWithSensorDto;
import com.example.EcoTrack.sensors.dto.workerDashboardDtos.WorkerDashboardTaskWithSensorFixDto;
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
    private WorkerDashboardTaskSensorWithTaskDto convertToSensorAllAndTaskDTO(Task task) {
        Sensor sensor = task.getSensor();

        WorkerDashboardTaskWithSensorDto workerDashboardTaskWithSensorDto = null;
        if (sensor != null) {
            workerDashboardTaskWithSensorDto = convertToWorkerDashboardTaskWithSensorDTO(sensor);
        }

        UserTaskDTO assignedByDTO = new UserTaskDTO(
                task.getAssignedBy().getId(),
                task.getAssignedBy().getFirstName(),
                task.getAssignedBy().getSurName()
        );

        return new WorkerDashboardTaskSensorWithTaskDto(
                task.getId(),
                workerDashboardTaskWithSensorDto,
                task.getSuperVizorDescription(),
                task.getSuperVizorDeadline(),
                assignedByDTO,
                task.getWorkerArriving(),
                task.getWorkerArrived(),
                task.getWorkerOnRoadNote(),
                task.getSolvingNote(),
                task.getTaskImages(),
                task.getTaskCompletedTime()
        );
    }

    private WorkerDashboardTaskWithSensorDto convertToWorkerDashboardTaskWithSensorDTO(Sensor sensor) {
        SensorStatus status = sensor.getStatus();
        SensorLocation loc = sensor.getSensorLocation();
        SensorFix session = sensor.getCurrentSensorSession();

        WorkerDashboardTaskWithSensorFixDto workerDashboardTaskWithSensorFixDTO = null;
        if (session != null) {
            workerDashboardTaskWithSensorFixDTO = WorkerDashboardTaskWithSensorFixDto.builder()
                    .id(session.getId())
                    .note(session.getNote())
                    .startTime(session.getStartTime())
                    .completedTime(session.getCompletedTime())
                    .build();
        }

        double lat = (loc != null && loc.getLocation() != null) ? loc.getLocation().getY() : 0.0;
        double lng = (loc != null && loc.getLocation() != null) ? loc.getLocation().getX() : 0.0;

        return WorkerDashboardTaskWithSensorDto.builder()
                .id(sensor.getId())
                .sensorName(sensor.getSensorName())
                .status(status != null ? status.name() : "UNKNOWN")
                .color_code(status != null ? status.getColorCode() : "#000000")
                .latitude(lat)
                .longitude(lng)
                .currentSensorSession(workerDashboardTaskWithSensorFixDTO)
                .build();
    }
    public  ResponseEntity<List<WorkerDashboardTaskSensorWithTaskDto>> getSensorListFromTasksOfSingleUser(Long userId){
        List<Task> tasks = taskRepository.findIncompleteTasksByWorkerId(userId);

        List<WorkerDashboardTaskSensorWithTaskDto> dtos = tasks.stream()
                .map(this::convertToSensorAllAndTaskDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);

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
