package com.example.EcoTrack.supervizor.service;

import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.notification.type.NotificationType;
import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorLocation;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.shared.dto.SensorDTO;
import com.example.EcoTrack.shared.dto.SensorFixDTO;
import com.example.EcoTrack.shared.dto.SensorLocationDTO;
import com.example.EcoTrack.shared.dto.SensorWithUserProjection;
import com.example.EcoTrack.supervizor.dto.SensorCountDTO;
import com.example.EcoTrack.supervizor.dto.SensorDateCountDTO;
import com.example.EcoTrack.task.dto.SensorTaskDTO;
import com.example.EcoTrack.task.dto.TaskDTO;
import com.example.EcoTrack.task.dto.UserTaskDTO;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.task.repository.TaskRepository;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO;
import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class SuperVizorService {
    private final UserService userService;
    private  final UserRepository userRepository;
    private  final SensorRepository sensorRepository;
    private  final SensorSessionRepository sensorSessionRepository;
    private  final TaskRepository taskRepository;
    private  final NotificationService notificationService;
    private SimpMessagingTemplate messagingTemplate;

    public SuperVizorService(UserService userService, UserRepository userRepository, SensorRepository sensorRepository, SensorSessionRepository sensorSessionRepository, TaskRepository taskRepository, NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.sensorRepository = sensorRepository;
        this.sensorSessionRepository = sensorSessionRepository;
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    //Supervizor dashboard graph functions


    //Getting all non task sensor session datas for workers and count them for dashboard doughnut component graph
    @Transactional
    public    ResponseEntity<Map<SensorStatus,Long>> getAllSensorStatusMetricValuesForDoughnutComponent(){
        List<SensorFix> sensorSessions = sensorSessionRepository.findAll();

        Map<SensorStatus,Long> statusCounts = sensorSessions.stream().map(sensor -> sensor.getFinalStatus())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        return ResponseEntity.ok(statusCounts);
    }


    //Get the time based data for supervizor dashboard bar chart on workers non task session solvings
    @Transactional
    public  List<Map<String, Map<String, Long>>> getTimeBasedSessionWorkerStatsForBarChartData(){
        List<User> users = userRepository.findAllByRole(Role.WORKER);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,-7);
        Date oneWeekAgo = calendar.getTime();

        Calendar calendarOfLastMonth = Calendar.getInstance();
        calendarOfLastMonth.add(Calendar.DAY_OF_YEAR,-30);
        Date oneMonthAgo = calendarOfLastMonth.getTime();


        Calendar calendarOfLastDay = Calendar.getInstance();
        calendarOfLastDay.add(Calendar.DAY_OF_YEAR,-1);
        Date oneDayAgo = calendarOfLastDay.getTime();

        List<Map<String, Map<String, Long>>> result = new ArrayList<>();

        Map<String,   Map<String, Long>> weekdata = new HashMap<>();
        Map<String, Map<String, Long> > monthData = new HashMap<>();

        Map<String, Map<String, Long> > dailydata = new HashMap<>();



        Map<String, Long> userSessionCountsOfLastWeeek = getSinceDateForWorkerStat(oneWeekAgo,users);

        Map<String, Long> userSessionCountsOfLastMonth = getSinceDateForWorkerStat(oneMonthAgo,users);

        Map<String, Long> userSessionCountsOfLastDay = getSinceDateForWorkerStat(oneDayAgo,users);

        monthData.put("last_month",userSessionCountsOfLastMonth);

        weekdata.put("last_week",userSessionCountsOfLastWeeek);

        dailydata.put("last_day",userSessionCountsOfLastDay);
        result.add(monthData);

        result.add(weekdata);
        result.add(dailydata);

        return result;
    }
    //getTimeBasedSessionWorkerStatsForBarChartData component helper function
    private Map<String, Long> getSinceDateForWorkerStat(Date sinceDate,List<User> users){
        return users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user -> user.getSensorSessions().stream()
                                .filter(session ->
                                        session.getCompletedTime() != null &&
                                                session.getCompletedTime().after(sinceDate) &&
                                                session.getFinalStatus() == SensorStatus.SOLVED  || session.getFinalStatus() == SensorStatus.ACTIVE)
                                .count()
                ));

    }

    //Get The Faulty Locations For Supervizor dashboard Heatmap component
    public  List<SensorLocationDTO> getFaultyLocationsForSupervizorDashboardHeatmapComponent(){
        List<SensorLocationDTO> sensorSessions = sensorSessionRepository.findAllSensorFixesWithFaultySensors().stream().map(a -> {
            SensorLocationDTO sensorLocationDTO = new SensorLocationDTO();

            sensorLocationDTO.setId(a.getId());
            sensorLocationDTO.setLatitude(a.getSensor().getSensorLocation().getLocation().getX());
            sensorLocationDTO.setLongitude(a.getSensor().getSensorLocation().getLocation().getY());
            return sensorLocationDTO;
        }).collect(Collectors.toList());

        return sensorSessions;
    }

    //end of supervizor dashboard graph functions

    //Supervizor worker past sensors page functions

    public List<SensorWithUserProjection> getPastSensorsOfWorkers() {
        try {
            return sensorSessionRepository
                    .findCompletedSensorsWithUserDetails("WORKER");

        } catch (Exception e) {
            System.out.println("getPastSensorsOfWorkers failed" + e.getMessage() + e);
            return Collections.emptyList();
        }
    }

    //end of supervizor worker past sensors page functions


    //start of useFetchAllWorkers
    public   List<UserOnlineStatusDTO> getAllWorker() {
        List<User> allUsers = userRepository.findAllByRole(Role.WORKER);
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

        return dtoList;
    }

    //end of useFetchAllWorkers

    //start of worker performance analysis page functions


    //last month performance graph for workers
        public  Map<Long, Map<String, Object>>   getSensorSessionsOfLastMonth( Long userId){
            LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
            List<SensorFix> data = sensorSessionRepository.findLastMonthDataByUserId(userId, oneMonthAgo);
            //sadece completed zamanı ve idsi lazım

            //yada direk id ye göre yapak id completed time şekli
            Map<Long, Map<String, Object>> completedTimeLastMonth = data.stream().collect(Collectors.toMap(
                    sensor -> {return  sensor.getId();}
                    ,
                    sensor -> {

                        Date start = sensor.getStartTime();
                        Date end = sensor.getCompletedTime();

                        long diffMilis = end.getTime() - start.getTime();
                        long diffSeconds = diffMilis / 1000;
                        long diffdays = diffSeconds / (24*3600);

                        diffSeconds %= (24*3600);
                        long hours = diffSeconds / 3600 ;

                        diffSeconds %= 3600;
                        long minutes = diffSeconds /60;

                        long seconds = diffSeconds % 60;

                        Map<String,Object> durationInfo = new HashMap<>();

                        durationInfo.put("rawMinutes", diffMilis / (60 * 1000));
                        durationInfo.put("formatted", String.format("%02d gün %02d saat %02d dakika %02d saniye", diffdays, hours, minutes, seconds));

                        return  durationInfo;
                    }
            ));;


            return completedTimeLastMonth;

        }
    //Get The workers non-task session solving Sensor Name counts function for worker-performance-analysis-chart page dougnut component

    public List<SensorCountDTO> getNonTaskSessionSolvingSensorNames(Long userId){
        User user = userService.findById(userId);

        List<SensorFix> sensorSessions = sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user);

        Map<String,Long> nameCounts = sensorSessions.stream().map(sensor ->


                        sensor.getSensor().getSensorName())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        //aynısının statüslü değil de sensor isimlisini yapsak mantıklı olabilir aslında usera göre bulup
        return         nameCounts.entrySet().stream().map(a -> new SensorCountDTO(a.getKey(),a.getValue())).collect(Collectors.toList());

    }


    //Get The workers non-task session solving dates and solving counts based on their dates endpoint function
    private List<SensorDateCountDTO> convertToSensorDateCountDTO(Map<LocalDate, Long> counts) {
        return counts.entrySet().stream()
                .map(entry -> new SensorDateCountDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


    private  Map<LocalDate,Long> mapToDateCount(List<SensorFix> sessions){

        Map<LocalDate,Long> nameCounts = sessions.stream()
                .filter(sf -> sf.getCompletedTime() != null)
                .map(sensor -> sensor.getCompletedTime()
                        .toInstant()

                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        return nameCounts;
    }


    public  List<SensorDateCountDTO> getDatesAndBasedOnTheirSessionCounts(Long userId){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
        Optional<User> user = userRepository.findById(userId);

        List<SensorFix> sensorSessions = sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user.get());


        //o tarih ve sayılarını almamız lazım
        Map<LocalDate, Long> countsByDate = mapToDateCount(sensorSessions);


        //aynısının statüslü değil de sensor isimlisini yapsak mantıklı olabilir aslında usera göre bulup
        return convertToSensorDateCountDTO(countsByDate);

    }

    //Get The workers faulty status heatmap component for supervizor dashboard page for selected workerId

    public List<SensorLocationDTO> getNonTaskHeatmapComponentForSupervizorDashboardPage (Long userId){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
        User user = userService.findById(userId);

        List<SensorFix> sensorSessions = sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user);

        List<SensorLocationDTO> sensorSessionss =sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user).stream().map(a -> {
                    SensorLocationDTO sensorLocationDTO = new SensorLocationDTO();

                    sensorLocationDTO.setId(a.getSensor().getId());
                    sensorLocationDTO.setLatitude(a.getSensor().getSensorLocation().getLocation().getX());
                    sensorLocationDTO.setLongitude(a.getSensor().getSensorLocation().getLocation().getY());
                    return sensorLocationDTO;
                })
                .collect(Collectors.toMap(
                        SensorLocationDTO::getId,
                        dto -> dto,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream().collect(Collectors.toList());

        return sensorSessionss;
    }


    //Get The Worker LeaderBoard Data for Leaderboard component in user performance analysis chart page
    public  List<Map<String, Map<String, Long>>> getThePerformanceTableForWorker(){
        List<User> users = userRepository.findAllByRole(Role.WORKER);


        Calendar calendarOfLastMonth = Calendar.getInstance();
        calendarOfLastMonth.add(Calendar.DAY_OF_YEAR,-30);
        Date oneMonthAgoa = calendarOfLastMonth.getTime();

        Calendar calendarOfLastDay = Calendar.getInstance();
        calendarOfLastDay.add(Calendar.DAY_OF_YEAR,-1);
        Date oneDayAgo = calendarOfLastDay.getTime();

        long maxAcceptableDurationMillis = 5L  * 60 * 60 * 1000;

        Map<String, Long> userAverageMinLastMonth = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user ->{
                            List<SensorFix> recentSessions = user.getSensorSessions().stream()
                                    .filter(session ->
                                            session.getCompletedTime() != null &&
                                                    session.getCompletedTime().after(oneMonthAgoa) &&
                                                    session.getCompletedTime().getTime() - session.getStartTime().getTime() <= maxAcceptableDurationMillis

                                    )
                                    .collect(Collectors.toList());
                            //en son ayki datasını alıp onun tek tek geçen zamanlarını toplayıp total sensor sessionuna bölüp ortalama zamanını buluyor
                            long totalDurationMillis = recentSessions.stream()
                                    .mapToLong(session -> session.getCompletedTime().getTime() - session.getStartTime().getTime())
                                    .sum();

                            //bu kısımda 5 saati geçenler datada çok uçlar oluşturduğu için onları çıkarttım datamdan ama sizelarını çıkartmayınca ortalama bu sefer çok küçük geliyor onu yapmadım o da yapılabilir
                            long sessionCount = recentSessions.size();

                            if (sessionCount == 0) return 0L;

                            return totalDurationMillis / (sessionCount * (60 * 1000));

                        }));

        Map<String, Long> userTotalSensorCount = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user ->{
                            List<SensorFix> recentSessions = user.getSensorSessions().stream()
                                    .filter(session ->
                                            session.getCompletedTime() != null &&
                                                    session.getCompletedTime().after(oneMonthAgoa)
                                    )
                                    .collect(Collectors.toList());
                            //en son ayki datasını alıp onun tek tek geçen zamanlarını toplayıp total sensor sessionuna bölüp ortalama zamanını buluyor

                            long sessionCount = recentSessions.size();

                            return sessionCount;

                        }));
        Map<String,   Map<String, Long>> averageTimeChartData = new HashMap<>();

        Map<String, Map<String, Long> > totalSensorChartData = new HashMap<>();

        Map<String, Map<String, Long> > dailydata = new HashMap<>();

        Map<String, Long> userSessionCountsOfLastDay = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user -> user.getSensorSessions().stream()
                                .filter(session ->
                                        session.getCompletedTime() != null &&
                                                session.getCompletedTime().after(oneDayAgo)
                                )
                                .count()

                ));

        averageTimeChartData.put("averageChartData",userAverageMinLastMonth);

        totalSensorChartData.put("totalSensorChartData",userTotalSensorCount);

        dailydata.put("last_day",userSessionCountsOfLastDay);

        List<Map<String, Map<String, Long>>> result = new ArrayList<>();

        result.add(averageTimeChartData);
        result.add(totalSensorChartData);
        result.add(dailydata);


        return result;
    }


    //end of worker performance analysis page functions

    //start of supervizor assigning task page functions


    //Supervizor assignin task functions

    public ResponseEntity supervizorCreateTaskForWorker (Task task){

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




        Notification notificationn = new Notification();

        notificationn.setSupervizorDescription(taskk.getSuperVizorDescription());
        notificationn.setSuperVizorDeadline(taskk.getSuperVizorDeadline());
        notificationn.setUserNotifications(assignedToUser);
        notificationn.setType(NotificationType.TASK);
        notificationn.setReceiverId(assignedToUser.getId());
        notificationn.setSenderId(assignedBy.getId());
        notificationn.setTaskId(taskk.getId());

        notificationService.sendNotification(notificationn);


        messagingTemplate.convertAndSend("/topic/tasks", taskDTO);



        sensorRepository.save(sensor);


        return ResponseEntity.ok(taskDTO);
    }

    //get my assigned tasks for supervizor function
        public  ResponseEntity getTasksOfIAssigned(){
            Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
            String username = securityContextHolder.getName();

            User user = userRepository.findByFirstName(username);

            List<Task> usersTask = userService.getAllTask(user.getId());

            if (usersTask.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supervizor doesnt have any assigned tasks ");
            }
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

    public List<SensorDTO> getAllAvailableSensorsForAssigningTaskSelectComponent() {


        List<SensorDTO> sensorlistDTO  = sensorRepository.findAllAvailable().stream().map(a ->
        {
            SensorFix currentSession = a.getCurrentSensorSession();
            SensorStatus status = a.getStatus();
            SensorLocation location = a.getSensorLocation();

            return new SensorDTO(
                    a.getId(),
                    a.getSensorName(),
                    status != null ? status.getDisplayName() : null,
                    status != null ? status.getColorCode() : null,
                    location != null && location.getLocation() != null ? location.getLocation().getX() : 0.0,
                    location != null && location.getLocation() != null ? location.getLocation().getY() : 0.0,

                    new SensorFixDTO(
                            currentSession != null ? currentSession.getId() : null,
                            a.getSensorName(),
                            status != null ? status.getDisplayName() : null,
                            status != null ? status.getColorCode() : null,
                            currentSession != null ? currentSession.getNote() : null,
                            currentSession != null ? currentSession.getStartTime() : null,
                            currentSession != null ? currentSession.getCompletedTime() : null,
                            location != null && location.getLocation() != null ? location.getLocation().getX() : 0.0,
                            location != null && location.getLocation() != null ? location.getLocation().getY() : 0.0
                    )
            );
        }).collect(Collectors.toList());
        return  sensorlistDTO;
    }

    //Supervizor end of assignin task functions






}
