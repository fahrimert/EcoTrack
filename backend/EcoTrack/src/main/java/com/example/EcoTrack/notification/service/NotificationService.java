package com.example.EcoTrack.notification.service;

import com.example.EcoTrack.notification.dto.ManagerNotificationDTO;
import com.example.EcoTrack.notification.dto.NotificationDTO;
import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.pdfReports.model.PdfReports;
import com.example.EcoTrack.pdfReports.repository.PdfRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.service.UserService;
import org.apache.catalina.Manager;
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
    public UserService userService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private  final PdfRepository pdfRepository;

    public NotificationService(UserService userService, NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate, PdfRepository pdfRepository) {
        this.userService = userService;
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
        this.pdfRepository = pdfRepository;
    }


    //Sending notification after receiving task module
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
                notificationn.setType(notification.getType());

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
            notificationDTO.setNotificationType(savedNotification.getType());


            messagingTemplate.convertAndSend(
                    "/topic/notifications/" + notification.getReceiverId(),
                    notificationDTO
            );

        }

    //Mark the notification true in worker dashboard notification component
        public  ResponseEntity<?> markNotificationsOfRead(Long userId){
            List<Notification> notifications = notificationRepository.findAll().stream()
                    .filter(notification -> Boolean.FALSE.equals(notification.getIsRead()))
                    .toList();
            for (Notification n : notifications) {
                n.setIsRead(true);
            }
            notificationRepository.saveAll(notifications);
            return ResponseEntity.ok().build();
        }



    public ResponseEntity getNotificationByManagerId( Long userId){

        List<ManagerNotificationDTO> notificationDTOS =notificationRepository.findByReceiverId(userId).stream().map(a -> {
                    ManagerNotificationDTO managerNotificationDTO = new ManagerNotificationDTO();
                    PdfReports pdfReports = pdfRepository.findById(a.getPdfReportId()).orElseThrow();

                    managerNotificationDTO.setId(a.getId());
                    managerNotificationDTO.setIsread(a.getIsRead());
                    managerNotificationDTO.setSenderName(pdfReports.getSupervisor().getFirstName());
                    managerNotificationDTO.setSensorName(pdfReports.getSensorName());

                    return managerNotificationDTO;
                })
                .collect(Collectors.toList());
        return  ResponseEntity.ok(notificationDTOS);

    }

}
