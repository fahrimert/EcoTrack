    package com.example.EcoTrack.service;

    import com.example.EcoTrack.dto.*;
    import com.example.EcoTrack.model.*;
    import com.example.EcoTrack.repository.*;
    import com.example.EcoTrack.util.ImageUtil;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.messaging.simp.SimpMessagingTemplate;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.util.*;
    import java.util.stream.Collectors;



    import static org.springframework.http.HttpStatus.NOT_FOUND;

    @Service
    public class SensorService {
            private final SensorRepository sensorRepository;
            private  final SensorImageIconRepository sensorImageIconRepository;
            private final SensorSessionRepository sensorSessionRepository;
            private final UserRepository userRepository;
            private  final  SensorSessionImageService sensorSessionImageService;
            private final UserService userService;
            private final SimpMessagingTemplate messagingTemplate;
            private  final SensorSessionImagesRepository sensorSessionImagesRepository;
        public SensorService(SensorRepository sensorRepository, SensorImageIconRepository sensorImageIconRepository, SensorSessionRepository sensorSessionRepository, UserRepository userRepository, SensorSessionImageService sensorSessionImageService, UserService userService, SimpMessagingTemplate messagingTemplate, SensorSessionImagesRepository sensorSessionImagesRepository) {
            this.sensorRepository = sensorRepository;
            this.sensorImageIconRepository = sensorImageIconRepository;
            this.sensorSessionRepository = sensorSessionRepository;
            this.userRepository = userRepository;
            this.sensorSessionImageService = sensorSessionImageService;
            this.userService = userService;
            this.messagingTemplate = messagingTemplate;
            this.sensorSessionImagesRepository = sensorSessionImagesRepository;
        }

        public List<SensorDTO> getAllSensor() {

            List<Sensor> sensor = sensorRepository.findAll();

            List<SensorDTO> sensorlistDTO  = sensorRepository.findAll().stream().map(a ->
            {
                SensorFix currentSession = a.getCurrentSensorSession();
                SensorStatus status = a.getStatus();
                SensorLocation location = a.getSensorLocation();

                return new SensorDTO(
                        a.getId(),
                        a.getSensorName(),
                        status != null ? status.getDisplayName() : null,
                        status != null ? status.getColorCode() : null,
                        location != null && location.getLocation() != null ? location.getLocation().getX() : 0.0,
                        location != null && location.getLocation() != null ? location.getLocation().getY() : 0.0,

                        new SensorFixDTO(
                                currentSession != null ? currentSession.getId() : null,
                                a.getSensorName(),
                                status != null ? status.getDisplayName() : null,
                                status != null ? status.getColorCode() : null,
                                currentSession != null ? currentSession.getNote() : null,
                                currentSession != null ? currentSession.getStartTime() : null,
                                currentSession != null ? currentSession.getCompletedTime() : null,
                                location != null && location.getLocation() != null ? location.getLocation().getX() : 0.0,
                                location != null && location.getLocation() != null ? location.getLocation().getY() : 0.0
                        )
                );
            }).collect(Collectors.toList());
                return  sensorlistDTO;
       }

        public List<SensorDTO> getInRepaırSensors() {

            List<Sensor> sensor = sensorRepository.findAll();
            List<SensorDTO> sensorlistDTO  = sensorRepository.findAll().stream().map(a -> new SensorDTO(a.getId(),a.getSensorName(),a.getStatus().getDisplayName(),a.getStatus().getColorCode() ,a.getSensorLocation().getLocation().getX(),a.getSensorLocation().getLocation().getY(),

                    new SensorFixDTO(
                            a.getCurrentSensorSession() != null ? a.getCurrentSensorSession().getId( )  : null,
                            a != null ? a.getSensorName() : null,
                            a != null ? a.getStatus().getDisplayName() : null,         // Eğer varsa
                            a != null ? a.getStatus().getColorCode() : null,         // Eğer varsa
                            a.getCurrentSensorSession() != null ?  a.getCurrentSensorSession().getNote() : null,
                            a.getCurrentSensorSession() != null ? a.getCurrentSensorSession().getStartTime() : null,
                            a.getCurrentSensorSession() != null ? a.getCurrentSensorSession().getCompletedTime() : null,
                            a.getSensorLocation() != null ? a.getSensorLocation().getLocation().getX() : 0.0,
                            a.getSensorLocation() != null ? a.getSensorLocation().getLocation().getY() : 0.0
                    )
                    )).collect(Collectors.toList());
            return  sensorlistDTO;
        }

       public ResponseEntity<String> updateInRepairTheSensor( Long id ){
            try {
                Sensor sensor = sensorRepository.findById(id).orElseThrow(() -> new RuntimeException("Sensor Not Found"));
                Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
                String username = securityContextHolder.getName();

                User user = userService.findByUsername(username);
                Optional<SensorFix> existingSession = sensorSessionRepository.findByUserAndCompletedTimeIsNull(user);
                if (existingSession.isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("You already have an active repair session.");
                }


                if (sensor.getCurrentSensorSession() != null && sensor.getStatus() == SensorStatus.IN_REPAIR) {
                    return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sensor is already in another worker hand");
                }

                sensor.setStatus(SensorStatus.IN_REPAIR);
                SensorFix sensorSession = new SensorFix();
                sensorSession.setSensor(sensor);
                Date now = new Date();



                sensorSession.setUser(user);

                sensorSession.setStartTime(now);
                sensor.setCurrentSensorSession(sensorSession);
                sensorSessionRepository.save(sensorSession);
                sensorRepository.save(sensor);
                List<SensorDTO> sensors = getAllSensor();
                messagingTemplate.convertAndSend("/topic/sensors", sensors);

                return  ResponseEntity.status(HttpStatus.ACCEPTED).body("Now you are repairing" + sensor.getSensorName());

            }
           catch (Exception e){
               return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");

           }



       };

        public List<ImageResponseDTO> getImagesBySessionId(Long sessionId) {
            List<SensorSessionImages> images = sensorSessionImagesRepository.findBySensorSessionsId(sessionId);

            return images.stream()
                    .map(img -> {
                        String base64 = Base64.getEncoder().encodeToString(img.getImage());
                        return new ImageResponseDTO(img.getName(), img.getType(), base64);
                    })
                    .collect(Collectors.toList());
        }

            public ResponseEntity<String> updateFinalState(String note, SensorStatus statusID, Long id, List<MultipartFile> files){
                //burada o sensoru bulacak onun durumunu sensorsessiondan gelen veriyle düzeltece

                //ilk başta sensor sessionu düzeltsin notu falan kaydetsin hangi sensor session peki bizim sayfada aldığımız sensor session
                //ilk olarak sensorsessionun note ve completed timesini kaydetsin databaseye o sensore ait geçmişi çekerken ihtiyacımız olacak
                //nası çekicez sensorsessionu s
               try {
                   Sensor sensor = sensorRepository.findById(id).orElseThrow(() -> new RuntimeException("Sensor Not Found"));
                 SensorFix sensorFix = sensorSessionRepository.findByUserAndCompletedTimeIsNull(sensor.getCurrentSensorSession().getUser()).orElseThrow();

                   sensorFix.setNote(note);
                   sensorSessionImageService.uploadImage(files,sensorFix.getId());

                   Date now = new Date();
                   sensorFix.setCompletedTime(now);
                    //burada tamam o sensore dair statusu değiştir ama sensor sessionsda da bunu değiştirdiğin zmaan oraya özgün bir status olmalı aslında yoksa sensor datası aynı
                   System.out.println(statusID);
                   sensor.setStatus(statusID);

                   sensorFix.setFinalStatus(statusID);
                   sensor.setCurrentSensorSession(null);

                   Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
                   String username = securityContextHolder.getName();
                   User user = userService.findByUsername(username);
                   user.setSensorSessions(null);

                   sensorSessionRepository.save(sensorFix);
                   sensorRepository.save(sensor);
                   userRepository.save(user);
                   List<SensorDTO> sensors = getAllSensor();
                   messagingTemplate.convertAndSend("/topic/sensors", sensors);

                   return  ResponseEntity.status(HttpStatus.ACCEPTED).body("Sensor Updated" + sensor.getSensorName());

               }catch (Exception e){
                   System.out.println(e.getMessage());
                   return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

               }


            };


        public ResponseEntity<ApiResponse>  getSensorDetail(Long id) {
            //eğer şuanki userin sensorssessionunda değilse buraya erişememesi lazım


            Optional<SensorFix> sensor = sensorSessionRepository.findById(id);

            List<SensorSessionImages> images = sensorSessionImagesRepository.findBySensorSessionsId(sensor.get().getId());
            List<ImageResponseDTO> imageResponseDTOS = images.stream()
                    .map(img -> {
                        String base64 = Base64.getEncoder().encodeToString(ImageUtil.decompressImage(img.getImage()));
                        return new ImageResponseDTO(img.getName(), img.getType(), base64);
                    })
                    .collect(Collectors.toList());

            SensorIconImage sensorIconImage = sensorImageIconRepository.findBySensorId(sensor.orElse(null).getSensor().getId());
                        String base64 = Base64.getEncoder().encodeToString(ImageUtil.decompressImage(sensorIconImage.getImage()));
            SensorStatus finalStatus = sensor.orElse(null) != null ? sensor.orElse(null).getFinalStatus() : null;

            ImageResponseDTO ıconImageResponse = new ImageResponseDTO(sensorIconImage.getName(), sensorIconImage.getType(), base64);
            SensorSessionDTO sensorSessionDTO = new SensorSessionDTO(  sensor.orElse(null).getSensor().getId(),
                    sensor.orElse(null).getSensor().getSensorName(),
                    finalStatus == null ? null : finalStatus.getDisplayName(),
                    sensor.orElse(null).getSensor().getStatus().getColorCode(),
                    imageResponseDTOS,
                    ıconImageResponse,
                    sensor.orElse(null).getNote() ,
                    sensor.orElse(null).getStartTime(),
                    finalStatus,
                    sensor.orElse(null).getCompletedTime(),
                    sensor.orElse(null).getSensor().getSensorLocation().getLocation().getX(),
                    sensor.orElse(null).getSensor().getSensorLocation().getLocation().getY()
            );

            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse(true,"Successfully got sensor",

                    sensorSessionDTO   ,null,200));

        }


        public ResponseEntity<ApiResponse> getInduvualSensor(Long id) {
            //eğer şuanki userin sensorssessionunda değilse buraya erişememesi lazım
            Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
            String username = securityContextHolder.getName();

            User user = userService.findByUsername(username);

            Optional<Sensor> sensor = sensorRepository.findById(id);
            if (user.getSensorSessions().stream().map(a -> a.getSensor().getId()).collect(Collectors.toList()).contains(id) == false) {
                return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(false,"You are not Authorized to enter here .",null,null,500));

            }
            Sensor sensorEntity = sensor.orElse(null);

            if (sensorEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Sensor not found", null, null, 404));
            }

            SensorFix currentSession = sensorEntity.getCurrentSensorSession();
            SensorStatus status = sensorEntity.getStatus();
            SensorLocation location = sensorEntity.getSensorLocation();
            SensorDTO sensorDTO = new SensorDTO(
                    sensorEntity.getId(),
                    sensorEntity.getSensorName(),
                    status != null ? status.getDisplayName() : null,
                    status != null ? status.getColorCode() : null,
                    location != null && location.getLocation() != null ? location.getLocation().getX() : 0.0,
                    location != null && location.getLocation() != null ? location.getLocation().getY() : 0.0,

                    new SensorFixDTO(
                            currentSession != null ? currentSession.getId() : null,
                            sensorEntity.getSensorName(),
                            status != null ? status.getDisplayName() : null,
                            status != null ? status.getColorCode() : null,
                            currentSession != null ? currentSession.getNote() : null,
                            currentSession != null ? currentSession.getStartTime() : null,
                            currentSession != null ? currentSession.getCompletedTime() : null,
                            location != null && location.getLocation() != null ? location.getLocation().getX() : 0.0,
                            location != null && location.getLocation() != null ? location.getLocation().getY() : 0.0
                    )
            );
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse(true,"Successfully got sensor",
                    sensorDTO,null,200));


        }
        public List<SensorWithUserDTO> getPastSensorsOfWorkers() {
            try {
                Role workerRole = Role.WORKER;
                List<User> workers = userRepository.findAllByRole(workerRole);
                //burada ayrıca userin bilgisini de almam lazım
                List<SensorWithUserDTO> responseList = new ArrayList<>();
                //hayatımda böyle bişe görmedim
                for (User worker : workers) {
                    List<SensorFix> workerSensors = sensorSessionRepository
                            .findAllByUserAndCompletedTimeIsNotNull(worker);
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(worker.getId());
                    userDTO.setRole(worker.getRole());
                    userDTO.setSurName(worker.getSurName());
                    userDTO.setFirstName(worker.getFirstName());

                    //alttaki satırı ve yeni dto ekledik ikisi de gelsin diye user da çok önemli frontend için çünkü
                    for (SensorFix sensor : workerSensors) {
                        responseList.add(new SensorWithUserDTO(
                                sensor.getSensor().getId(),
                                sensor.getId(),
                                sensor.getSensor().getSensorName(),
                                sensor.getNote(),
                                sensor.getFinalStatus(),
                                sensor.getStartTime(),
                                sensor.getCompletedTime(),
                                worker.getId(),
                                worker.getFirstName(),
                                worker.getSurName(),
                                worker.getRole()
                        ));
                    }
                }

                return responseList;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        public List<SensorFix> getPastSensorsOfUser() {
            try{
                Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
                String username = securityContextHolder.getName();

                User user = userService.findByUsername(username);
            //sensor sessionlaraki sensorleri alıp onları döndürmem lazım
              List<SensorFix> pastSensors =   sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user).stream().collect(Collectors.toList());;

           return pastSensors;
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                return null;
            }
        }
        public  SensorIconImage uploadIconImage(MultipartFile file,Long sensorId) throws IOException {
            Sensor sensor = sensorRepository.findById(sensorId).orElseThrow();

                SensorIconImage sensorIconImage = new SensorIconImage();
                sensorIconImage.setName(file.getOriginalFilename());
                sensorIconImage.setType(file.getContentType());
                sensorIconImage.setSensor(sensor);
                sensorIconImage.setImage(ImageUtil.compressImage(file.getBytes()));
                sensor.setSensorIconImage(sensorIconImage);
                sensorImageIconRepository.save(sensorIconImage);
                sensorRepository.save(sensor);
                return  sensorIconImage;
            }
    }
