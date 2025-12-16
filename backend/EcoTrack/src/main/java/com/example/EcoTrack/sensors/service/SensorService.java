    package com.example.EcoTrack.sensors.service;

    import com.example.EcoTrack.notification.dto.SinglePdfReportDTO;
    import com.example.EcoTrack.sensors.dto.AllSensorForManagerDTO;
    import com.example.EcoTrack.sensors.dto.CreateSensorLocationRequestDTO;
    import com.example.EcoTrack.sensors.dto.SensorDetailForManagerDTO;
    import com.example.EcoTrack.sensors.dto.sensorSolvingDtos.SensorSolvingSensorDto;
    import com.example.EcoTrack.sensors.dto.sensorSolvingDtos.SensorSolvingSensorFixDto;
    import com.example.EcoTrack.sensors.dto.workerDashboardDtos.WorkerDashboardSensorDto;
    import com.example.EcoTrack.sensors.dto.workerDashboardDtos.WorkerDashboardSensorFixDto;
    import com.example.EcoTrack.sensors.model.*;
    import com.example.EcoTrack.sensors.repository.SensorImageIconRepository;
    import com.example.EcoTrack.sensors.repository.SensorRepository;
    import com.example.EcoTrack.sensors.repository.SensorSessionImagesRepository;
    import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
    import com.example.EcoTrack.shared.dto.*;
    import com.example.EcoTrack.user.dto.AllSensorSessionDTOForManager;
    import com.example.EcoTrack.user.dto.pastsensors.PastSensorDetailDto;
    import com.example.EcoTrack.user.dto.pastsensors.PastSensorsDto;
    import com.example.EcoTrack.user.dto.pastsensors.PastSensorsSensorFixDto;
    import com.example.EcoTrack.user.model.User;
    import com.example.EcoTrack.user.repository.UserRepository;
    import com.example.EcoTrack.user.service.UserService;
    import com.example.EcoTrack.util.ImageUtil;
    import jakarta.persistence.EntityNotFoundException;
    import jakarta.transaction.Transactional;
    import lombok.extern.slf4j.Slf4j;
    import org.locationtech.jts.geom.Coordinate;
    import org.locationtech.jts.geom.GeometryFactory;
    import org.locationtech.jts.geom.Point;
    import org.locationtech.jts.geom.PrecisionModel;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.messaging.simp.SimpMessagingTemplate;
    import org.springframework.security.access.AccessDeniedException;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.util.*;
    import java.util.stream.Collectors;



    import static org.springframework.http.HttpStatus.NOT_FOUND;

    @Service
    @Slf4j
    public class SensorService {
            private final SensorRepository sensorRepository;
            private  final SensorImageIconRepository sensorImageIconRepository;
            private final SensorSessionRepository sensorSessionRepository;
            private final UserRepository userRepository;
            private  final SensorSessionImageService sensorSessionImageService;
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

        //Start Of Manager Sensor Functions
        public List<AllSensorForManagerDTO> getAllSensorForManagerUse() {
            List<AllSensorForManagerDTO> sensorlistDTO  = sensorRepository.findAll().stream().map(a ->
            {
                SensorFix currentSession = a.getCurrentSensorSession();
                SensorStatus status = a.getStatus();
                SensorLocation location = a.getSensorLocation();

                AllSensorSessionDTOForManager sessionDTO = null;
                if (currentSession != null) {
                    User user = currentSession.getUser();
                    sessionDTO = new AllSensorSessionDTOForManager(
                            currentSession.getId(),
                            a.getSensorName(),
                            user != null ? user.getFirstName() : null,
                            user != null ? user.getSurName() : null,
                            user != null && user.getUserOnlineStatus() != null
                                    ? user.getUserOnlineStatus().getIsOnline()
                                    : null
                    );
                }
                String base64 = Base64.getEncoder().encodeToString(ImageUtil.decompressImage(a.getSensorIconImage().getImage()));

                ImageResponseDTO ıconImageResponse = new ImageResponseDTO(a.getSensorIconImage().getName(), a.getSensorIconImage().getType(), base64);
                return new AllSensorForManagerDTO(
                        a.getId(),
                        a.getSensorName(),
                        ıconImageResponse,
                        status != null ? status.getDisplayName() : null,
                        status != null ? status.getColorCode() : null,
                        location != null && location.getLocation() != null ? location.getLocation().getX() : 0.0,
                        location != null && location.getLocation() != null ? location.getLocation().getY() : 0.0,
                        a.getInstallationDate(),
                        a.getLastUpdatedAt(),
                        sessionDTO
                );
            }).collect(Collectors.toList());
            return  sensorlistDTO;
        }
        public ResponseEntity managerCreateSensor (String sensorName, MultipartFile files) {

            try {
                if (sensorName == null){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Sensör İsmi Boş Olamaz");
                }
                if (sensorRepository.existsBySensorName(sensorName)){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Aynı Sensör İsmine Ait Sensör Bulunmakta");

                }

                if (files == null){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Fotoğraf Ekleme boş olamaz ");

                }
                Sensor sensor = new Sensor();
                sensor.setSensorName(sensorName);
                Date now = new Date();
                sensor.setStatus(SensorStatus.ACTIVE);
                sensor.setLastUpdatedAt(now);
                sensor.setInstallationDate(now);
                Sensor createdSensor = sensorRepository.save(sensor);


                uploadIconImage(files,createdSensor.getId());

                return  ResponseEntity.status(HttpStatus.ACCEPTED).body(sensor);

            }catch (Exception e){
                System.out.println(e.getMessage());
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

            }

        }

        public ResponseEntity managerUpdateInduvualSensor (String sensorId, String sensorName, MultipartFile files) {

            try {
                if (sensorName == null){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Sensör İsmi Boş Olamaz");
                }

                if (files == null){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Fotoğraf Ekleme boş olamaz ");

                }
                Sensor sensor = sensorRepository.findById(Long.parseLong(sensorId)).orElseThrow();
                sensor.setSensorName(sensorName);
                Date now = new Date();
                sensor.setStatus(SensorStatus.ACTIVE);
                sensor.setLastUpdatedAt(now);
                sensor.setInstallationDate(now);


                updateIconImage(files,Long.parseLong(sensorId));

                return  ResponseEntity.status(HttpStatus.ACCEPTED).body(sensor);

            }catch (Exception e){
                System.out.println(e.getMessage());
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

            }

        }


        //manager sensor management page update sensor location component in add sensor section
        public ResponseEntity managerUpdateSensorLocation (CreateSensorLocationRequestDTO sensorLocationDTO )  {

            try {

                Sensor sensor = sensorRepository.findById(sensorLocationDTO.getId()).orElse(null);

                GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
                double latitude = sensorLocationDTO.getLatitude();
                double longitude = sensorLocationDTO.getLongitude();

                Point location = geometryFactory.createPoint(new Coordinate(latitude, longitude));

                SensorLocation sensorLocation = new SensorLocation();

                CreateSensorLocationRequestDTO createSensorLocationDTO = new CreateSensorLocationRequestDTO();



                Date now = new Date();

                sensorLocation.setCreatedAt(now);
                sensorLocation.setLocation(location);
                sensorLocation.setSensor(sensor);
                sensor.setSensorLocation(sensorLocation);

                sensorRepository.save(sensor);

                createSensorLocationDTO.setLatitude(latitude);
                createSensorLocationDTO.setLongitude(longitude);
                return  ResponseEntity.status(HttpStatus.ACCEPTED).body(createSensorLocationDTO);

            }catch (Exception e){
                System.out.println(e.getMessage());
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

            }

        }



        //this function for worker section non-task sensor solving page
        @Transactional
        public void updateNonTaskSensorFinalState(String note, SensorStatus statusID, Long sensorId,  Long userId, List<MultipartFile> files){
                Sensor sensor = sensorRepository.findById(sensorId)
                        .orElseThrow(() -> new EntityNotFoundException("Sensör bulunamadı ID: " + sensorId));
                SensorFix activeSession = sensorSessionRepository.findActiveSession(userId, sensorId)
                        .orElseThrow(() -> new IllegalStateException("Bu sensör üzerinde aktif bir işleminiz bulunmamaktadır."));
                Date now = new Date();
                activeSession.setCompletedTime(now);
                sensor.setCurrentSensorSession(null);
                activeSession.setNote(note);
                activeSession.setFinalStatus(statusID);

                sensor.setStatus(statusID);

                try {
                    if (!files.isEmpty()) {
                        sensorSessionImageService.uploadImage(files, activeSession.getId());
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Resimler yüklenirken hata oluştu: " + e.getMessage());
                }

                sensorSessionRepository.save(activeSession);
                sensorRepository.save(sensor);

                broadcastDashboardUpdate();

        };
        private void broadcastDashboardUpdate() {
            List<WorkerDashboardSensorDto> sensors = getAllSensorsWorkerDashboard();
            messagingTemplate.convertAndSend("/topic/sensors", sensors);
        }
        //Get the past non task sensor detail function based on given sensor ıd for worker
        @Transactional
        public PastSensorDetailDto  getWorkersPastNonTaskSensorDetail(Long sensorId) {

            if (sensorId == null || sensorId <= 0) {
                throw new IllegalArgumentException("Geçersiz Session ID: " + sensorId);
            }
            SensorFix session = sensorSessionRepository.findByIdWithDetails(sensorId)
                    .orElseThrow(() -> new EntityNotFoundException("Kayıt bulunamadı"));
            return mapToDetailDTO(session);
        }

        private PastSensorDetailDto mapToDetailDTO(SensorFix session) {
            Sensor sensor = session.getSensor();

            ImageResponseDTO iconDTO = null;
            if (sensor.getSensorIconImage() != null) {
                String base64 = ImageUtil.decompressAndEncode(sensor.getSensorIconImage().getImage());
                iconDTO = new ImageResponseDTO(sensor.getSensorIconImage().getName(),
                        sensor.getSensorIconImage().getType(), base64);
            }

            List<ImageResponseDTO> evidenceImages = session.getSensorSessionImages().stream()
                    .map(img -> {
                        String base64 = ImageUtil.decompressAndEncode(img.getImage());
                        return new ImageResponseDTO(img.getName(), img.getType(), base64);
                    })
                    .collect(Collectors.toList());

            return PastSensorDetailDto.builder()
                    .sensorId(sensor.getId())
                    .sensorName(sensor.getSensorName())
                    .sensorStatus(sensor.getStatus().name())
                    .iconImage(iconDTO)
                    .sessionId(session.getId())
                    .note(session.getNote())
                    .finalStatus(session.getFinalStatus() != null ? session.getFinalStatus().name() : null)
                    .startTime(session.getStartTime())
                    .completedTime(session.getCompletedTime())
                    .latitude(sensor.getSensorLocation().getLocation().getY())
                    .longitude(sensor.getSensorLocation().getLocation().getX())
                    .evidenceImages(evidenceImages)
                    .build();
        }
        //this function for user session  solving page (not the task solving page)  get the sensor with given id
            public SensorSolvingSensorDto getInduvualSensorForSensorSolving(Long sensorId, Long userId ) {
                Sensor sensor = sensorRepository.findById(sensorId)
                        .orElseThrow(() -> new EntityNotFoundException("Sensör bulunamadı: " + sensorId));
                boolean isAuthorized = sensorSessionRepository.existsByUserIdAndSensorId(userId, sensorId);

                if (!isAuthorized) {
                    throw new AccessDeniedException("Bu sensörü görüntüleme yetkiniz yok.");
                }

                return  convertToSensorSolvingSensorDto(sensor);

            }

        private SensorSolvingSensorDto convertToSensorSolvingSensorDto(Sensor sensor) {
            SensorStatus status = sensor.getStatus();
            SensorLocation location = sensor.getSensorLocation();
            SensorFix session = sensor.getCurrentSensorSession();

            SensorSolvingSensorFixDto sessionDTO = null;
            if (session != null) {
                sessionDTO = SensorSolvingSensorFixDto.builder()
                        .id(session.getId())
                        .note(session.getNote())
                        .startTime(session.getStartTime())
                        .completedTime(session.getCompletedTime())
                        .userId(session.getUser() != null ? session.getUser().getId() : null)
                        .build();
            }

            double lat = 0.0, lng = 0.0;
            if (location != null && location.getLocation() != null) {
                lat = location.getLocation().getY();
                lng = location.getLocation().getX();
            }

            return SensorSolvingSensorDto.builder()
                    .id(sensor.getId())
                    .sensorName(sensor.getSensorName())
                    .status(status != null ? status.name() : "UNKNOWN")
                    .color_code(status != null ? status.getColorCode() : "#000")
                    .latitude(lat)
                    .longitude(lng)
                    .currentSensorSession(sessionDTO)
                    .build();
        }


        //management sensor management page for update sensor component initialdata purposes module same as the upper module without protection detail
        public ResponseEntity<ApiResponse> getJustDetailOfSensorForManagerManageSensorUsage(Long id) {
            //eğer şuanki userin sensorssessionunda değilse buraya erişememesi lazım

            Optional<Sensor> sensor = sensorRepository.findById(id);
            Sensor sensorEntity = sensor.orElse(null);
            if (sensorEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Sensor not found", null, null, 404));
            }

            String base64 = Base64.getEncoder().encodeToString(ImageUtil.decompressImage(sensorEntity.getSensorIconImage().getImage()));

            ImageResponseDTO ıconImageResponse = new ImageResponseDTO(sensorEntity.getSensorIconImage().getName(), sensorEntity.getSensorIconImage().getType(), base64);

            SensorDetailForManagerDTO sensorDTO = new SensorDetailForManagerDTO(
                    sensorEntity.getSensorName(),
                    ıconImageResponse
            );
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse(true,"Successfully got sensor for manager sensor management page",
                    sensorDTO,null,200));


        }


        //End  Of Manager Sensor Functions

       //worker sensor functions
       //Worker Dashboard Page Go To The sensor session not the task sensor
       @Transactional
        public String goToThesensorSessionNotTheTask(Long id) {
           Sensor sensor = sensorRepository.findById(id)
                   .orElseThrow(() -> new EntityNotFoundException("Sensor Not Found"));

           Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
           String username = securityContextHolder.getName();

           User user = userService.findByEmail(username);

           Optional<SensorFix> existingSession = sensorSessionRepository.findByUserAndCompletedTimeIsNull(user);
           if (existingSession.isPresent()) {
               throw new IllegalStateException("You already have an active repair session.");
           }

           if (sensor.getCurrentSensorSession() != null && sensor.getStatus() == SensorStatus.IN_REPAIR) {
               throw new IllegalStateException("Sensor is already in another worker hand");
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

           List<WorkerDashboardSensorDto> sensors = getAllSensorsWorkerDashboard();
           messagingTemplate.convertAndSend("/topic/sensors", sensors);

           return "Now you are repairing " + sensor.getSensorName();
       }


        //Worker past-sensors page get past  sensors of a logged ın worker
        public List<PastSensorsDto> getPastSensorsOfWorker() {
            try{
                Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
                String username = securityContextHolder.getName();

                User user = userService.findByEmail(username);

                if (user == null) {
                    throw new RuntimeException("Kullanıcı bulunamadı");
                }


                List<SensorFix> pastSensorSessions =   sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user).stream().collect(Collectors.toList());;


                Map<Sensor, List<SensorFix>> groupedBySensor = pastSensorSessions.stream()
                        .collect(Collectors.groupingBy(SensorFix::getSensor));


                return groupedBySensor.entrySet().stream()
                        .map(entry -> {
                            Sensor sensor = entry.getKey();
                            List<SensorFix> sessions = entry.getValue();

                            // Alt DTO Listesini Oluştur
                            List<PastSensorsSensorFixDto> sessionDTOs = sessions.stream()
                                    .map(s -> PastSensorsSensorFixDto.builder()
                                            .id(s.getId())
                                            .startTime(s.getStartTime())
                                            .completedTime(s.getCompletedTime())
                                            .note(s.getNote()) // Note -> note (küçük harf düzeltmesi sonrası)
                                            .build())
                                    .sorted(Comparator.comparing(PastSensorsSensorFixDto::getStartTime).reversed()) // Yeniden eskiye sırala
                                    .collect(Collectors.toList());

                            // Ana DTO'yu Oluştur
                            return PastSensorsDto.builder()
                                    .sensorId(sensor.getId())
                                    .sensorName(sensor.getSensorName())
                                    .status(sensor.getStatus().name())
                                    .installationDate(sensor.getInstallationDate())
                                    .sessions(sessionDTOs)
                                    .build();
                        })
                        .collect(Collectors.toList());

            } catch (Exception e) {
                log.error("Geçmiş sensörler çekilemedi: {}", e.getMessage());
                return Collections.emptyList(); // Null dönmek yerine boş liste dönmek daha güvenlidir
            }        }


        //end of worker sensor functions
        //get all the sensors endpoint

        private WorkerDashboardSensorDto getAllSensorsConvertToDTO(Sensor sensor) {
            SensorLocation loc = sensor.getSensorLocation();
            SensorFix session = sensor.getCurrentSensorSession();
            SensorStatus status = sensor.getStatus();

            WorkerDashboardSensorFixDto sessionDTO = null;
            if (session != null) {
                sessionDTO = WorkerDashboardSensorFixDto.builder()
                        .id(session.getId())
                        .startTime(session.getStartTime())
                        .userId(session.getUser().getId())
                        .build();
            }

            double lat = 0.0;
            double lng = 0.0;
            if (loc != null && loc.getLocation() != null) {
                lat = loc.getLocation().getX();
                lng = loc.getLocation().getY();
            }

            return WorkerDashboardSensorDto.builder()
                    .id(sensor.getId())
                    .sensorName(sensor.getSensorName())
                    .status(status != null ? status.getDisplayName() : "UNKNOWN")
                    .color_code(status != null ? status.getColorCode() : "#000000")
                    .latitude(lat)
                    .longitude(lng)
                    .currentSensorSession(sessionDTO)
                    .build();
        }
        @Transactional
        public List<WorkerDashboardSensorDto> getAllSensorsWorkerDashboard() {
            return sensorRepository.findAllWithDetailsForWorkerDashboardSensor().stream()
                    .map(this::getAllSensorsConvertToDTO)
                    .collect(Collectors.toList());
        }

        public List<ImageResponseDTO>  getImagesBySessionId(Long sessionId) {
            List<SensorSessionImages> images = sensorSessionImagesRepository.findBySensorSessionsId(sessionId);

            return images.stream()
                    .map(img -> {
                        String base64 = Base64.getEncoder().encodeToString(img.getImage());
                        return new ImageResponseDTO(img.getName(), img.getType(), base64);
                    })
                    .collect(Collectors.toList());
        }

        public ResponseEntity<ApiResponse> getInduvualSensorLocation(Long id) {
            //eğer şuanki userin sensorssessionunda değilse buraya erişememesi lazım
            Optional<Sensor> sensor = sensorRepository.findById(id);

            Sensor sensorEntity = sensor.orElse(null);

            if (sensorEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Sensor not found", null, null, 404));
            }
            SensorLocationDTO sensorLocationDTO = new SensorLocationDTO();

            SensorLocation location = sensorEntity.getSensorLocation();


            sensorLocationDTO.setId(location.getSensor().getId());
            sensorLocationDTO.setLatitude(location.getSensor().getSensorLocation().getLocation().getX());
            sensorLocationDTO.setLongitude(location.getSensor().getSensorLocation().getLocation().getY());

            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse(true,"Successfully got sensor",
                    sensorLocationDTO,null,200));


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

        public  ResponseEntity<?>  updateIconImage(MultipartFile file,Long sensorId) throws IOException {

            try {
                Sensor sensor = sensorRepository.findById(sensorId).orElseThrow();
                SensorIconImage existingImage = sensor.getSensorIconImage();

                sensor.getSensorIconImage().setName(file.getOriginalFilename());
                sensor.getSensorIconImage().setType(file.getContentType());
                sensor.getSensorIconImage().setImage(ImageUtil.compressImage(file.getBytes()));
                sensor.setSensorIconImage(existingImage);
                sensorRepository.save(sensor);
                return ResponseEntity.ok(existingImage) ;
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Null");
            }
        }

        public ResponseEntity deleteSensorById(Long sensorId) {
                try {
                    Optional<Sensor> sensor = sensorRepository.findById(sensorId);
                    if (sensor.isPresent()){

                        sensorRepository.deleteById(sensorId);
                        return ResponseEntity.status(HttpStatus.OK)
                                .body(ApiResponse.success(
                                        "Sensor Successfully deleted"
                                ));
                    }
                    else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(ApiResponse.error(
                                        "Error",
                                        List.of("No sensor Found"),
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
                            ));                }
            }


        //integrasyon testinin bi bu methodu kaldı sadece
        public ResponseEntity<ApiResponse>  getPdfReportInduvualSensor(String sensorId) {


            Sensor sensor = sensorRepository.findById(Long.parseLong(sensorId)).orElseThrow();
            List<SensorSessionImages> images = sensorSessionImagesRepository.findBySensorSessionsId(sensor.getId());
            List<ImageResponseDTO> imageResponseDTOS = images.stream()
                    .map(img -> {
                        String base64 = Base64.getEncoder().encodeToString(ImageUtil.decompressImage(img.getImage()));
                        return new ImageResponseDTO(img.getName(), img.getType(), base64);
                    })
                    .collect(Collectors.toList());

            SensorIconImage sensorIconImage = sensorImageIconRepository.findBySensorId(sensor.getId());
            String base64 = Base64.getEncoder().encodeToString(ImageUtil.decompressImage(sensorIconImage.getImage()));

            ImageResponseDTO ıconImageResponse = new ImageResponseDTO(sensorIconImage.getName(), sensorIconImage.getType(), base64);
            SinglePdfReportDTO singlePdfReportDTO = new SinglePdfReportDTO(  sensor.getId(),
                    sensor.getSensorName(),
                    sensor.getStatus().getDisplayName(),

                    sensor.getStatus().getColorCode(),
                    imageResponseDTOS,
                    ıconImageResponse
            );

            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse(true,"Successfully got sensor",

                    singlePdfReportDTO   ,null,200));

        }

    }
