package com.example.EcoTrack.announecements.service;

import com.example.EcoTrack.announecements.model.Announcement;
import com.example.EcoTrack.announecements.repository.AnnounecementsRepository;
import com.example.EcoTrack.notification.dto.ManagerNotificationDTO;
import com.example.EcoTrack.notification.dto.NotificationDTO;
import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.pdfReports.model.PdfReports;
import com.example.EcoTrack.pdfReports.repository.PdfRepository;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service

public class AnnounecementService {

    public NotificationRepository notificationRepository;
    public UserService userService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private  final AnnounecementsRepository announecementsRepository;
    private  final PdfRepository pdfRepository;

    public AnnounecementService(UserService userService, NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate, AnnounecementsRepository announecementsRepository, PdfRepository pdfRepository) {
        this.userService = userService;
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
        this.announecementsRepository = announecementsRepository;
        this.pdfRepository = pdfRepository;
    }


    //Sending annonucement
    public void sendAnnounecement(Announcement announcement){
            Announcement announecement = new Announcement();

        Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
        String username = securityContextHolder.getName();


        User announcedBy = userService.findByUsername(username);

        announecement.setTitle(announecement.getTitle());
        announecement.setContent(announecement.getContent());
        announecement.setReceiverId(announecement.getReceiverId());
        announecement.setSenderId(announcedBy.getId());
        announecement.setUserAnnonucements(announcedBy);

        Announcement announcements = announecementsRepository.save(announcement);

        messagingTemplate.convertAndSend(
                "/topic/announecements/" + announcements.getReceiverId(),
                announcements
        );

    }

    //Mark the notification true in worker dashboard notification component
//    public  ResponseEntity<?> markNotificationsOfRead(Long userId){
//        User user = userService.findById(userId);
//        List<Notification> notifications = notificationRepository.findByReceiverIdAndIsReadFalse(userId);
//        for (Notification n : notifications) {
//            n.setIsRead(true);
//        }
//        notificationRepository.saveAll(notifications);
//        return ResponseEntity.ok().build();
//    }
//
//
//    //get notifications for given worker id
//    public ResponseEntity<?> getNotificationById( Long userId){
//
//        List<NotificationDTO> notificationDTOS =notificationRepository.findByReceiverId(userId).stream().map(a -> {
//                    NotificationDTO notificationDTO = new NotificationDTO();
//                    notificationDTO.setId(a.getId());
//                    notificationDTO.setSupervizorDescription(a.getSupervizorDescription());
//                    notificationDTO.setSuperVizorDeadline(a.getSuperVizorDeadline());
//                    notificationDTO.setCreatedAt(a.getCreatedAt());
//                    notificationDTO.setSenderId(a.getSenderId());
//                    notificationDTO.setReceiverId(a.getReceiverId());
//                    notificationDTO.setTaskId(a.getTaskId());
//                    notificationDTO.setIsread(a.getIsRead());
//
//                    return notificationDTO;
//                })
//                .collect(Collectors.toList());
//        return  ResponseEntity.ok(notificationDTOS);
//
//    }
//
//    public ResponseEntity<?> getNotificationByManagerId( Long userId){
//
//        List<ManagerNotificationDTO> notificationDTOS =notificationRepository.findByReceiverId(userId).stream().map(a -> {
//                    ManagerNotificationDTO managerNotificationDTO = new ManagerNotificationDTO();
//                    PdfReports pdfReports = pdfRepository.findById(a.getPdfReportId()).orElseThrow();
//
//                    managerNotificationDTO.setId(a.getId());
//                    managerNotificationDTO.setIsread(a.getIsRead());
//                    managerNotificationDTO.setSenderName(pdfReports.getSupervisor().getFirstName());
//                    managerNotificationDTO.setSensorName(pdfReports.getSensorName());
//
//                    return managerNotificationDTO;
//                })
//                .collect(Collectors.toList());
//        return  ResponseEntity.ok(notificationDTOS);
//
//    }

}
