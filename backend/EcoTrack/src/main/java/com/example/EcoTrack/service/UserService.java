package com.example.EcoTrack.service;

import com.example.EcoTrack.dto.*;
import com.example.EcoTrack.model.*;
import com.example.EcoTrack.repository.RefreshTokenRepository;
import com.example.EcoTrack.repository.TaskRepository;
import com.example.EcoTrack.repository.UserRepository;
import com.example.EcoTrack.security.customUserDetail.CustomUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.hibernate.Internal;
import org.locationtech.jts.geom.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private  final UserRepository userRepository;
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private  final TaskRepository taskRepository;

    private  final CustomUserDetailService userDetailServicee;
    private  final JwtService jwtService;
    private  final  RefreshTokenService refreshTokenService;
    private  final  OTPService otpService;
    private  final RefreshTokenRepository refreshTokenRepository;
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, TaskRepository taskRepository, CustomUserDetailService userDetailServicee1, JwtService jwtService, RefreshTokenService refreshTokenService, OTPService otpService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        this.authenticationManager = authenticationManager;
        this.taskRepository = taskRepository;
        this.userDetailServicee = userDetailServicee1;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.otpService = otpService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public User findByUsername(String username) {
        return userRepository.findByFirstName(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //ilk step bu aga biz 3. step falan yazmışız buna
    public ResponseEntity<ApiResponse<?>> login (@RequestBody UserRequestDTO user){

            User dbUser = userRepository.findByFirstName(user.getFirstName());
            if (dbUser == null ) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(
                                "Not Found",
                                List.of("User Not Found"),
                                HttpStatus.FORBIDDEN
                        ));

            }
        if (!dbUser.getEmail().equals(user.getEmail     ())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Error",
                            List.of("Credentials is wrong"),
                            HttpStatus.FORBIDDEN
                    ));
        }
        if (!dbUser.getFirstName().equals(user.getFirstName())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Error",
                            List.of("Credentials is wrong"),
                            HttpStatus.FORBIDDEN
                    ));
        }
        System.out.println(bCryptPasswordEncoder.matches(user.getPassword(),dbUser.getPassword()));
        if (!bCryptPasswordEncoder.matches(user.getPassword(),dbUser.getPassword())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Error",
                            List.of("Credentials is wrong"),
                            HttpStatus.FORBIDDEN
                    ));
        }
            if (dbUser.isTwoFactorAuthbeenverified() == false) {
                otpService.sendCodeToEmail(dbUser);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(
                                "2FA required",
                                List.of("Please check your email for 2 factor authentication"),
                                HttpStatus.FORBIDDEN
                        ));
            } else {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dbUser.getFirstName(),
                                user.getPassword()

                        )
                );

                UserDetails userDetails = userDetailServicee.loadUserByUsername(user.getFirstName());
                String token = jwtService.generateToken(userDetails.getUsername());
                String refreshToken = refreshTokenService.createRefreshToken(dbUser.getFirstName(), dbUser, userRepository);
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(ApiResponse.success(
                                Map.of(

                                        "accessToken", token,
                                        "refreshToken", refreshToken
                                )
                        ));

            }
        };

    public ResponseEntity<ApiResponse<Boolean>> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByFirstName(authentication.getName());

        if (authentication != null && authentication.isAuthenticated()){
            String token = request.getHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")){
                token = token.substring(7);
            }else{
                throw new RuntimeException("Access token does not exist");
            }

            RefreshToken refreshToken = refreshTokenService.findByUserId(user.getId());
            System.out.println(user);
            System.out.println(refreshToken.getUser().getFirstName());
            System.out.println(refreshToken.getId());

            user.setRefreshToken(null);
            userRepository.save(user);
            refreshTokenRepository.delete(refreshToken);
            new SecurityContextLogoutHandler().logout(request, response, authentication);

            return ResponseEntity.ok(ApiResponse.success(true));

        }
        else {
            throw new RuntimeException("User does not exists");
        }
    }

    public  List<Task> getAllTask(Long id){
        User user = userRepository.findById(id).orElse(null);
        return  user.getTasksIAssigned();
    }


    public  List<Task> getAllTaskOfMe(Long id){
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return Collections.emptyList();
        List<Task> incompleteTasks = user.getTasksAssignedToMe()
                .stream()
                .filter(task -> task.getTaskCompletedTime() == null)
                .collect(Collectors.toList());


        return  incompleteTasks;
    }
    public   List<UserOnlineStatusDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();

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

        return dtoList;
    }
    public   List<UserOnlineStatusDTO> getAllusersWithoutTasks() {

        List<User> allUsers = userRepository.findAll();

        List<UserOnlineStatusDTO> dtoList = allUsers.stream()
                .filter(user -> user.getSensorSessions() == null ||
                        user.getSensorSessions().stream()
                        .noneMatch(session -> session.getCompletedTime() == null))
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
        return dtoList;
    }

    public List<User> findAllByIds(List<Long> userIds) {
        return userRepository.findAllById(userIds);

    }
}
