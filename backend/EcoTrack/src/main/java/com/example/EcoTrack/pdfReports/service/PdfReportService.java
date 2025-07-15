package com.example.EcoTrack.pdfReports.service;

import com.example.EcoTrack.notification.dto.ManagerNotificationDTO;
import com.example.EcoTrack.notification.dto.NotificationDTO;
import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.notification.type.NotificationType;
import com.example.EcoTrack.pdfReports.dto.PdfRequestDTO;
import com.example.EcoTrack.pdfReports.model.PdfReports;
import com.example.EcoTrack.pdfReports.repository.PdfRepository;
import com.example.EcoTrack.sensors.model.Sensor;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service

    public class PdfReportService {
        private final PdfRepository pdfRepository;
        private  final UserService userService;
        private  final SimpMessagingTemplate messagingTemplate;
        private  final NotificationRepository notificationRepository;
        private  final SensorRepository sensorRepository;
        public PdfReportService(PdfRepository pdfRepository, UserService userService, SimpMessagingTemplate messagingTemplate, NotificationRepository notificationRepository, SensorRepository sensorRepository) {
            this.pdfRepository = pdfRepository;
            this.userService = userService;
            this.messagingTemplate = messagingTemplate;
            this.notificationRepository = notificationRepository;
            this.sensorRepository = sensorRepository;
        }

        //supervizor create pdfReport and send it to the   manager

        public ResponseEntity createPdfAndSendNotificationToManager (PdfRequestDTO pdfRequest){

            try {


                PdfReports pdfReports = new PdfReports();
                boolean reportExists = pdfRepository.existsByStartTimeAndSupervisorId(
                        pdfRequest.getStartTime(),
                       pdfRequest.getSupervizorId()
                );

                if (reportExists) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Bu sensör rapor zaten oluşturulmuştur");
                }
                User manager = userService.findById(pdfRequest.getManagerId());
                User supervizor = userService.findById(pdfRequest.getSupervizorId());
                Sensor sensor = sensorRepository.findById(pdfRequest.getOriginalSensorId()).orElseThrow();
                pdfReports.setSensor(sensor);
                pdfReports.setSensorName(pdfRequest.getSensorName());
                pdfReports.setTechnicianNote(pdfRequest.getNote());
                pdfReports.setLatitude(pdfRequest.getLatitude());
                pdfReports.setLongitude(pdfRequest.getLongitude());
                pdfReports.setCompletedTime(pdfRequest.getCompletedTime());
                pdfReports.setStartTime(pdfRequest.getStartTime());
                pdfReports.setManager(manager);
                pdfReports.setSupervisor(supervizor);
                PdfReports createdPdfReport = pdfRepository.save(pdfReports);


                Notification notificationn = new Notification();

                notificationn.setType(NotificationType.PDF_REPORT);
                notificationn.setReceiverId(pdfRequest.getManagerId());
                notificationn.setSenderId(pdfRequest.getSupervizorId());

                sendNotificationToManager(notificationn,createdPdfReport.getId());



                return ResponseEntity.ok(createdPdfReport);
            }
            catch (Exception e){
                System.out.println(e.getMessage());

                return  ResponseEntity.status(HttpStatus.CONFLICT).body("Report Couldn be created" + e.getMessage());
            }
    }

        //Sending notification to manager after creating a pdf report
        public void sendNotificationToManager(Notification notification,Long pdfReportId ){

            notification.setPdfReportId(pdfReportId);
            notification.setType(NotificationType.PDF_REPORT);

            PdfReports pdfReports = pdfRepository.findById(notification.getPdfReportId()).orElseThrow();


            Notification savedNotification = notificationRepository.save(notification);

            ManagerNotificationDTO managerNotificationDTO = new ManagerNotificationDTO();

            managerNotificationDTO.setId(savedNotification.getId());
            managerNotificationDTO.setIsread(savedNotification.getIsRead());
            managerNotificationDTO.setSenderName(pdfReports.getSupervisor().getFirstName());
            managerNotificationDTO.setSensorName(pdfReports.getSensorName());


            messagingTemplate.convertAndSend(
                    "/topic/notifications/" + savedNotification .getReceiverId(),
                    managerNotificationDTO
            );

        }


}
