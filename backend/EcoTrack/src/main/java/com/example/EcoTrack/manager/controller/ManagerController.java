package com.example.EcoTrack.controller;

import com.example.EcoTrack.dto.*;
import com.example.EcoTrack.service.ManagerGraphService;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO;
import com.example.EcoTrack.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ManagerController {

public ManagerGraphService managerGraphService;
    public UserService userService;
    public SensorService sensorService;

    public ManagerController(ManagerGraphService managerGraphService,UserService userService,SensorService sensorService) {
        this.managerGraphService= managerGraphService;
        this.userService = userService;
        this.sensorService = sensorService;
    }

    @GetMapping("/manager/getSuperVizorTasks")
    @PreAuthorize("hasAuthority('manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public List<Map<String, Map<String, Long>>> getSuperVizorTasks(){

        return  managerGraphService.getSuperVizorTasks();
    }

    @GetMapping("/manager/getAverageTaskMinsOfLastMonth")
    @Transactional
    @PreAuthorize("hasAnyAuthority('supervisor:get','manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public        Map<String, Map<String, Long>>  getSensorSessionsOfLastMonth(){
        return  managerGraphService.getTheAverageTaskCompletedTimeForWorkerChart();

    }

    @GetMapping("/manager/getTheSupervizorPerformanceCharts")
    @Transactional

    @PreAuthorize("hasAnyAuthority('manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public List<Map<String, Map<String, Long>>>     getThePerformanceTableForSupervizor(){
        return  managerGraphService.getThePerformanceTableForSupervizor();

    }


    @GetMapping("/manager/getSupervizorsAssignedTaskStatusValues")
    @PreAuthorize("hasAuthority('manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public List<TaskCountDTO>  getAllSensorStatusMetricValues(){
        return  managerGraphService.getSensorNameCounts();

    }
    @PreAuthorize("hasAuthority('manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @GetMapping("/manager/getAllSupervizors")
    public  List<UserOnlineStatusDTO> getAllSuperVizor(){

        return  userService.getAllSuperVizor();
    }

    @PreAuthorize("hasAuthority('manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @GetMapping("/manager/getAllSupervizorsAndUsers")
    public  List<UserAndSupervizorsDTO> getAllSupervizorsAndUsers(){

        return  userService.getAllSupervizorAndWorker();
    }


    @PreAuthorize("hasAuthority('manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @GetMapping("/manager/getAllSensorForManagerUse")
    public  List<AllSensorForManagerDTO> getAllSensorForManagerUse(){

        return  sensorService.getAllSensorForManagerUse();
    }



    @PreAuthorize("hasAuthority('manager:delete')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @DeleteMapping("/manager/deleteUserById/{userId}")
    public void  deleteUserById(@PathVariable Long userId){
        userService.deleteUserById(userId);
    }


    @PreAuthorize("hasAuthority('manager:delete')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @DeleteMapping("/manager/deactivateUser/{userId}")
    public void  deactivateUser(@PathVariable Long userId){
        userService.deleteUserById(userId);
    }


    @PreAuthorize("hasAuthority('manager:write')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @PostMapping("/manager/managerCreateSensor")
    public ResponseEntity<?> managerCreateSensor(@RequestParam String sensorName, @RequestParam MultipartFile files){
        return  sensorService.managerCreateSensor(sensorName,files);
    }


//    @PreAuthorize("hasAuthority('manager:write')")
//    @CrossOrigin(
//            origins = "http://localhost:9595", // veya frontend URL’in
//            allowedHeaders = "*",
//            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
//    )
//    @PostMapping("/manager/updateSensorLocations")
//    public ResponseEntity<?> managerUpdateSensorLocation(CreateSensorLocationRequestDTO createSensorLocationRequestDTO){
//        System.out.println(createSensorLocationRequestDTO.getLongitude());
//        System.out.println(createSensorLocationRequestDTO.getLatitude());
//        return  sensorService.getJustDetailOfSensorForManagerManageSensorUsage(createSensorLocationRequestDTO);
//    }




    @GetMapping("/manager/getSuperVizorPropertiesForRadarChart/{userId}")
    @PreAuthorize("hasAuthority('manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public   List<Map<String, Long>> getSuperVizorPropertiesForRadarChart(@PathVariable Long userId){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
        return  managerGraphService.getSuperVizorPropertiesForRadarChart(userId);
    }

    @GetMapping("/manager/getWorkerPropertiesForRadarChart/{userId}")
    @PreAuthorize("hasAuthority('manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public   List<Map<String, Long>> getWorkerPropertiesForRadarChart(@PathVariable Long userId){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
        return  managerGraphService.getWorkerPropertiesForRadarChart(userId);
    }


}
