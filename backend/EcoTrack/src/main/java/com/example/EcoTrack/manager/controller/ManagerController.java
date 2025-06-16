package com.example.EcoTrack.manager.controller;

import com.example.EcoTrack.manager.dto.TaskCountDTO;
import com.example.EcoTrack.manager.service.ManagerService;
import com.example.EcoTrack.sensors.dto.AllSensorForManagerDTO;
import com.example.EcoTrack.sensors.dto.CreateSensorLocationRequestDTO;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO;
import com.example.EcoTrack.user.dto.UserAndSupervizorsDTO;
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

public ManagerService managerGraphService;
    public UserService userService;
    public SensorService sensorService;

    public ManagerController(ManagerService managerGraphService,UserService userService,SensorService sensorService) {
        this.managerGraphService= managerGraphService;
        this.userService = userService;
        this.sensorService = sensorService;
    }

    //Manager dashboard Graph Endpoints


    //Get The Bar Chart graph data  for Manager Dashboard Page
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

    //Get the sensor name counts for dougnhut component

    @GetMapping("/manager/getAllAssignedTaskStatusValuesForDoughnutComponent")
    @PreAuthorize("hasAuthority('manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public List<TaskCountDTO>  getAllSupervizorAssignedTaskNamesValuesForDoughnutComponent(){
        return  managerGraphService.getSensorNameCounts();

    }

    //Manager dashboard page radar chart component endpoints for supervizor
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

    //Manager dashboard page radar chart component endpoints for worker
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

    //Scatter Chart Component Endpoint
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

    //end of manager dashboard graph endpoints

    //Start of a User management page endpoints

    //Delete user by id endpoint

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

    //Deactivate User By id endpoint

    @PreAuthorize("hasAuthority('manager:delete')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @DeleteMapping("/manager/deactivateUser/{userId}")
    public void  deactivateUser(@PathVariable Long userId){
        userService.deactivateUserById(userId);
    }

    //user management page performance chart component endpoints

    @GetMapping("/manager/getTheSupervizorPerformanceCharts")
    @Transactional

    @PreAuthorize("hasAnyAuthority('manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public List<Map<String, Map<String, Long>>> getThePerformanceTableForSupervizor(){
        return  managerGraphService.getThePerformanceTableForSupervizor();
    }


    //end of a User Management page endpoints

    // start of  useGetAllSupervizorhook endpoint and usegetAllSupervizorsAndUsers endpoint
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

    // end of  useGetAllSupervizorhook endpoint and usegetAllSupervizorsAndUsers endpoint


    //start of an get all sensors for sensor management page endpoint
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

    @GetMapping("/sensors/sensormanagement/{sensorId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public ResponseEntity<ApiResponse> getJustDetailOfSensorForManagerManageSensorUsage(@PathVariable Long sensorId){
        return  sensorService.getJustDetailOfSensorForManagerManageSensorUsage(sensorId);
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


    @PreAuthorize("hasAuthority('manager:write')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @PostMapping("/manager/updateSensorLocations")
    public ResponseEntity<?> managerUpdateSensorLocation(CreateSensorLocationRequestDTO createSensorLocationRequestDTO){
        System.out.println(createSensorLocationRequestDTO.getLongitude());
        System.out.println(createSensorLocationRequestDTO.getLatitude());
        return  sensorService.managerUpdateSensorLocation(createSensorLocationRequestDTO);
    }

    //manager update Induvual Sensor
    @PreAuthorize("hasAuthority('manager:write')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @PostMapping("/manager/managerUpdateSensor")
    public ResponseEntity<?> managerUpdateSensor(@RequestParam String sensorId ,@RequestParam String sensorName, @RequestParam MultipartFile files){
        return  sensorService.managerUpdateInduvualSensor(sensorId,sensorName,files);
    }


    @PreAuthorize("hasAuthority('manager:delete')")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @DeleteMapping("/manager/deleteSensorById/{sensorId}")
    public void  deleteSensorById(@PathVariable Long sensorId){
        sensorService.deleteSensorById(sensorId);
    }



    //end  of an get all sensors for sensor management page endpoint







}
