package com.example.EcoTrack.user.controller;

import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.notification.dto.EnrichedNotificationDTO;
import com.example.EcoTrack.notification.dto.NotificationDTO;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.security.principal.UserPrincipal;
import com.example.EcoTrack.sensors.dto.ekipTakibiDtos.CrewJobsUserAndSessionSensorDTO;
import com.example.EcoTrack.sensors.dto.workerDashboardDtos.WorkerDashboardTaskSensorWithTaskDto;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.shared.dto.HeartbeatDTO;
import com.example.EcoTrack.task.dto.SensorAllAndTaskDTO;
import com.example.EcoTrack.task.service.TaskService;
import com.example.EcoTrack.user.dto.UserAndSessionSensorDTO;
import com.example.EcoTrack.user.dto.UserDTO;
import com.example.EcoTrack.user.dto.UserLocationDTO;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO;
import com.example.EcoTrack.user.dto.pastsensors.PastSensorDetailDto;
import com.example.EcoTrack.user.dto.pastsensors.PastSensorsDto;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.model.UserOnlineStatus;
import com.example.EcoTrack.user.repository.UserOnlineStatusRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;
    private final SensorService sensorService;
    private final TaskService taskService;
    private final UserRepository userRepository;
    private final UserOnlineStatusRepository userOnlineStatusRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserLocationService userLocationService;
    private final NotificationService notificationService;
    private final JwtService jwtService;
    public UserController(UserService userService, SensorService sensorService, TaskService taskService, UserRepository userRepository, UserOnlineStatusRepository userOnlineStatusRepository, SimpMessagingTemplate messagingTemplate, UserLocationService userLocationService, NotificationService notificationService, JwtService jwtService) {
        this.userService = userService;
        this.sensorService = sensorService;
        this.taskService = taskService;
        this.userRepository = userRepository;
        this.userOnlineStatusRepository = userOnlineStatusRepository;
        this.messagingTemplate = messagingTemplate;
        this.userLocationService = userLocationService;
        this.notificationService = notificationService;
        this.jwtService = jwtService;
    }

    //Currently Logged In Worker  Detail data endpoint
    //bunu değiştir
        @GetMapping("/user/me")
        public UserDTO getTheDetailOfALoggedInUserController(HttpServletRequest request , HttpServletResponse response ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

            return userService.getTheDetailOfALoggedInUser(email);
        }





    //User Location Controller Based On A Given User Id endpoint  worker or supervizor
    @GetMapping("/user/getUserLocationBasedOnıd/{userId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public UserLocationDTO getUserLocationBasedOnTheirIdController(@PathVariable Long userId) {
        return  userService.getTheUserLocationBasedOnTheirId(userId);
    }



    //Endpoint About saving Worker Location
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @PostMapping("/workers/saveWorkersLocation")
    @SendTo("topic/locations")
    public  String saveLocation ( @RequestParam Double lat, @RequestParam Double longtitude) {

        Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
        String username = securityContextHolder.getName();

        return  userLocationService.saveUserLocation(username,lat,longtitude);

    }


    //Get the logged ın user location endpoint
        @GetMapping("/user/getUserLocation")
    @Transactional

    public UserLocationDTO getUserLocation(){
        Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
        String username = securityContextHolder.getName();

        return  userLocationService.getLocation(username);
    }


    // get all workers location and their session sensor location endpoint for worker ekiptakibi page
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @GetMapping("/workers/getAllWorkersSessionSensorAndTheirLocation")
    @Transactional

    public   List<CrewJobsUserAndSessionSensorDTO> getAllWorkersSessionSensorAndTheirLocation(){
        return  userLocationService.getAllWorkersSessionSensorAndTheirLocation();
    }


    //Worker Task Section


    //Worker update the given task for "on road section endpoint
    @PutMapping("/worker/updateTaskForOnRoad/{taskId}")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional

    public ResponseEntity<ApiResponse<?>> workerUpdateTasksOnRoadNote(@RequestBody String workerNote, @PathVariable Long taskId){

        return taskService.workerUpdateTasksOnRoadNote(taskId,workerNote);
    }

    //Worker update task for solving task section endpoint
    @PutMapping("/worker/updateTaskForFinishing/{taskId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public ResponseEntity<String> workerUpdateTaskToFinal(@RequestParam String solvingNote, @RequestParam SensorStatus statusID, @PathVariable Long taskId, @RequestParam List<MultipartFile> files){
        return  taskService.workerUpdateTaskToFinal(solvingNote,statusID,taskId,files);
    }


    //get the tasks of user based on given id for worker pages use cases
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @GetMapping("/workerDashboard/getTasksOfMe/{userId}")
    @Transactional
    public ResponseEntity  <List<WorkerDashboardTaskSensorWithTaskDto>> getSensorListFromTasksOfSingleUser (@PathVariable Long userId) {
        return  taskService.getSensorListFromTasksOfSingleUser(userId);
    }

    // worker task section finish


    //worker sensor endpoints


    //worker past sensors page get sensor endpoint
    @GetMapping("/worker/past-sensors")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public List<PastSensorsDto> getWorkerPastSensors(
    ){
        return  sensorService.getPastSensorsOfWorker();
    }

    //This endpoint for worker page sensor session purposes not the task sensor go to endpoint
    @MessageMapping("/repair")
    @SendTo("topic/repair")
    @PutMapping("/sensor/goToThesensorSessionNotTheTask/{sensorId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public ResponseEntity<String> goToThesensorSessionNotTheTask(@PathVariable Long sensorId) {
        try {
            String responseMessage = sensorService.goToThesensorSessionNotTheTask(sensorId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseMessage);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sensor Not Found");
        }
        catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }


    //This endpoint for solving non-task sensor session page
    @PutMapping("/worker/nonTaskSensorSolving/{sensorId}")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public ResponseEntity<String> updateNonTaskSensorToFinal(@RequestParam String note,@RequestParam SensorStatus statusID, @PathVariable Long sensorId,@RequestParam List<MultipartFile> files ,Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getUser().getId();

        List<MultipartFile> fileList = (files != null) ? files : new ArrayList<>();

        sensorService.updateNonTaskSensorFinalState( note, statusID,sensorId, userId, fileList);

        return ResponseEntity.ok("Bakım tamamlandı ve kaydedildi.");
    }

    //Get the past non task sensor detail endpoint based on given sensor ıd for worker
    @GetMapping("/worker/getPastNonTaskSensorDetail/{sensorId}")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    public ResponseEntity<ApiResponse<PastSensorDetailDto>> getWorkersPastNonTaskSensorDetail(@PathVariable Long sensorId){
        PastSensorDetailDto detailDto = sensorService.getWorkersPastNonTaskSensorDetail(sensorId);
        return ResponseEntity.ok(ApiResponse.success(detailDto));
    }
    // worker sensor endpoints finish



    //start of user notification endpoints



    //worker update notification to read endpoint
    @PutMapping("/notifications/markAsRead/{userId}")
    public ResponseEntity<Void> markAllAsRead(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long currentUserId = userPrincipal.getUser().getId();

        notificationService.markNotificationsOfRead(currentUserId);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/user/getNotifications")
    @Transactional
    public ResponseEntity<List<EnrichedNotificationDTO>> getNotificationById(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long currentUserId = userPrincipal.getUser().getId();

        List<EnrichedNotificationDTO> notifications = userService.getEnrichedNotifications(currentUserId);

        return ResponseEntity.ok(notifications);

    }
    //end of user notification endpoints




    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @PostMapping("/hearthbeat")

    @Transactional
    public void hearthBeatController(HttpServletRequest request, HttpServletResponse response,  @RequestBody HeartbeatDTO heartbeatDTO){
        Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
        String username = securityContextHolder.getName();
        User user = userService.findByEmail(username);

        UserOnlineStatus userOnlineStatus = userOnlineStatusRepository.findByUser(user
        ) .orElseGet(() -> {
            UserOnlineStatus newStatus = new UserOnlineStatus();
            newStatus.setUser(user);
            return newStatus;
        });

        user.setUserOnlineStatus(userOnlineStatus);
        userOnlineStatus.setUser(user);
        userOnlineStatus.setIsOnline(heartbeatDTO.getIsOnline());

        userOnlineStatus.setLastOnlineTime(LocalDateTime.now());



        userOnlineStatusRepository.save(userOnlineStatus);
        userRepository.save(user);
        //tüm userları döndürsek
        List<User> allUsers = userRepository.findAll();
        List<UserOnlineStatusDTO> dtoList = allUsers.stream()
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

        //tüm userları dönsek tek tek dtolarına dönsek ama zaten bunu sadece kaydediyoruz sonrasında
        messagingTemplate.convertAndSend("/topic/users",dtoList);
    }





}
