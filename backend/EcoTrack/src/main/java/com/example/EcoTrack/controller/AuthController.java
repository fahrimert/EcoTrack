package com.example.EcoTrack.controller;

import com.example.EcoTrack.dto.*;
import com.example.EcoTrack.model.*;
import com.example.EcoTrack.repository.UserOnlineStatusRepository;
import com.example.EcoTrack.repository.UserRepository;
import com.example.EcoTrack.service.*;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RefreshTokenService refreshTokenService;
    private TwoFactorTokenService twoFactorTokenService;
    private UserOnlineStatusRepository userOnlineStatusRepository;
    private SimpMessagingTemplate messagingTemplate;
    private JwtService jwtService;
    private OTPService otpService;
    public AuthController(UserService userService, UserRepository userRepository, RefreshTokenService refreshTokenService, TwoFactorTokenService twoFactorTokenService, UserOnlineStatusRepository userOnlineStatusRepository, SimpMessagingTemplate messagingTemplate, JwtService jwtService, OTPService otpService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.twoFactorTokenService = twoFactorTokenService;
        this.userOnlineStatusRepository = userOnlineStatusRepository;
        this.messagingTemplate = messagingTemplate;
        this.jwtService = jwtService;
        this.otpService = otpService;
    }



    @PostMapping("/login")
        public ResponseEntity<ApiResponse<?>> login(@RequestBody UserRequestDTO loginRequest, HttpServletRequest request) {
        return userService.login(loginRequest);
    }

    @CrossOrigin(
            origins = "http://localhost:9595", // veya frontend URL’in
            allowedHeaders = "*",
            methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}
    )
    @PostMapping("/customLogout")
    public ResponseEntity<ApiResponse<Boolean>> logout( HttpServletRequest request,HttpServletResponse response) {
        return userService.logout(request,response);
    }

    @GetMapping("/user/{accessToken}")
    public Claims accessTokenController(HttpServletRequest request , HttpServletResponse response , @PathVariable String accessToken){
        return  jwtService.extractAllClaims(accessToken);
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



    @GetMapping("/user/profile/{accessToken}")
    @Transactional
    public UserDTO profileController(HttpServletRequest request , HttpServletResponse response , @PathVariable String accessToken){
        Claims claims =  jwtService.extractAllClaims(accessToken);

        User user = userService.findByUsername(claims.getSubject());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setFirstName(user.getSurName());
        userDTO.setRole(user.getRole());
        List<SensorFixDTO> sensorFixDTOList = user.getSensorSessions()
                .stream()
                .map(sensorFix -> {
                    Sensor sensor = sensorFix.getSensor();
                    SensorLocation location = sensor.getSensorLocation();

                    return new SensorFixDTO(
                            sensorFix.getId(),
                            sensor != null ? sensor.getSensorName() : null,
                            sensor != null ? sensor.getStatus().getDisplayName() : null,         // Eğer varsa
                            sensor != null ? sensor.getStatus().getColorCode() : null,         // Eğer varsa
                            sensorFix.getNote(),
                            sensorFix.getStartTime(),
                            sensorFix.getCompletedTime(),
                            location != null ? location.getLocation().getX() : 0.0,
                            location != null ? location.getLocation().getY() : 0.0
                    );
                })
                .collect(Collectors.toList());
            userDTO.setSensorSessions(sensorFixDTOList);


        return userDTO;

    }

    @PostMapping("/refreshToken/{refreshToken}")
    public String refreshTokenController(HttpServletRequest request, HttpServletResponse response, @PathVariable String refreshToken ){
return  refreshTokenService.findByToken(refreshToken,request,response);
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


    @PostMapping("/twofactorToken")
    public ResponseEntity<String> twoFactorTokenController(@RequestBody TwoFactorCode twoFactorCode) {

        return  twoFactorTokenService.verifyTwoFactorToken(twoFactorCode.getTwoFactorToken());
    }



}
