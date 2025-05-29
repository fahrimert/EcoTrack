package com.example.EcoTrack.controller;

import com.example.EcoTrack.dto.*;
import com.example.EcoTrack.model.*;
import com.example.EcoTrack.repository.SensorSessionRepository;
import com.example.EcoTrack.repository.UserRepository;
import com.example.EcoTrack.service.SensorService;
import com.example.EcoTrack.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")

public class SuperVizorBasedController {

    private  final SensorSessionRepository sensorSessionRepository;
    private  final  SensorService sensorService;
    private  final UserRepository userRepository;
    private  final UserService userService;
    public SuperVizorBasedController(SensorSessionRepository sensorSessionRepository, SensorService sensorService, UserRepository userRepository, UserService userService) {
        this.sensorSessionRepository = sensorSessionRepository;
        this.sensorService = sensorService;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @GetMapping("/superVizorSensors/getSensorsFiltersBasedStat")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public    Map<SensorStatus,Long>  getAllSensorStatuses(){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
        List<SensorFix> sensorSessions = sensorSessionRepository.findAll();

        Map<SensorStatus,Long> statusCounts = sensorSessions.stream().map(sensor -> sensor.getSensor().getStatus())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        return statusCounts;

    }
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @GetMapping("/superVizorSensors/getAllUser")
    public  List<UserOnlineStatusDTO> getAllUser(){
        return  userService.getAllUsers();
    }


    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @GetMapping("/superVizorSensors/getAllusersWithoutTasks")
    @Transactional
    public  List<UserOnlineStatusDTO> getAllusersWithoutTasks(){
        return  userService.getAllusersWithoutTasks();
    }

    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @GetMapping("/superVizorSensors/getAllAvailableSensors")
    @Transactional

    public  List<SensorDTO> getAllAvailableSensors(){
        return  sensorService.getAllAvailableSensors();
    }


    @GetMapping("/superVizorSensors/getSensorSessionsOfLastMonth/{userId}")
    @Transactional

    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public         Map<Long, Map<String, Object>>  getSensorSessionsOfLastMonth(@PathVariable Long userId){
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
    @GetMapping("/superVizorSensors/getTheUserPerformanceCharts")
    @Transactional

    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public List<Map<String, Map<String, Long>>>     getTheAverageCompletedTimeForUserChart(){
        List<User> users = userRepository.findAll();

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        Calendar calendarOfLastMonth = Calendar.getInstance();
        calendarOfLastMonth.add(Calendar.DAY_OF_YEAR,-30);
        Date oneMonthAgoa = calendarOfLastMonth.getTime();

        Calendar calendarOfLastDay = Calendar.getInstance();
        calendarOfLastDay.add(Calendar.DAY_OF_YEAR,-1);
        Date oneDayAgo = calendarOfLastDay.getTime();

        Map<String, Long> userAverageMinLastMonth = users.stream()
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
                            long totalDurationMillis = recentSessions.stream()
                                    .mapToLong(session -> session.getCompletedTime().getTime() - session.getStartTime().getTime())
                                    .sum();

                            long sessionCount = recentSessions.size();

                            if (sessionCount == 0) return 0L;

                            return totalDurationMillis / (sessionCount * 60 * 1000);

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


    @GetMapping("/superVizorSensors/getSensorNames/{userId}")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public    List<SensorCountDTO>  getSensorNames(@PathVariable Long userId){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
        User user = userService.findById(userId);

        List<SensorFix> sensorSessions = sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user);
        List<String> sensorSession = sensorSessions.stream().map(a -> a.getSensor().getSensorName()).collect(Collectors.toList());
        Map<String,String> nameInfo = new HashMap<>();

        Map<String,Long> nameCounts = sensorSessions.stream().map(sensor -> sensor.getSensor().getSensorName())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        //aynısının statüslü değil de sensor isimlisini yapsak mantıklı olabilir aslında usera göre bulup
        return         nameCounts.entrySet().stream().map(a -> new SensorCountDTO(a.getKey(),a.getValue())).collect(Collectors.toList());

    }

    @GetMapping("/superVizorSensors/getSensorSessionLocationsBasedOnUser/{userId}")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public List<SensorLocationDTO> getSensorSessionLocationsBasedOnUser(@PathVariable Long userId){
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

        return sensorSessionss;}
    @GetMapping("/superVizorSensors/getSensorDatesAndSessionCounts/{userId}")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public    List<SensorDateCountDTO>  getDatesAndBasedOnTheirSessionCounts(@PathVariable Long userId){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
        User user = userService.findById(userId);

        List<SensorFix> sensorSessions = sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user);
        Map<LocalDate,String> dateInfo = new HashMap<>();
    //o tarih ve sayılarını almamız lazım
        Map<LocalDate,Long> nameCounts = sensorSessions.stream().map(sensor -> sensor.getCompletedTime()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        //aynısının statüslü değil de sensor isimlisini yapsak mantıklı olabilir aslında usera göre bulup
        return         nameCounts.entrySet().stream().map(a -> new SensorDateCountDTO(a.getKey(),a.getValue())).collect(Collectors.toList());

    }


    @GetMapping("/superVizorSensors/getWorkersPastSensors")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public List<SensorWithUserDTO> getWorkersPastSensors(){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
     List<SensorWithUserDTO> sensor = sensorService.getPastSensorsOfWorkers();

        return sensor;
    }

    @GetMapping("/superVizorSensors/getFaultyLocations")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public List<SensorLocationDTO> getFaultyLocationsOfAllSensors(){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
        List<SensorLocationDTO> sensorSessions = sensorSessionRepository.findAllSensorFixesWithFaultySensors().stream().map(a -> {
            SensorLocationDTO sensorLocationDTO = new SensorLocationDTO();

            sensorLocationDTO.setId(a.getId());
            sensorLocationDTO.setLatitude(a.getSensor().getSensorLocation().getLocation().getX());
            sensorLocationDTO.setLongitude(a.getSensor().getSensorLocation().getLocation().getY());
            return sensorLocationDTO;
        }).collect(Collectors.toList());

        return sensorSessions;
    }
    // yapacağımız şey gelen tarih versine göre ondan sonraki sessionsları alıcaz



    @GetMapping("/superVizorSensors/getWorkerStats")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public       List<Map<String, Map<String, Long>>>  getWorkerStats(){

        //tüm userların tüm sensorsessionları arasından çözülmüleri bulsak
        List<User> users = userRepository.findAll();
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
        //hayatımda böyle bişe görmedim

        Map<String,   Map<String, Long>> weekdata = new HashMap<>();
        Map<String, Map<String, Long> > monthData = new HashMap<>();

        Map<String, Map<String, Long> > dailydata = new HashMap<>();



        Map<String, Long> userSessionCountsOfLastWeeek = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user -> user.getSensorSessions().stream()
                                .filter(session ->
                                          session.getCompletedTime() != null &&
                                                    session.getCompletedTime().after(oneWeekAgo) &&
                                                    session.getSensor().getStatus() == SensorStatus.ACTIVE)
                                .count()
                ));

        Map<String, Long> userSessionCountsOfLastMonth = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user -> user.getSensorSessions().stream()
                                .filter(session ->
                                        session.getCompletedTime() != null &&
                                                session.getCompletedTime().after(oneMonthAgo) &&
                                                session.getSensor().getStatus() == SensorStatus.ACTIVE)
                                .count()
                ));

        Map<String, Long> userSessionCountsOfLastDay = users.stream()
                .collect(Collectors.toMap(
                        user -> user.getFirstName(),
                        user -> user.getSensorSessions().stream()
                                .filter(session ->
                                        session.getCompletedTime() != null &&
                                                session.getCompletedTime().after(oneDayAgo) &&
                                                session.getSensor().getStatus() == SensorStatus.ACTIVE)
                                .count()
                ));

        monthData.put("last_month",userSessionCountsOfLastMonth);

        weekdata.put("last_week",userSessionCountsOfLastWeeek);

        dailydata.put("last_day",userSessionCountsOfLastDay);
        result.add(monthData);

        result.add(weekdata);
        result.add(dailydata);

        return result;

    }
}
