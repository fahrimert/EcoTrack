    package com.example.EcoTrack.sensors.service;

    import com.example.EcoTrack.notification.dto.SinglePdfReportDTO;
    import com.example.EcoTrack.sensors.dto.AllSensorForManagerDTO;
    import com.example.EcoTrack.sensors.dto.CreateSensorLocationRequestDTO;
    import com.example.EcoTrack.sensors.dto.SensorDetailForManagerDTO;
    import com.example.EcoTrack.sensors.model.*;
    import com.example.EcoTrack.sensors.repository.SensorImageIconRepository;
    import com.example.EcoTrack.sensors.repository.SensorRepository;
    import com.example.EcoTrack.sensors.repository.SensorSessionImagesRepository;
    import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
    import com.example.EcoTrack.shared.dto.*;
    import com.example.EcoTrack.user.dto.AllSensorSessionDTOForManager;
    import com.example.EcoTrack.user.model.User;
    import com.example.EcoTrack.user.repository.UserRepository;
    import com.example.EcoTrack.user.service.UserService;
    import com.example.EcoTrack.util.ImageUtil;
    import jakarta.persistence.EntityNotFoundException;
    import org.locationtech.jts.geom.Coordinate;
    import org.locationtech.jts.geom.GeometryFactory;
    import org.locationtech.jts.geom.Point;
    import org.locationtech.jts.geom.PrecisionModel;
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
        public ResponseEntity<String> updateNonTaskSensorFinalState(String note, SensorStatus statusID, Long id, List<MultipartFile> files){
            try {
                Sensor sensor = sensorRepository.findById(id).orElseThrow(() -> new RuntimeException("Sensor Not Found"));
                SensorFix sensorFix = sensorSessionRepository.findByUserAndCompletedTimeIsNull(sensor.getCurrentSensorSession().getUser()).get();
                if (sensorFix.getSensor().getCurrentSensorSession().getUser() == null) {
                    return ResponseEntity.ok("Sensor has no active session."); // veya null da dönebilirsin
                }
                sensorFix.setNote(note);
                sensorSessionImageService.uploadImage(files,sensorFix.getId());

                Date now = new Date();


                sensorFix.setCompletedTime(now);
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
        //Get the past non task sensor detail function based on given sensor ıd for worker
        public ResponseEntity<ApiResponse>  getWorkersPastNonTaskSensorDetail(Long id) {

            if (id == null ){
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false,"Sensor doesnt exists.",null,null,404));
            }
            Optional<SensorFix> sensor = sensorSessionRepository.findById(id);

            if (sensor == null ){
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false,"Sensor doesnt exists.",null,null,404));
            }

            if (sensor.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Sensor doesnt exists.", null, null, 404));
            }
            if (id < 0){
                return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(false,"Invalid Sensor id.",null,null,404));

            }


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
        //this function for user session  solving page (not the task solving page)  get the sensor with given id
            public ResponseEntity<ApiResponse> getInduvualSensor(Long id) {
                Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
                String username = securityContextHolder.getName();

                Optional<Sensor> sensor = sensorRepository.findById(id);

                Sensor sensorEntity = sensor.orElse(null);

                if (sensorEntity == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse(false, "Sensor not found", null, null, 404));
                }
                User user = userService.findByUsername(username);
                if (user == null){
                    return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(false,"User Not Found",null,null,500));

                }

                if (user.getSensorSessions() == null){
                    return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(false,"User dont have sensor sessions",null,null,500));

                }


                if (user.getSensorSessions().stream().map(a -> a.getSensor().getId()).collect(Collectors.toList()).contains(id) == false) {
                    return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(false,"You are not Authorized to enter here .",null,null,500));

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
           public ResponseEntity<String> goToThesensorSessionNotTheTask( Long id ){
               try {
                   Sensor sensor = sensorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sensor Not Found"));
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
               catch (EntityNotFoundException e ){
                   return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sensor Not Found");
               }
               catch (Exception e){
                   return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");

               }



           };
        //Worker past-sensors page get past  sensors of a logged ın worker
        public List<SensorFix> getPastSensorsOfWorker() {
            try{
                Authentication securityContextHolder = SecurityContextHolder.getContext().getAuthentication();
                String username = securityContextHolder.getName();

                User user = userService.findByUsername(username);
                List<SensorFix> pastSensors =   sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(user).stream().collect(Collectors.toList());;

                return pastSensors;
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                return null;
            }
        }


        //end of worker sensor functions
        //get all the sensors endpoint


        public List<SensorDTO> getAllSensor() {


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
