package com.example.EcoTrack.service;

import com.example.EcoTrack.dto.*;
import com.example.EcoTrack.model.*;
import com.example.EcoTrack.repository.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class TaskService {

    private  final TaskRepository taskRepository;
    private  final  SensorService sensorService;
    private  final  NotificationService notificationService;
    private final  UserService userService;
    private  final SensorRepository sensorRepository;

    private final  SensorSessionRepository sensorSessionRepository;
    private  final UserRepository userRepository;
    private  final  TaskImageService taskImageService;
    private SimpMessagingTemplate messagingTemplate;
    private NotificationRepository notificationRepository;

    public TaskService(TaskRepository taskRepository, SensorService sensorService, NotificationService notificationService, UserService userService, SensorRepository sensorRepository, SensorSessionRepository sensorSessionRepository, UserRepository userRepository1, TaskImageService taskImageService, SimpMessagingTemplate messagingTemplate, NotificationRepository notificationRepository) {
        this.taskRepository = taskRepository;
        this.sensorService = sensorService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.sensorRepository = sensorRepository;
        this.sensorSessionRepository = sensorSessionRepository;
        this.userRepository = userRepository1;
        this.taskImageService = taskImageService;
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }
//    public   List<Task>  getAllUserTasks (Task task){
//
//        //burada atanan useri bulup o userda eğer aynı sensör ona atanmışsa zaten atanmasın aynı görev biaha
//            List<User> user =       userRepository.findAll();
//        List<User> allUsers = userRepository.findAll();
//        Map<String,   Map<String, Long>> userData = new HashMap<>();
//        List<UserOnlineStatusDTO> dtoList = allUsers.stream()
//                .map(userItem -> {userItem.getTasksAssignedToMe(),userItem.getTasksIAssigned();})
//                .collect(Collectors.toList());
//
//        return user;
//    }

    public  ResponseEntity<?> getTasks(){
        Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
        String username = securityContextHolder.getName();

        User user = userRepository.findByFirstName(username);

        List<Task> usersTask = userService.getAllTask(user.getId());


        List<TaskDTO> taskDTOList = usersTask.stream()
                .filter(task -> task.getSensor() != null)

                .map(a -> {
            UserTaskDTO userTaskDTOassignedto = new UserTaskDTO();
            userTaskDTOassignedto.setId(a.getAssignedTo().getId());
            userTaskDTOassignedto.setFirstName(a.getAssignedTo().getFirstName());
            userTaskDTOassignedto.setSurName(a.getAssignedTo().getSurName());

            UserTaskDTO userTaskDTOassignedBy = new UserTaskDTO();
            userTaskDTOassignedBy.setId(a.getAssignedBy().getId());
            userTaskDTOassignedBy.setFirstName(a.getAssignedBy().getFirstName());
            userTaskDTOassignedBy.setSurName(a.getAssignedBy().getSurName());

            SensorTaskDTO sensorTaskDTO = new SensorTaskDTO();
            sensorTaskDTO.setId(a.getSensor().getId());
            sensorTaskDTO.setSensorName(a.getSensor().getSensorName());
            sensorTaskDTO.setLatitude(a.getSensor().getSensorLocation().getLocation().getX());
            sensorTaskDTO.setLongitude(a.getSensor().getSensorLocation().getLocation().getY());

            return new TaskDTO(
                    a.getId(),
                    a.getSuperVizorDescription(),
                    a.getSuperVizorDeadline(),
                    userTaskDTOassignedto,
                    userTaskDTOassignedBy,
                    sensorTaskDTO,
                    a.getWorkerArriving(),
                    a.getWorkerArrived()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(taskDTOList);

    }

    public  ResponseEntity<?> getSensorListFromTasksOfSingleUser(Long userId){

        User user = userRepository.findById(userId).orElse(null);
        List<Task> usersTask = userService.getAllTaskOfMe(user.getId());

        List<SensorAllAndTaskDTO> taskSensorListDTO  = usersTask.stream().map(a ->
        {
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


    public  ResponseEntity<?> markNotificationsOfRead(Long userId){
        User user = userService.findById(userId);
        List<Notification> notifications = notificationRepository.findByReceiverIdAndIsReadFalse(userId);
        for (Notification n : notifications) {
            n.setIsRead(true);
        }
        notificationRepository.saveAll(notifications);
        return ResponseEntity.ok().build();
    }


    public   ResponseEntity<?>  createTask (Task task){

        //burada atanan useri bulup o userda eğer aynı sensör ona atanmışsa zaten atanmasın aynı görev biaha
        UserTaskDTO userTaskDTOassignedto = new UserTaskDTO();
        UserTaskDTO userTaskDTOassignedBy = new UserTaskDTO();
        SensorTaskDTO sensorTaskDTO = new SensorTaskDTO();


        Sensor sensor = sensorRepository.findById(task.getSensor().getId()).orElse(null);

        sensorTaskDTO.setId(sensor.getId());
        sensorTaskDTO.setSensorName(sensor.getSensorName());
        sensorTaskDTO.setLatitude(sensor.getSensorLocation().getLocation().getX());
        sensorTaskDTO.setLongitude(sensor.getSensorLocation().getLocation().getY());

        Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
        String username = securityContextHolder.getName();


        User assignedToUser = userService.findById(task.getAssignedTo().getId());
        User assignedBy = userService.findByUsername(username);



        userTaskDTOassignedto.setId(assignedToUser.getId());
        userTaskDTOassignedto.setFirstName(assignedToUser.getFirstName());
        userTaskDTOassignedto.setSurName(assignedToUser.getSurName());


        userTaskDTOassignedBy.setId(assignedBy.getId());
        userTaskDTOassignedBy.setFirstName(assignedBy.getFirstName());
        userTaskDTOassignedBy.setSurName(assignedBy.getSurName());

        TaskDTO taskDTO = new TaskDTO(task.getId(), task.getSuperVizorDescription(),task.getSuperVizorDeadline()
        ,userTaskDTOassignedto
                ,userTaskDTOassignedBy
                ,sensorTaskDTO
                ,task.getWorkerArriving()
                ,task.getWorkerArrived()

        );

        User user = userService.findByUsername(assignedToUser.getFirstName());



        if ( sensor.getStatus() == SensorStatus.IN_REPAIR) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sensor is already in another worker hand");
        }

        sensor.setStatus(SensorStatus.IN_REPAIR);
        Date now = new Date();

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task);
        assignedBy.setTasksIAssigned(tasks);

        assignedToUser.setTasksAssignedToMe(tasks);
        sensor.setTasks(tasks);

        Task taskk = new Task();

        taskk.setSuperVizorDescription(task.getSuperVizorDescription());
        taskk.setSuperVizorDeadline(task.getSuperVizorDeadline());
        taskk.setAssignedTo(assignedToUser);
        taskk.setAssignedBy(assignedBy);
        taskk.setSensor(sensor);

        taskk.setCreatedAt(LocalDateTime.now());



        userRepository.save(assignedToUser);
        userRepository.save(assignedBy);

        taskRepository.save(taskk);

        Authentication securityContextHolderr = SecurityContextHolder.getContext().getAuthentication();
        String usernamee = securityContextHolderr.getName();



        Notification notificationn = new Notification();

        notificationn.setSupervizorDescription(taskk.getSuperVizorDescription());
        notificationn.setSuperVizorDeadline(taskk.getSuperVizorDeadline());
        notificationn.setUserNotifications(assignedToUser);
        notificationn.setReceiverId(assignedToUser.getId());
        notificationn.setSenderId(assignedBy.getId());
        notificationn.setTaskId(taskk.getId());

        notificationService.sendNotification(notificationn);


        messagingTemplate.convertAndSend("/topic/tasks", taskDTO);



        sensorRepository.save(sensor);


        return ResponseEntity.ok(taskDTO);
    }

    public  ResponseEntity<ApiResponse<?>>  updateTasksOnRoadNote (Long taskId, String workerNote){
        System.out.println(workerNote);
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
    @Transactional
    public ResponseEntity<String> finishTask(String workerNote,  Long taskId, List<MultipartFile> files){

        try {

            Task taskk = taskRepository.findById(taskId).orElseThrow();
            Sensor sensor = sensorRepository.findById(taskk.getSensor().getId()).orElseThrow(() -> new RuntimeException("Sensor Not Found"));

            taskk.setSolvingNote(workerNote);
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
