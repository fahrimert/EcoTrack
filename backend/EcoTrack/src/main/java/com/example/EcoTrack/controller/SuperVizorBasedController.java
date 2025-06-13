package com.example.EcoTrack.controller;

import com.example.EcoTrack.dto.*;
import com.example.EcoTrack.model.*;
import com.example.EcoTrack.repository.SensorSessionRepository;
import com.example.EcoTrack.repository.UserRepository;
import com.example.EcoTrack.service.SensorService;
import com.example.EcoTrack.service.SuperVizorGraphsService;
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
    private final SuperVizorGraphsService superVizorGraphsService;
    public SuperVizorBasedController(SensorSessionRepository sensorSessionRepository, SensorService sensorService, UserRepository userRepository, UserService userService, SuperVizorGraphsService superVizorGraphsService) {
        this.sensorSessionRepository = sensorSessionRepository;
        this.sensorService = sensorService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.superVizorGraphsService = superVizorGraphsService;
    }


    @GetMapping("/superVizorSensors/getSensorsFiltersBasedStat")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public
    Map<SensorStatus,java. lang. Long>  getAllSensorStatusMetricValues(){
        return  superVizorGraphsService.getSensorStatusesMetricValues();

    }
    @PreAuthorize("hasAnyAuthority('supervisor:get','manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @GetMapping("/superVizorSensors/getAllWorker")
    public  List<UserOnlineStatusDTO> getAllWorker(){
        return  userService.getAllWorker();
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


    @GetMapping("/superVizorSensors/getScatterPlotGraphDataOfWorkerTasks/{userId}")
    @Transactional
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    //buradaki mantık task bitirmiş  userları listeleyip onların ortalama task çözme sürelerini isimleriyle almak
    public         Map<Long, Map<String, Object>>  getSensorSessionsOfLastMonth(@PathVariable Long userId){
        return  superVizorGraphsService.getSensorSessionsOfLastMonth(userId);

    }
    @GetMapping("/superVizorSensors/getTheUserPerformanceCharts")
    @Transactional

    @PreAuthorize("hasAnyAuthority('supervisor:get','manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public List<Map<String, Map<String, Long>>>     getTheAverageCompletedTimeForUserChart(){
        return  superVizorGraphsService.getThePerformanceTableForWorker();

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
   return  superVizorGraphsService.getSensorNameCounts(userId);
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
        return  superVizorGraphsService.getSensorSessionLocationsBasedOnUser(userId);
    }
    @GetMapping("/superVizorSensors/getSensorDatesAndSessionCounts/{userId}")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public    List<SensorDateCountDTO>  getDatesAndBasedOnTheirSessionCounts(@PathVariable Long userId){
       return  superVizorGraphsService.getDatesAndBasedOnTheirSessionCounts(userId);
    }


    @GetMapping("/superVizorSensors/getWorkersPastSensors")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public List<SensorWithUserProjection> getWorkersPastSensors(){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
     List<SensorWithUserProjection> sensor = sensorService.getPastSensorsOfWorkers();

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
     return  superVizorGraphsService.getFaultyLocationsOfAllSensors();
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

        return  superVizorGraphsService.getWorkerStats();

    }
}
