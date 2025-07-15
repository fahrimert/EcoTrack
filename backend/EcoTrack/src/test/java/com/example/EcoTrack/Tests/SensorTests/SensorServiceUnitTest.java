package com.example.EcoTrack.Tests.SensorTests;
import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.sensors.dto.CreateSensorLocationRequestDTO;
import com.example.EcoTrack.sensors.dto.SensorDetailForManagerDTO;
import com.example.EcoTrack.sensors.model.*;
import com.example.EcoTrack.sensors.repository.SensorImageIconRepository;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionImagesRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.sensors.service.SensorSessionImageService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.shared.dto.ImageResponseDTO;
import com.example.EcoTrack.user.dto.*;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.repository.LocationRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.service.UserService;
import com.example.EcoTrack.util.ImageUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SensorServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private UserLocationService userLocationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SensorService sensorService;

    @Mock
    GeometryFactory geometryFactory;
    @InjectMocks
    private UserDTO userDTO;

    @InjectMocks
    private SensorFix sensorFix;

    @InjectMocks
    NotificationService notificationService;

    @InjectMocks
    private AuthService authService;

    @Mock
    private SensorSessionImageService sensorSessionImageService;

    @Mock
    private LocationRepository userLocationRepository;

    @Mock
    private SensorSessionRepository sensorSessionRepository;

    @Mock
    private SensorImageIconRepository sensorImageIconRepository;

    @Mock
    private SensorSessionImagesRepository sensorSessionImagesRepository;

    @Mock
    private SensorRepository sensorRepository;
    @Mock
    private JwtService jwtService;

    @Mock
    private  SimpMessagingTemplate messagingTemplate;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @Mock
    private Authentication authentication;

    @Test
    @WithMockUser(username = "testUser")
    void updateNonTaskSensor_whenSensorSolvingComponent() throws Exception {
        String mockNote = "Mock Note" ;
        SensorStatus mockSensorStatus = SensorStatus.ACTIVE;
        Long mockSensorId = 15L;
        String mockSensorName = "TestSensor";
        List<MultipartFile> files = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        Date nextWeek = calendar.getTime();


        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(100L);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setCompletedTime(nextWeek);
        mockSensorFix.setStartTime(currentDate);
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);
        mockSensorFix.setNote(mockNote);


        Sensor mockSensor = new Sensor();
        mockSensor.setId(mockSensorId);
        mockSensor.setSensorName(mockSensorName);
        mockSensor.setCurrentSensorSession(mockSensorFix);
        when(sensorRepository.findById(mockSensorId)).thenReturn(Optional.of(mockSensor));
        when(sensorSessionRepository.findByUserAndCompletedTimeIsNull(mockSensor.getCurrentSensorSession().getUser())).thenReturn(Optional.of(mockSensorFix));

        mockSensorFix.setSensor(mockSensor);


        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        String mockUsernamee = SecurityContextHolder.getContext().getAuthentication().getName();


        User user = new User();
        user.setFirstName(mockUsernamee);
        user.setId(21L);
        user.setSensorSessions(null);

        when(userService.findByUsername(mockUsernamee)).thenReturn(user);
        when(sensorRepository.save(mockSensor)).thenReturn(mockSensor);

       ResponseEntity<String> result = sensorService.updateNonTaskSensorFinalState(      mockNote,
               mockSensorStatus,
               mockSensorId,
               files);
        System.out.println(result.getBody());
        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
        assertEquals("Sensor Updated" + mockSensor.getSensorName(), result.getBody());


    }

    @Test
    void shouldgoToTheSensorSessionNotTheTask() throws Exception{
        Long mockSensorId = 15L;

        Sensor mockSensor = new Sensor();
        mockSensor.setId(mockSensorId);
        mockSensor.setSensorName("Mock Sensor");
        mockSensor.setStatus(SensorStatus.ACTIVE);


        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        String mockUsernamee = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = new User();
        user.setFirstName(mockUsernamee);
        user.setId(21L);
        user.setSensorSessions(null);

        when(userService.findByUsername(mockUsernamee)).thenReturn(user);
        SensorFix sensorSession = new SensorFix();

        sensorSession.setSensor(mockSensor);

        mockSensor.setCurrentSensorSession(sensorSession);
        when(sensorRepository.findById(mockSensorId)).thenReturn(Optional.of(mockSensor));
        when(sensorSessionRepository.findByUserAndCompletedTimeIsNull(user)).thenReturn(Optional.empty());

        ArgumentCaptor<SensorFix> sensorFixCaptor = ArgumentCaptor.forClass(SensorFix.class);
        when(sensorSessionRepository.save(sensorFixCaptor.capture())).thenAnswer(invocation -> {
            SensorFix saved = invocation.getArgument(0);
            saved.setId(100L);
            return saved;
        });

        when(sensorRepository.save(mockSensor)).thenReturn(mockSensor);

        ResponseEntity<String> result = sensorService.goToThesensorSessionNotTheTask(mockSensorId );

        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
        assertEquals("Now you are repairing" + mockSensor.getSensorName(), result.getBody());

//        verify(sensorRepository.findById(mockSensorId));
        verify(sensorSessionRepository).findByUserAndCompletedTimeIsNull(user);
        verify(sensorSessionRepository).save(any(SensorFix.class));
        verify(sensorRepository).save(mockSensor);

    }

    @Test
    void goToSensorSessionNotTheTask_whenSensorNotFound_shouldReturnError() throws  Exception{
        when(sensorRepository.findById(any())).thenReturn(Optional.empty());

        ResponseEntity<String> response = sensorService.goToThesensorSessionNotTheTask(1021L);

        assertEquals("Sensor Not Found", response.getBody());

    }

    @Test
    void goToThesensorSessionNotTheTask_whenSensorInRepairByOtherWorker_shouldReturnConflict() {
        Long sensorId = 1L;
        String  mockUsername = "testUser";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(mockUsername);



        User mockUser = new User();
        mockUser.setFirstName(mockUsername);
        mockUser.setId(2L);
        mockUser.setFirstName("testUser");


        Sensor mockSensor = new Sensor();
        mockSensor.setId(sensorId);
        mockSensor.setSensorName("mockSensor");

        SensorFix mockSensorSession = new SensorFix();
        mockSensorSession.setId(101L);
        mockSensorSession.setSensor(mockSensor);

        mockUser.setSensorSessions(List.of(mockSensorSession));

        when(sensorRepository.findById(sensorId)).thenReturn(Optional.of(mockSensor));

        when(userService.findByUsername("testUser")).thenReturn(mockUser);

        when(sensorSessionRepository.findByUserAndCompletedTimeIsNull(mockUser)).thenReturn(Optional.of(mockSensorSession));

        ResponseEntity<String>  mockApiResponse = sensorService.goToThesensorSessionNotTheTask(sensorId);

        assertEquals(ResponseEntity.status(HttpStatus.CONFLICT).body("You already have an active repair session."), mockApiResponse);
    }

    @Test
    void getJustDetailOfSensorForManagerManageSensorUsage(){
        Long mockSensorId = 10L;
        Sensor mockSensor = new Sensor();
        mockSensor.setId(501L);
        mockSensor.setSensorName("Temperature-Sensor-01");
        byte[] mockImageData = "mock-image-data".getBytes();

        when(sensorRepository.findById(mockSensorId)).thenReturn(Optional.of(mockSensor));


        SensorIconImage sensorIconImage = new SensorIconImage();
        sensorIconImage.setId(mockSensorId);
        sensorIconImage.setName("mockimage.png");
        sensorIconImage.setType("image/png");
        sensorIconImage.setImage(ImageUtil.compressImage(mockImageData));

        mockSensor.setSensorIconImage(sensorIconImage);
        String base64 = Base64.getEncoder().encodeToString(ImageUtil.decompressImage(sensorIconImage.getImage()));

        ImageResponseDTO imageResponseDTO = new ImageResponseDTO();
        imageResponseDTO.setBase64Image(base64);
        imageResponseDTO.setName(sensorIconImage.getName());
        imageResponseDTO.setType(sensorIconImage.getType());

        SensorDetailForManagerDTO sensorDetailForManagerDTO = new SensorDetailForManagerDTO();
        sensorDetailForManagerDTO.setSensorName(mockSensor.getSensorName());
        sensorDetailForManagerDTO.setImageResponseDTO(imageResponseDTO);

        ResponseEntity<ApiResponse> result = sensorService.getJustDetailOfSensorForManagerManageSensorUsage(mockSensorId);

        assertEquals("Successfully got sensor for manager sensor management page",result.getBody().getMessage());

        assertEquals(HttpStatus.ACCEPTED,result.getStatusCode());

    }

    @Test
    void shouldReturnSensorNotFoundError(){
        Long mockSensorId = 10L;

        when(sensorRepository.findById(mockSensorId)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> result = sensorService.getJustDetailOfSensorForManagerManageSensorUsage(mockSensorId);

        assertNotNull(result);
        assertFalse(result.getBody().isSuccess());

        assertEquals("Sensor not found",result.getBody().getMessage());

        assertEquals(NOT_FOUND,result.getStatusCode());

    }


    @Test
    void  shouldManagerCreateSensor() throws IOException {
        String mockSensorName = "mock Sensor Name";
        byte[] mockImageData = "mock-image-data".getBytes();

        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "sensor-icon.png",
                MediaType.IMAGE_PNG_VALUE,
                "test-data".getBytes()
        );
        when(sensorRepository.existsBySensorName(mockSensorName)).thenReturn(false);

        Sensor mockSensor = new Sensor();
        mockSensor.setSensorName(mockSensorName);
        Date now = new Date();
        mockSensor.setId(1L);
        mockSensor.setStatus(SensorStatus.ACTIVE);
        mockSensor.setLastUpdatedAt(now);
        mockSensor.setInstallationDate(now);

        when(sensorRepository.findById(mockSensor.getId())).thenReturn(Optional.of(mockSensor));

        sensorService.uploadIconImage(mockFile,mockSensor.getId());
        SensorIconImage sensorIconImage = new SensorIconImage();
        sensorIconImage.setId(mockSensor.getId());
        sensorIconImage.setName("mockimage.png");
        sensorIconImage.setType("image/png");
        sensorIconImage.setImage(ImageUtil.compressImage(mockImageData));

        mockSensor.setSensorIconImage(sensorIconImage);

        when(sensorRepository.save(any(Sensor.class))).thenAnswer(invocation -> {
            Sensor sensor = invocation.getArgument(0);
            sensor.setId(1L);
            return sensor;
        });


        ResponseEntity response = sensorService.managerCreateSensor(mockSensorName,mockFile);

        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        Sensor responseSensor = (Sensor) response.getBody();



        assertEquals(mockSensorName,responseSensor.getSensorName());
        assertEquals(SensorStatus.ACTIVE,responseSensor.getStatus());

    }


    @Test
    void  shouldmanagerUpdateSensorLocation() throws IOException {

        CreateSensorLocationRequestDTO createSensorLocationRequestDTO = new CreateSensorLocationRequestDTO();

        createSensorLocationRequestDTO.setLatitude(20.45);
        createSensorLocationRequestDTO.setLongitude(30.40);
        createSensorLocationRequestDTO.setId(10L);

        Sensor mockSensor = new Sensor();
        mockSensor.setId(10L);
        mockSensor.setSensorName("mock sensor name");
        mockSensor.setSensorLocation(null);

        when(sensorRepository.findById(createSensorLocationRequestDTO.getId())).thenReturn(Optional.of(mockSensor));

        when(sensorRepository.save(any(Sensor.class))).thenAnswer(invocation -> {
            Sensor sensor = invocation.getArgument(0);
            sensor.setId(1L);
            return sensor;
        });

        ResponseEntity response = sensorService.managerUpdateSensorLocation(createSensorLocationRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        CreateSensorLocationRequestDTO responseDTO = (CreateSensorLocationRequestDTO) response.getBody();

        assertNotNull(responseDTO);
        assertEquals(createSensorLocationRequestDTO.getLatitude(),responseDTO.getLatitude());
        assertEquals(createSensorLocationRequestDTO.getLongitude(),responseDTO.getLongitude());

    }


    @Test
    void  shouldmanagerUpdateInduvualSensor() throws IOException {

        Long mockSensorId = 10L;
        String mockSensorName = "mock sensor";
        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "sensor-icon.png",
                MediaType.IMAGE_PNG_VALUE,
                "test-data".getBytes()
        );
        Sensor mockSensor = new Sensor();
        mockSensor.setId(10L);
        mockSensor.setSensorName(mockSensorName);
        mockSensor.setStatus(SensorStatus.ACTIVE);

        SensorIconImage mockIconImage = new SensorIconImage();
        mockSensor.setSensorIconImage(mockIconImage);


        when(sensorRepository.findById(mockSensorId)).thenReturn(Optional.of(mockSensor));

        sensorService.updateIconImage(mockFile,mockSensor.getId());




        CreateSensorLocationRequestDTO createSensorLocationRequestDTO = new CreateSensorLocationRequestDTO();

        createSensorLocationRequestDTO.setLatitude(20.45);
        createSensorLocationRequestDTO.setLongitude(30.40);
        createSensorLocationRequestDTO.setId(10L);


        ResponseEntity response = sensorService.managerUpdateInduvualSensor(String.valueOf(mockSensorId),mockSensorName,mockFile);
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        Sensor responseDTO = (Sensor) response.getBody();

        assertNotNull(responseDTO);
        assertEquals(10L,(responseDTO.getId()));

        assertNotNull(responseDTO.getSensorIconImage());
        assertEquals(mockSensor.getSensorName(), responseDTO.getSensorName());

    }

    @Test
    void  shouldDeleteSensorById() throws IOException {

        Long mockSensorId = 10L;
        String mockSensorName = "mock sensor";
        MockMultipartFile mockFile = new MockMultipartFile(
                "files",
                "sensor-icon.png",
                MediaType.IMAGE_PNG_VALUE,
                "test-data".getBytes()
        );
        Sensor mockSensor = new Sensor();
        mockSensor.setId(10L);
        mockSensor.setSensorName(mockSensorName);
        mockSensor.setStatus(SensorStatus.ACTIVE);

        SensorIconImage mockIconImage = new SensorIconImage();
        mockSensor.setSensorIconImage(mockIconImage);


        when(sensorRepository.findById(mockSensorId)).thenReturn(Optional.of(mockSensor));

        sensorService.updateIconImage(mockFile,mockSensor.getId());




        CreateSensorLocationRequestDTO createSensorLocationRequestDTO = new CreateSensorLocationRequestDTO();

        createSensorLocationRequestDTO.setLatitude(20.45);
        createSensorLocationRequestDTO.setLongitude(30.40);
        createSensorLocationRequestDTO.setId(10L);


        ResponseEntity response = sensorService.managerUpdateInduvualSensor(String.valueOf(mockSensorId),mockSensorName,mockFile);
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        Sensor responseDTO = (Sensor) response.getBody();

        assertNotNull(responseDTO);
        assertEquals(10L,(responseDTO.getId()));

        assertNotNull(responseDTO.getSensorIconImage());
        assertEquals(mockSensor.getSensorName(), responseDTO.getSensorName());

    }

//    @Test
//    void getAllSensorForManagerUse() throws  Exception {
//        User mockTestUser = new User();
//        mockTestUser.setFirstName("mockFirstname");
//        mockTestUser.setSurName("mock Surname");
//
//        UserOnlineStatus userStatus = new UserOnlineStatus();
//        userStatus.setIsOnline(true);
//        mockTestUser.setUserOnlineStatus(userStatus);
//
//
//    }

}
