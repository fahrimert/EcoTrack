package com.example.EcoTrack.user.controller;

import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.notification.dto.NotificationDTO;
import com.example.EcoTrack.notification.service.NotificationService;
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
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.model.UserOnlineStatus;
import com.example.EcoTrack.user.repository.UserOnlineStatusRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
        @Transactional
        public UserDTO getTheDetailOfALoggedInUserController(HttpServletRequest request , HttpServletResponse response ){
            String token = jwtService.extractTokenFromHeader(request);
            return userService.getTheDetailOfALoggedInUser(token);
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


    //Get the workers for is sensor  worker dashboard page
    @PostMapping("/worker/getProfilesOfWorkers")
    @Transactional
    public List<UserOnlineStatusDTO> getProfilesOfWorkers(@RequestBody List<Long> userIds) {
      return  userService.getProfilesOfAllWorkers(userIds);
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

    public  List<UserAndSessionSensorDTO> getAllWorkersSessionSensorAndTheirLocation(){
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
    @GetMapping("/worker/getTasksOfMe/{userId}")
    @Transactional
    public ResponseEntity  <List<SensorAllAndTaskDTO>> getSensorListFromTasksOfSingleUser (@PathVariable Long userId) {
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
    public List<SensorFix> getWorkerPastSensors(
    ){
        return  sensorService.getPastSensorsOfWorker();
    }


    //this endpoint for worker non-task sensor solving page update status select component
    @GetMapping("/sensors/getSensorStatuses")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public List<SensorStatus> getAllSensorStatuses(){

        return SensorStatus.getAll();

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
    @Transactional

    public ResponseEntity<String> goToThesensorSessionNotTheTask(@PathVariable Long sensorId){
        return sensorService.goToThesensorSessionNotTheTask(sensorId);
    }


    //This endpoint for solving non-task sensor session page
    @PutMapping("/worker/nonTaskSensorSolving/{sensorId}")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public ResponseEntity<String> updateNonTaskSensorToFinal(@RequestParam String note,@RequestParam SensorStatus statusID, @PathVariable Long sensorId,@RequestParam List<MultipartFile> files){
        return  sensorService.updateNonTaskSensorFinalState(note,statusID,sensorId,files);
    }

    //Get the past non task sensor detail endpoint based on given sensor ıd for worker
    @GetMapping("/worker/getPastNonTaskSensorDetail/{sensorId}")
    @Transactional
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    public ResponseEntity<ApiResponse> getWorkersPastNonTaskSensorDetail(@PathVariable Long sensorId){


        return  sensorService.getWorkersPastNonTaskSensorDetail(sensorId);
    }
    // worker sensor endpoints finish



    //start of user notification endpoints



    //worker update notification to read endpoint
    @PutMapping("/notifications/workerUpdateNotificationMarkIsRead/{userId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional

    public ResponseEntity<?>  workerUpdateNotificationMarkIsRead(@PathVariable Long userId){

        return notificationService.markNotificationsOfRead(userId);
    }


    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @GetMapping("/user/getNotifications/{userId}")
    @Transactional
    public ResponseEntity<List<NotificationDTO>> getNotificationById (@PathVariable Long userId) {
        return  userService.getNotificationById(userId);
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
        User user = userService.findByUsername(username);

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
