package com.example.EcoTrack.user.service;
import com.example.EcoTrack.notification.dto.EnrichedNotificationDTO;
import com.example.EcoTrack.notification.dto.NotificationDTO;
import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.model.SensorFix;
import com.example.EcoTrack.sensors.model.SensorLocation;
import com.example.EcoTrack.shared.dto.ApiResponse;
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
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Point;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.example.EcoTrack.user.dto.UserLocationDTO;
@Service
public class UserService {

    private  final UserRepository userRepository;
    private  final NotificationRepository notificationRepository;
    private  final JwtService jwtService;
    public UserService(UserRepository userRepository, NotificationRepository notificationRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.jwtService = jwtService;
    }
    private SensorFixDTO convertToSensorFixDTO(SensorFix sensorFix) {
        Sensor sensor = sensorFix.getSensor();

        double latitude = 0.0;
        double longitude = 0.0;

        if (sensor != null && sensor.getSensorLocation() != null) {
            latitude = sensor.getSensorLocation().getLocation().getX();
            longitude = sensor.getSensorLocation().getLocation().getY();
        }

        return SensorFixDTO.builder()
                .id(sensorFix.getId())
                .sensorName(sensor != null ? sensor.getSensorName() : "Bilinmiyor")
                .displayName(sensor != null ? sensor.getStatus().getDisplayName() : "Bilinmiyor")
                .color_code(sensor != null ? sensor.getStatus().getColorCode() : "#000000")
                .note(sensorFix.getNote())
                .startTime(sensorFix.getStartTime())
                .completedTime(sensorFix.getCompletedTime())
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
    //Get The Detail Of Logged In Worker function
    @Transactional
    public UserDTO getTheDetailOfALoggedInUser(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setSurName(user.getSurName());
        userDTO.setRole(user.getRole());
        List<SensorFixDTO> sensorFixDTOList = user.getSensorSessions()
                .stream()
                .map(this::convertToSensorFixDTO)
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
    Optional<User> user =   userRepository.findById(id);
    if (user.isPresent()){
        return  user.get();
    }

    else{
        throw  new RuntimeException();
    }

    }


    //delete user by id on user management page
    public ResponseEntity deleteUserById(Long id)
    {
        try {
            userRepository.flush();
            Optional<User> user = userRepository.findById(id);
            userRepository.flush();

            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(
                                "Error",
                                List.of("No user Found"),
                                HttpStatus.NOT_FOUND
                        ));
            }

            try {
                System.out.println("Silinmek istenen kullanıcı ID: " + user.get().getId());
                System.out.println("Veritabanında var mı? " + userRepository.findById(user.get().getId()));

                userRepository.flush();
                userRepository.deleteById(id);
                // Silme işlemi başarılı olduğunda
                return ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success(
                                "User Successfully deleted"
                        ));
            } catch (EmptyResultDataAccessException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(
                                "Error",
                                List.of("User could not be found for deletion"),
                                HttpStatus.NOT_FOUND
                        ));
            } catch (DataAccessException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error(
                                "Database Error",
                                List.of("Failed to delete user: " + e.getMessage()),
                                HttpStatus.INTERNAL_SERVER_ERROR
                        ));

            }

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Server Error",
                            List.of("Unexpected server error" + e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }

    public ResponseEntity deactivateUserById(Long id)
    {
        try {

            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()){
                User updatedUser = userOptional.get();

                if (updatedUser.getIsActive() == false){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error(
                                    "Error",
                                    List.of("User already deactivated"),
                                    HttpStatus.CONFLICT
                            ));
                }
                updatedUser.setIsActive(false);
                userRepository.save(updatedUser);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success(
                                "User Successfully deactivated"
                        ));


            }


            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(
                                "Error",
                                List.of("No user Found"),
                                HttpStatus.NOT_FOUND
                        ));

            }

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Server Error",
                            List.of("Unexpected server error" + e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }



    public User findByUsername(String username) {
        return userRepository.findByFirstName(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email))
                ;
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

    //start of fetch all managers
    public   List<UserOnlineStatusDTO> getAllManagers() {
        List<User> allUsers = userRepository.findAllByRole(Role.MANAGER);
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

    //get notifications for given worker id
    public List<EnrichedNotificationDTO> getEnrichedNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByReceiverId(userId);


        if (notifications.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> senderIds = notifications.stream()
                .map(Notification::getSenderId)
                .distinct()
                .collect(Collectors.toList());

        List<User> senders = userRepository.findAllByIdWithStatus(senderIds);


        Map<Long, UserOnlineStatusDTO> senderMap = senders.stream()
                .collect(Collectors.toMap(User::getId, user -> {
                    return new UserOnlineStatusDTO(
                            user.getId(),
                            user.getFirstName(),
                            user.getSurName(),
                            user.getRole(),
                            user.getUserOnlineStatus()
                    );
                }));

        return notifications.stream().map(notif -> {
            EnrichedNotificationDTO dto = new EnrichedNotificationDTO();

            dto.setId(notif.getId());
            dto.setSupervizorDescription(notif.getSupervizorDescription());
            dto.setSuperVizorDeadline(notif.getSuperVizorDeadline());
            dto.setCreatedAt(notif.getCreatedAt());
            dto.setNotificationType(notif.getType()); // Enum
            dto.setSenderId(notif.getSenderId());
            dto.setReceiverId(notif.getReceiverId());
            dto.setTaskId(notif.getTaskId());
            dto.setIsread(notif.getIsRead());

            dto.setSender(senderMap.get(notif.getSenderId()));

            return dto;
        }).collect(Collectors.toList());
    }


}

