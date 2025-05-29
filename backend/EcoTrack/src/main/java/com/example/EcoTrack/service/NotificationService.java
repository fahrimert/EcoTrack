package com.example.EcoTrack.service;

import com.example.EcoTrack.dto.NotificationDTO;
import com.example.EcoTrack.dto.SensorLocationDTO;
import com.example.EcoTrack.model.Notification;
import com.example.EcoTrack.model.User;
import com.example.EcoTrack.repository.*;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service

public class NotificationService {

    public NotificationRepository notificationRepository;
    public  UserService userService;
    private UserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public NotificationService(UserService userService, NotificationRepository notificationRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.userService = userService;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(Notification notification){
            Notification notificationn = new Notification();



            notificationn.setSupervizorDescription(notification.getSupervizorDescription());
            notificationn.setSuperVizorDeadline(notification.getSuperVizorDeadline());
            notificationn.setUserNotifications(notification.getUserNotifications());
            notificationn.setReceiverId(notification.getReceiverId());
            notificationn.setCreatedAt(LocalDateTime.now());
            notificationn.setSenderId(notification.getSenderId());
            notificationn.setTaskId(notification.getTaskId());
            notificationn.setSenderId(notification.getSenderId());

        Notification savedNotification = notificationRepository.save(notificationn);

            NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setId(savedNotification.getId());
        notificationDTO.setSupervizorDescription(savedNotification.getSupervizorDescription());
        notificationDTO.setSuperVizorDeadline(savedNotification.getSuperVizorDeadline());
        notificationDTO.setCreatedAt(savedNotification.getCreatedAt());
        notificationDTO.setSenderId(savedNotification.getSenderId());
        notificationDTO.setReceiverId(savedNotification.getReceiverId());
        notificationDTO.setTaskId(savedNotification.getTaskId());
        notificationDTO.setIsread(savedNotification.getIsRead());



        messagingTemplate.convertAndSend(
                "/topic/notifications/" + notification.getReceiverId(),
                notificationDTO
        );

    }


    public ResponseEntity<?> getNotificationById( Long userId){

        List<NotificationDTO> notificationDTOS =notificationRepository.findByReceiverId(userId).stream().map(a -> {
                    NotificationDTO notificationDTO = new NotificationDTO();
                    notificationDTO.setId(a.getId());
                    notificationDTO.setSupervizorDescription(a.getSupervizorDescription());
                    notificationDTO.setSuperVizorDeadline(a.getSuperVizorDeadline());
                    notificationDTO.setCreatedAt(a.getCreatedAt());
                    notificationDTO.setSenderId(a.getSenderId());
                    notificationDTO.setReceiverId(a.getReceiverId());
                    notificationDTO.setTaskId(a.getTaskId());
                    notificationDTO.setIsread(a.getIsRead());

                    return notificationDTO;
                })
                .collect(Collectors.toList());
        return  ResponseEntity.ok(notificationDTOS);

    }

}
