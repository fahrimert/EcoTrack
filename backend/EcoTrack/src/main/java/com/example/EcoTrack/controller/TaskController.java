package com.example.EcoTrack.controller;

import com.example.EcoTrack.dto.ApiResponse;
import com.example.EcoTrack.model.SensorStatus;
import com.example.EcoTrack.model.Task;
import com.example.EcoTrack.service.NotificationService;
import com.example.EcoTrack.service.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Tüm origin'lere izin ver

public class TaskController {
    private TaskService taskService;
    private NotificationService notificationService;

    public TaskController(TaskService taskService, NotificationService notificationService) {
        this.taskService = taskService;
        this.notificationService = notificationService;
    }


    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @PreAuthorize("hasAuthority('supervisor:write')")
        @PostMapping("/tasks/createTask")
    @Transactional
    public ResponseEntity<?> createTask (@RequestBody Task task) {


        return  taskService.createTask(task);

    }

    //supervizore dair task listesini alalım aslında şuanki kullanıcıya dair yani buraya da zaten sadece supervizor erişebildiği için
    //supervizore dair olmuş oluyor

    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @GetMapping("/notifications/getNotifications/{userId}")
    @Transactional
    public ResponseEntity<?> getNotificationById (@PathVariable Long userId) {


        return  notificationService.getNotificationById(userId);

    }

    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @PreAuthorize("hasAuthority('supervisor:write')")
    @GetMapping("/tasks/getTasks")
    @Transactional
    public ResponseEntity<?> getTasks () {


        return  taskService.getTasks();

    }


    @CrossOrigin(
            origins = "http://localhost:9595",
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @GetMapping("/tasks/getTasksOfMe/{userId}")
    @Transactional
    public ResponseEntity<?> getSensorListFromTasksOfSingleUser (@PathVariable Long userId) {


        return  taskService.getSensorListFromTasksOfSingleUser(userId);

    }


    @PutMapping("/task/updateOnRoad/{taskId}")
    @CrossOrigin(
            origins = "http://localhost:9595", 
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional

    public ResponseEntity<ApiResponse<?>>  updateTasksOnRoadNote(@RequestBody String workerNote, @PathVariable Long taskId){

   return taskService.updateTasksOnRoadNote(taskId,workerNote);
    }

        @PutMapping("/task/updateMarkIsRead/{userId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional

    public ResponseEntity<?>  updateMarkIsRead(@PathVariable Long userId){

        return taskService.markNotificationsOfRead(userId);
    }



    @PutMapping("/sensor/finishTask/{taskId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    public void updateTaskToFinal(@RequestParam String solvingNote, @PathVariable Long taskId, @RequestParam List<MultipartFile> files){
        taskService.finishTask(solvingNote,taskId,files);
    }


}
