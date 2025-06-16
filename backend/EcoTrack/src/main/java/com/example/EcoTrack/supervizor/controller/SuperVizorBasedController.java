package com.example.EcoTrack.controller;

import com.example.EcoTrack.dto.*;
import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
import com.example.EcoTrack.sensors.model.SensorStatus;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.task.service.TaskService;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.service.SuperVizorService;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO;
import com.example.EcoTrack.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")

public class SuperVizorBasedController {

    private  final SensorSessionRepository sensorSessionRepository;
    private  final  SensorService sensorService;
    private  final UserRepository userRepository;
    private  final UserService userService;
    private final SuperVizorService superVizorService;
    private TaskService taskService;

    public SuperVizorBasedController(SensorSessionRepository sensorSessionRepository, SensorService sensorService, UserRepository userRepository, UserService userService, SuperVizorService superVizorService, TaskService taskService) {
        this.sensorSessionRepository = sensorSessionRepository;
        this.sensorService = sensorService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.superVizorService = superVizorService;
        this.taskService = taskService;
    }

    //Supervizor dashboard Graph Endpoints

    //Get the sensor status counts for dougnhut component
    @GetMapping("/superVizor/getAllSensorStatusMetricValuesForDoughnutComponent")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public
    Map<SensorStatus,java. lang. Long>  getAllSensorStatusMetricValuesForDoughnutComponent(){
        return  superVizorService.getAllSensorStatusMetricValuesForDoughnutComponent();

    }

    //Get The Bar Chart graph data  for Supervizor Dashboard Page
    @GetMapping("/superVizor/getTimeBasedSessionWorkerStatsForBarChartData")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public       List<Map<String, Map<String, Long>>>  getTimeBasedSessionWorkerStatsForBarChartData(){

        return  superVizorService.getTimeBasedSessionWorkerStatsForBarChartData();

    }
    //supervizor dashboard page heatmap component endpoint
    @GetMapping("/superVizor/getFaultyLocationsForSupervizorDashboardHeatmapComponent")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public List<SensorLocationDTO> getFaultyLocationsForSupervizorDashboardHeatmapComponent(){
        return  superVizorService.getFaultyLocationsForSupervizorDashboardHeatmapComponent();
    }

    //end of supervizor dashboard graph endpoints

    //Supervizor worker past sensors page endpoint

    @GetMapping("/superVizor/getWorkersPastSensors")
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

    // end of  past sensors page endpoint


    //endpoint for useAllUsersHook
    @PreAuthorize("hasAnyAuthority('supervisor:get','manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )

    @GetMapping("/supervizor/getAllWorker")
    public  List<UserOnlineStatusDTO> getAllWorker(){
        return  userService.getAllWorker();
    }
    //end of  useAllUsersHook

    //Workers-performance-analysis-charts page endpoints

    //Scatter Plot Graph Endpoint
    @GetMapping("/supervizor/getScatterPlotGraphDataOfWorkerTasks/{userId}")
    @Transactional
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    //buradaki mantık task bitirmiş  userları listeleyip onların ortalama task çözme sürelerini isimleriyle almak
    public         Map<Long, Map<String, Object>>  getScatterPlotGraphDataOfWorkerTasks(@PathVariable Long userId){
        return  superVizorService.getSensorSessionsOfLastMonth(userId);
    }

    //Get The workers non-task session solving Sensor Name counts for worker-performance-analysis-chart page dougnut component
    @GetMapping("/supervizor/getNonTaskSessionSolvingSensorNames/{userId}")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public    List<SensorCountDTO>  getNonTaskSessionSolvingSensorNames(@PathVariable Long userId){
        //tüm sensor sessionlarının sensorlerindeki statlara göre sayıları arttırmamız gerekiyor aslında
        return  superVizorService.getNonTaskSessionSolvingSensorNames(userId);
    }

    //Get The workers non-task session solving dates and solving counts based on their dates
    @GetMapping("/superVizorSensors/getSensorDatesAndSessionCounts/{userId}")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public    List<SensorDateCountDTO>  getDatesAndBasedOnTheirSessionCounts(@PathVariable Long userId){
        return  superVizorService.getDatesAndBasedOnTheirSessionCounts(userId);
    }

    //Get The workers faulty status heatmap component for supervizor dashboard page for selected workerId
    @GetMapping("/supervizor/getNonTaskHeatmapComponent/{userId}")
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional
    public List<SensorLocationDTO> getNonTaskHeatmapComponent(@PathVariable Long userId){
        return  superVizorService.getNonTaskHeatmapComponentForSupervizorDashboardPage(userId);
    }


    //Get The LeaderBoard Data for Leaderboard component in user performance analysis chart page
    @GetMapping("/superVizor/getTheLeaderBoardTable")
    @Transactional

    @PreAuthorize("hasAnyAuthority('supervisor:get','manager:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public List<Map<String, Map<String, Long>>>     getTheAverageCompletedTimeForUserChart(){
        return  superVizorService.getThePerformanceTableForWorker();

    }
    //end of user performance analysis chart endpoints

    //Supervizor Task assigning page endpoint
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @PreAuthorize("hasAuthority('supervisor:write')")
    @PostMapping("/superVizor/createTask")
    @Transactional
    public ResponseEntity<?> supervizorCreateTaskForWorker (@RequestBody Task task) {


        return  taskService.supervizorCreateTaskForWorker(task);

    }

    //get tasks of ı assigned for supervizor assign task page use purposes
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @PreAuthorize("hasAuthority('supervisor:write')")
    @GetMapping("/supervizor/getTasksOfIAssigned")
    @Transactional
    public ResponseEntity<?> supervizorGetTasksOfIAssigned () {


        return taskService.getTasksOfIAssigned();
    }

    //Supervizor get available sensors for assignin task select component
    @PreAuthorize("hasAuthority('supervisor:get')")
    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @GetMapping("/supervizor/getAllAvailableSensorsForAssigningTaskSelectComponent")
    @Transactional

    public  List<SensorDTO> getAllAvailableSensorsForAssigningTaskSelectComponent(){
        return  sensorService.getAllAvailableSensorsForAssigningTaskSelectComponent();
    }
    //Supervizor End Of Task assigning page endpoint




}
