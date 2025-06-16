package com.example.EcoTrack.user.controller;

import com.example.EcoTrack.dto.*;
import com.example.EcoTrack.user.*;
import com.example.EcoTrack.user.dto.UserDTO;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.model.UserOnlineStatus;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.user.repository.UserOnlineStatusRepository;
import com.example.EcoTrack.user.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserController {
    private UserService userService;
    private UserRepository userRepository;
    private UserOnlineStatusRepository userOnlineStatusRepository;
    private SimpMessagingTemplate messagingTemplate;
    private JwtService jwtService;
    public UserController(UserService userService, UserRepository userRepository,   UserOnlineStatusRepository userOnlineStatusRepository, SimpMessagingTemplate messagingTemplate, JwtService jwtService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userOnlineStatusRepository = userOnlineStatusRepository;
        this.messagingTemplate = messagingTemplate;
        this.jwtService = jwtService;
    }

    //Currently Logged In User Detail Data
    @GetMapping("/user/profile/{accessToken}")
    @Transactional
    public UserDTO profileController(HttpServletRequest request , HttpServletResponse response , @PathVariable String accessToken){
        return userService.getTheDetailOfLoggedInUser(accessToken);
    }


    @GetMapping("/getUserLocationBasedOnıd/{userId}")
    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @Transactional

    public UserLocationDTO getLocationBasedOnId(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        Point point =   user.getUserLocation().getLocation();

        return new UserLocationDTO(user.getId(),point.getY(), point.getX());
    }

    @GetMapping("/user/role/{accessToken}")
    @Transactional
    public String getUserRoleController(HttpServletRequest request , HttpServletResponse response , @PathVariable String accessToken){
        Claims claims =  jwtService.extractAllClaims(accessToken);

        User user = userService.findByUsername(claims.getSubject());



        return user.getRole().getDisplayName();

    }




    @PostMapping("/user/getProfilesOfUsers")
    @Transactional
    public List<UserOnlineStatusDTO> getProfilesOfUsers(@RequestBody List<Long> userIds) {
        List<User> users = userService.findAllByIds(userIds);

        return users.stream().map(user -> {
            UserOnlineStatusDTO dto = new UserOnlineStatusDTO();
            dto.setId(user.getId());
            dto.setFirstName(user.getFirstName());
            dto.setSurName(user.getSurName());
            dto.setRole(user.getRole());
            dto.setUserOnlineStatus(user.getUserOnlineStatus());
            return dto;
        }).collect(Collectors.toList());
    }








    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
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
