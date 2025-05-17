package com.example.EcoTrack.controller;

import com.example.EcoTrack.dto.SensorDTO;
import com.example.EcoTrack.dto.SensorLocationDTO;
import com.example.EcoTrack.dto.SensorWithUserDTO;
import com.example.EcoTrack.model.*;
import com.example.EcoTrack.repository.SensorSessionRepository;
import com.example.EcoTrack.repository.UserRepository;
import com.example.EcoTrack.service.SensorService;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")

public class SuperVizorBasedController {

    private  final SensorSessionRepository sensorSessionRepository;
    private  final  SensorService sensorService;
    private  final UserRepository userRepository;
    public SuperVizorBasedController(SensorSessionRepository sensorSessionRepository, SensorService sensorService, UserRepository userRepository) {
        this.sensorSessionRepository = sensorSessionRepository;
        this.sensorService = sensorService;
        this.userRepository = userRepository;
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
        List<String> sensorSession = sensorSessions.stream().map(a -> a.getSensor().getStatus().getDisplayName()).collect(Collectors.toList());

        Map<SensorStatus,Long> statusCounts = sensorSessions.stream().map(sensor -> sensor.getSensor().getStatus())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        return statusCounts;

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
