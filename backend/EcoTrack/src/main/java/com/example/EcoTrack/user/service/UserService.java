package com.example.EcoTrack.user.service;
import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorLocation;
import com.example.EcoTrack.shared.dto.SensorFixDTO;
import com.example.EcoTrack.task.model.Task;
import com.example.EcoTrack.user.dto.UserAndSupervizorsDTO;
import com.example.EcoTrack.user.dto.UserOnlineStatusDTO;
import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.dto.UserDTO;
import com.example.EcoTrack.user.model.User;
import io.jsonwebtoken.Claims;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.example.EcoTrack.user.dto.UserLocationDTO;
@Service
public class UserService {

    private  final UserRepository userRepository;
    private  final JwtService jwtService;
    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    //Get The Detail Of Logged In Worker function
    public UserDTO getTheDetailOfLoggedInWorker(String accessToken){
        Claims claims =  jwtService.extractAllClaims(accessToken);

        User user = findByUsername(claims.getSubject());

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
                            sensor != null ? sensor.getStatus().getDisplayName() : null,
                            sensor != null ? sensor.getStatus().getColorCode() : null,
                            sensorFix.getNote(),
                            sensorFix.getStartTime(),
                            sensorFix.getCompletedTime(),
                            location != null ? location.getLocation().getX() : 0.0,
                            location != null ? location.getLocation().getY() : 0.0
                    );
                })
                .collect(Collectors.toList());
        userDTO.setSensorSessions(sensorFixDTOList);
    return  userDTO;
    }


    //Get The Location Of User Based On Their Id
    public UserLocationDTO getTheUserLocationBasedOnTheirId(Long id) {
        User user = userRepository.findById(id).orElse(null);

        Point point =  user.getUserLocation().getLocation();

        return new UserLocationDTO(user.getId(),point.getY(), point.getX());
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    //delete user by id on user management page
    public void deleteUserById(Long id)
    {
        try {
            userRepository.deleteById(id);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void deactivateUserById(Long id)
    {
        try {
            User userOptional = findById(id);
            userOptional.setIsActive(false);
            userRepository.save(userOptional);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



    public User findByUsername(String username) {
        return userRepository.findByFirstName(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
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

    public List<UserOnlineStatusDTO> getAllSuperVizor(){
        List<User> allSupervizor = userRepository.findAllByRole(Role.SUPERVISOR);
        List<UserOnlineStatusDTO> dtoList = allSupervizor.stream()
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

    public List<UserAndSupervizorsDTO> getAllSupervizorAndWorker(){
        List<User> allUsers = userRepository.findAllExceptManager();
        List<UserAndSupervizorsDTO> dtoList = allUsers.stream()
                .map(userItem -> {
                    UserAndSupervizorsDTO dto = new UserAndSupervizorsDTO();
                    dto.setId(userItem.getId());
                    dto.setFirstName(userItem.getFirstName());
                    dto.setSurName(userItem.getSurName());
                    dto.setRole(userItem.getRole());
                    dto.setEmail(userItem.getEmail());
                    dto.setLastLoginTime(userItem.getLastLoginTime());
                    dto.setLastOnlineTime(
                            userItem.getUserOnlineStatus() != null && userItem.getUserOnlineStatus().getLastOnlineTime() != null
                                    ? userItem.getUserOnlineStatus().getLastOnlineTime()
                                    : LocalDateTime.now()
                    );
                    return dto;
                })
                .collect(Collectors.toList());

        return dtoList;

    }


    public List<User> findAllByIds(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }



}

