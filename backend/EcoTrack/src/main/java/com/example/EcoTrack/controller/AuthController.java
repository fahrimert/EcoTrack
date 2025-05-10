package com.example.EcoTrack.controller;

import com.example.EcoTrack.dto.ApiResponse;
import com.example.EcoTrack.dto.SensorFixDTO;
import com.example.EcoTrack.dto.UserDTO;
import com.example.EcoTrack.dto.UserRequestDTO;
import com.example.EcoTrack.model.*;
import com.example.EcoTrack.repository.UserRepository;
import com.example.EcoTrack.service.*;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RefreshTokenService refreshTokenService;
    private TwoFactorTokenService twoFactorTokenService;
    private JwtService jwtService;
    private OTPService otpService;
    public AuthController(UserService userService, UserRepository userRepository, RefreshTokenService refreshTokenService, TwoFactorTokenService twoFactorTokenService, JwtService jwtService, OTPService otpService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.twoFactorTokenService = twoFactorTokenService;
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


    @PostMapping("/twofactorToken")
    public ResponseEntity<String> twoFactorTokenController(@RequestBody TwoFactorCode twoFactorCode) {

        return  twoFactorTokenService.verifyTwoFactorToken(twoFactorCode.getTwoFactorToken());
    }



}
