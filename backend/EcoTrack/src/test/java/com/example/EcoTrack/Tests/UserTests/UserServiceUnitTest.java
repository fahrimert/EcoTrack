package com.example.EcoTrack.Tests.UserTests;

import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.notification.dto.NotificationDTO;
import com.example.EcoTrack.notification.model.Notification;
import com.example.EcoTrack.notification.repository.NotificationRepository;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.notification.type.NotificationType;
import com.example.EcoTrack.sensors.model.*;
import com.example.EcoTrack.sensors.repository.SensorImageIconRepository;
import com.example.EcoTrack.sensors.repository.SensorRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionImagesRepository;
import com.example.EcoTrack.sensors.repository.SensorSessionRepository;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.shared.dto.SensorFixDTO;
import com.example.EcoTrack.shared.dto.SensorSessionDTO;
import com.example.EcoTrack.user.dto.*;
import com.example.EcoTrack.user.model.Role;
import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.model.UserLocation;
import com.example.EcoTrack.user.repository.LocationRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.service.UserService;
import com.example.EcoTrack.util.ImageUtil;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
@Transactional
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private UserLocationService userLocationService;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private SensorService sensorService;

    @Mock
    GeometryFactory geometryFactory;
    @InjectMocks
    private UserDTO userDTO;

    @InjectMocks
    NotificationService notificationService;

    @InjectMocks
    private AuthService authService;

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
    private SecurityContextHolder securityContextHolder;

    @Mock
    private Authentication authentication;

    @Mock
    private  SecurityContext securityContext;


    @Test
    void getNotifications_shouldReturnNotificationByUserId() throws Exception {
        Long mockUserId = 10L;

        Notification mockNotification = new Notification();
        mockNotification.setId(101L);
        mockNotification.setIsRead(false);
        mockNotification.setType(NotificationType.TASK);
        mockNotification.setReceiverId(mockUserId);
        mockNotification.setSenderId(15L);
        mockNotification.setCreatedAt(LocalDateTime.now());
        mockNotification.setSuperVizorDeadline(LocalDateTime.now().plusDays(7));
        when(notificationRepository.findByReceiverId(mockUserId))
                .thenReturn(List.of(mockNotification));


        when(notificationRepository.findByReceiverId(mockUserId)).thenReturn(List.of(mockNotification));


        ResponseEntity<List<NotificationDTO>> response = userService.getNotificationById(mockUserId);
        List<NotificationDTO> dtos = response.getBody();

        assertEquals(mockNotification.getId(), dtos.get(0).getId());
        assertNotNull(dtos);
        assertEquals(1, dtos.size());
    }

    @Test
    void getProfilesOfAllWorkers_shouldReturnAllWorkersByIdGroup() throws Exception {
        Long mockUserId1 = 1L;
        Long mockUserId2 = 2L;

        User user1 = new User();
        user1.setId(mockUserId1);
        user1.setFirstName("TestUser");

        User user2 = new User();
        user2.setId(mockUserId2);
        user2.setFirstName("TestUser2");

        UserOnlineStatusDTO userOnlineStatusDTO = new UserOnlineStatusDTO();
        userOnlineStatusDTO.setId(mockUserId1);
        userOnlineStatusDTO.setFirstName("TestUser");
        userOnlineStatusDTO.setRole(Role.WORKER);


        UserOnlineStatusDTO userOnlineStatusDTO2 = new UserOnlineStatusDTO();
        userOnlineStatusDTO2.setId(mockUserId2);
        userOnlineStatusDTO2.setFirstName("TestUser2");
        userOnlineStatusDTO2.setRole(Role.WORKER);

        List<UserOnlineStatusDTO> userOnlineStatusDTOList = new ArrayList<>();

        userOnlineStatusDTOList.add(userOnlineStatusDTO);
        userOnlineStatusDTOList.add(userOnlineStatusDTO2);
        when(userRepository.findAllById(List.of(mockUserId1, mockUserId2))).thenReturn(List.of(user1, user2));

        List<UserOnlineStatusDTO> response = userService.getProfilesOfAllWorkers(List.of(mockUserId1, mockUserId2));

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("TestUser", response.get(0).getFirstName());
        assertEquals("TestUser2", response.get(1).getFirstName());
        verify(userRepository).findAllById(List.of(mockUserId1, mockUserId2));

    }

    @Test
    void saveLocation_ShouldSaveLocationAndReturnSuccess() throws Exception {
        String mockUsername = "TestUser";
        Double mockLat = 25.0;
        Double mockLang = 20.0;

        User mockUser = new User();
        mockUser.setFirstName(mockUsername);

        UserLocation userLocation = new UserLocation();
        userLocation.setUser(mockUser);

        when(userRepository.findByFirstName(mockUsername)).thenReturn(mockUser);
        when(userLocationRepository.save(any(UserLocation.class))).thenReturn(userLocation);

        String result = userLocationService.saveUserLocation(mockUsername, mockLat, mockLang);

        assertEquals("User location details saved successfully", result);

        verify(userLocationRepository).save(any(UserLocation.class));

    }

    @Test
    void shouldReturnUserDTO_whenGivenCorrectAccessToken() throws Exception {

        String mockToken = "mock.token.value";
        Claims cLaims = mock(Claims.class);
        when(cLaims.getSubject()).thenReturn("mockUser");

        User user = new User();
        user.setId(1L);
        user.setFirstName("MockatoMock");
        user.setSurName("Mockalangoza");
        user.setRole(Role.WORKER);

        Sensor sensor = new Sensor();
        sensor.setSensorName("Sensor 15");
        sensor.setStatus(SensorStatus.ACTIVE);


        SensorFix sensorFix = new SensorFix();
        sensorFix.setId(100L);
        sensorFix.setUser(user);
        sensorFix.setNote("MockatoMock Note");
        sensorFix.setCompletedTime(new Date());
        sensorFix.setStartTime(new Date());
        sensorFix.setFinalStatus(SensorStatus.SOLVED);
        sensorFix.setSensor(sensor);
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        double latitude = 25.0;
        double longitude = 25.0;

        org.locationtech.jts.geom.Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        SensorLocation sensorLocation = new SensorLocation();
        sensorLocation.setLocation(location);

        sensor.setSensorLocation(sensorLocation);
        user.setSensorSessions(List.of(sensorFix));

        when(jwtService.extractAllClaims(mockToken)).thenReturn(cLaims);
        when(userService.findByUsername("mockUser")).thenReturn(user);

        UserDTO userDTO = userService.getTheDetailOfALoggedInUser(mockToken);

        assertEquals(1L, userDTO.getId());
        assertEquals("Mockalangoza", userDTO.getFirstName());
        assertEquals(Role.WORKER, userDTO.getRole());
        assertEquals(1, userDTO.getSensorSessions().size());

        SensorFixDTO fixDTO = userDTO.getSensorSessions().get(0);
        assertEquals("Sensor 15", fixDTO.getSensorName());
        assertEquals(25.0, fixDTO.getLatitude());
        assertEquals(25.0, fixDTO.getLongitude());

    }

    @Test
    void shouldMarkNotificationIsReadTrue() throws Exception {
        Long mockUserId = 13L;

        Notification mockNotification = new Notification();
        mockNotification.setId(101L);
        mockNotification.setIsRead(false);
        mockNotification.setType(NotificationType.TASK);
        mockNotification.setReceiverId(mockUserId);
        mockNotification.setSenderId(15L);
        mockNotification.setCreatedAt(LocalDateTime.now());
        mockNotification.setSuperVizorDeadline(LocalDateTime.now().plusDays(7));

        when(notificationRepository.findByReceiverIdAndIsReadFalse(mockUserId)).thenReturn(List.of(mockNotification));
        when(notificationRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));


        ResponseEntity<?> response = notificationService.markNotificationsOfRead(mockUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertTrue(mockNotification.getIsRead());

        verify(notificationRepository).findByReceiverIdAndIsReadFalse(mockUserId);
        verify(notificationRepository).saveAll(List.of(mockNotification));

    }


    @Test
    void userLocationShouldReturnUserLocationDTO() throws Exception {
        String mockUsername = "TestUser";
        User mockUser = new User();
        mockUser.setId(102L);
        mockUser.setFirstName(mockUsername);

        UserLocation mockLocation = new UserLocation();
        Point point = new GeometryFactory().createPoint(new Coordinate(20, 25));
        mockLocation.setLocation(point);
        mockUser.setUserLocation(mockLocation);


        when(userRepository.findByFirstName(mockUsername)).thenReturn(mockUser);

        UserLocationDTO userLocationDTO = new UserLocationDTO();
        userLocationDTO.setId(102L);
        userLocationDTO.setLatitude(25);
        userLocationDTO.setLongitude(20);

        UserLocationDTO userLocationResponse = userLocationService.getLocation(mockUsername);

        assertEquals(102L, userLocationDTO.getId());
        assertEquals("TestUser", mockUser.getFirstName());
        assertEquals(25.0, userLocationDTO.getLatitude());
        assertEquals(20.0, userLocationDTO.getLongitude());
        assertEquals(userLocationDTO.getId(), userLocationResponse.getId());
        assertEquals(userLocationDTO.getLatitude(), userLocationResponse.getLatitude());
        assertEquals(userLocationDTO.getLongitude(), userLocationResponse.getLongitude());


    }


    @Test
    void getPastSensorsOfWorker_whenUserExistsAndHasCompletedSensors_shouldReturnSensorList() throws Exception {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        String mockUsername = SecurityContextHolder.getContext().getAuthentication().getName();


        User mockUser = new User();
        mockUser.setId(2L);
        mockUser.setFirstName(mockUsername);

        when(userService.findByUsername(mockUsername)).thenReturn(mockUser);

        Sensor mockSensor = new Sensor();
        mockSensor.setId(15L);
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        Date nextWeek = calendar.getTime();


        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(100L);
        mockSensorFix.setUser(mockUser);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setCompletedTime(nextWeek);
        mockSensorFix.setStartTime(currentDate);
        mockSensorFix.setFinalStatus(SensorStatus.FAULTY);



        mockUser.setSensorSessions(List.of(mockSensorFix));
        when(sensorSessionRepository.findAllByUserAndCompletedTimeIsNotNull(mockUser))
                .thenReturn(List.of(mockSensorFix));


        List<SensorFix> result = sensorService.getPastSensorsOfWorker();

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getId());

        verify(userService).findByUsername("testUser");
        verify(sensorSessionRepository).findAllByUserAndCompletedTimeIsNotNull(mockUser);


    }

    @Test
    void getWorkersPastNonTaskSensorDetail_whenValidIdProvided_shouldReturnSuccessfullyGotsensor() throws Exception {
        Long mockSensorId = 10L;
        byte[] mockImageData = "mock-image-data".getBytes();

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        Date nextWeek = calendar.getTime();


        Sensor mockSensor = new Sensor();
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

        double latitude = 25.0;
        double longitude = 25.0;

        org.locationtech.jts.geom.Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        SensorLocation sensorLocation = new SensorLocation();

        mockSensor.setId(10L);
        mockSensor.setStatus(SensorStatus.ACTIVE);
        mockSensor.setSensorLocation(sensorLocation);
        sensorLocation.setLocation(location);


        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(10L);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setCompletedTime(nextWeek);
        mockSensorFix.setStartTime(currentDate);
        mockSensorFix.setFinalStatus(SensorStatus.SOLVED);
        mockSensorFix.setSensor(mockSensor);
        when(sensorSessionRepository.findById(mockSensorFix.getId())).thenReturn(Optional.of(mockSensorFix));

        mockSensor.setCurrentSensorSession(mockSensorFix);

        SensorSessionImages sensorSessionImages = new SensorSessionImages();
        sensorSessionImages.setId(10L);
        sensorSessionImages.setName("mockımage.png");
        sensorSessionImages.setType("image/png");
        sensorSessionImages.setImage(ImageUtil.compressImage(mockImageData));
        when(sensorSessionImagesRepository.findBySensorSessionsId(mockSensorId)).thenReturn(List.of(sensorSessionImages));


        SensorIconImage sensorIconImage = new SensorIconImage();
        sensorIconImage.setId(mockSensorId);
        sensorIconImage.setName("mockimage.png");
        sensorIconImage.setType("image/png");
        sensorIconImage.setImage(ImageUtil.compressImage(mockImageData));
        when(sensorImageIconRepository.findBySensorId(mockSensorId)).thenReturn(sensorIconImage);

        SensorSessionDTO sensorSessionDTO = new SensorSessionDTO();
        sensorSessionDTO.setId(10l);
        sensorSessionDTO.setSensorName("sensorname");
        sensorSessionDTO.setDisplayName(SensorStatus.ACTIVE.getDisplayName());
        sensorSessionDTO.setStartTime(new Date());
        sensorSessionDTO.setFinalStatus(SensorStatus.ACTIVE);
        sensorSessionDTO.setCompletedTime(new Date(System.currentTimeMillis() + 3600000));
        sensorSessionDTO.setLatitude(41.0082);
        sensorSessionDTO.setLongitude(28.9784);
        sensorSessionDTO.setImageResponseDTO(null);
        sensorSessionDTO.setNote("Mock data note");

        sensorSessionDTO.setStartTime(new Date());
        sensorSessionDTO.setSensorIconImage(null);

        ResponseEntity<ApiResponse> getWorkersPastNonTaskSensorDetailResponse = sensorService.getWorkersPastNonTaskSensorDetail(mockSensorId);

        assertNotNull(getWorkersPastNonTaskSensorDetailResponse);
        assertEquals(HttpStatus.ACCEPTED, getWorkersPastNonTaskSensorDetailResponse.getStatusCode());
        assertEquals("Successfully got sensor", getWorkersPastNonTaskSensorDetailResponse.getBody().getMessage());
    }

    @Test
    void getNotFound_whenInValidIdProvided_shouldReturnNotFound() throws Exception {
        Long invalidSensorMockId = -1L;

        ResponseEntity<ApiResponse> result = sensorService.getWorkersPastNonTaskSensorDetail(invalidSensorMockId);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Invalid Sensor id.", result.getBody().getMessage());
        assertFalse(result.getBody().isSuccess());
        assertEquals(404, result.getBody().getStatus());

        verify(sensorSessionRepository, never()).findById(any());
        verify(sensorSessionImagesRepository, never()).findBySensorSessionsId(any());
        verify(sensorImageIconRepository, never()).findBySensorId(any());
    }

    @Test
    void getNotFound_whenNullIdProvided_shouldReturnNotFound() throws Exception {

        ResponseEntity<ApiResponse> result = sensorService.getWorkersPastNonTaskSensorDetail(null);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Sensor doesnt exists.", result.getBody().getMessage());
        assertFalse(result.getBody().isSuccess());
        assertEquals(404, result.getBody().getStatus());

        verify(sensorSessionRepository, never()).findById(any());
        verify(sensorSessionImagesRepository, never()).findBySensorSessionsId(any());
        verify(sensorImageIconRepository, never()).findBySensorId(any());
    }


    @Test
    void shouldReturnSuccessWithEmptyData_whenNoSensorFound() throws Exception {
        Long sensorId = 999L;
        when(sensorSessionRepository.findById(sensorId)).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse> mockApiResponse = sensorService.getWorkersPastNonTaskSensorDetail(sensorId);

        assertEquals(NOT_FOUND, mockApiResponse.getStatusCode());
        assertNull(mockApiResponse.getBody().getData());
        assertEquals("Sensor doesnt exists.", mockApiResponse.getBody().getMessage());


        verify(sensorSessionRepository).findById(sensorId);
        verify(sensorSessionImagesRepository, never()).findBySensorSessionsId(any());
        verify(sensorImageIconRepository, never()).findBySensorId(any());

    }


    @Test
    void getAllWorkersWithSensorSessionAndLocations() throws Exception {
        //mock user 1
        User mockUser = new User();
        mockUser.setId(2L);
        mockUser.setFirstName("mockUsername");
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        double latitude = 25.0;
        double longitude = 25.0;
        org.locationtech.jts.geom.Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        UserLocation userLocation1 = new UserLocation();
        userLocation1.setLocation(location);
        mockUser.setUserLocation(userLocation1);

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        Date nextWeek = calendar.getTime();
        //mockusersensorsession sensor 1
        Sensor mockSensor = new Sensor();

        double sensorLatitude = 35.0;
        double sensorLongitude = 19.0;

        org.locationtech.jts.geom.Point sensorLocationPoint = geometryFactory.createPoint(new Coordinate(sensorLongitude, sensorLatitude));
        SensorLocation sensorLocation = new SensorLocation();

        sensorLocation.setLocation(sensorLocationPoint);

        mockSensor.setId(10L);
        mockSensor.setStatus(SensorStatus.ACTIVE);
        mockSensor.setSensorLocation(sensorLocation);
        sensorLocation.setLocation(location);

        SensorFix mockSensorFix = new SensorFix();
        mockSensorFix.setId(10L);
        mockSensorFix.setNote("MockatoMock Note");
        mockSensorFix.setCompletedTime(nextWeek);
        mockSensorFix.setStartTime(currentDate);
        mockSensorFix.setFinalStatus(SensorStatus.SOLVED);
        mockSensorFix.setSensor(mockSensor);

        //mockUser2
        User mockUser2 = new User();
        mockUser2.setId(3L);
        mockUser2.setFirstName("mockUsername2");
        GeometryFactory geometryFactory2 = new GeometryFactory(new PrecisionModel(), 4326);
        double latitude2 = 29.0;
        double longitude2 = 32.0;
        org.locationtech.jts.geom.Point location2 = geometryFactory2.createPoint(new Coordinate(longitude2, latitude2));
        UserLocation userLocation2 = new UserLocation();
        userLocation2.setLocation(location2);
        mockUser2.setUserLocation(userLocation2);


        when(userRepository.findAll()).thenReturn(List.of(mockUser, mockUser2));
        //mock sensor 2
        Sensor mockSensor2 = new Sensor();
        mockSensor2.setId(999L);
        Point sensorLocation2 = geometryFactory.createPoint(new Coordinate(26.0, 26.0));
        SensorLocation sensorLocation3 = new SensorLocation();
        sensorLocation3.setLocation(sensorLocation2);
        mockSensor.setSensorLocation(sensorLocation3);


        SensorFix mockSensorFix2 = new SensorFix();
        mockSensorFix2.setId(10L);
        mockSensorFix2.setNote("MockatoMock Note");
        mockSensorFix2.setFinalStatus(SensorStatus.SOLVED);
        mockSensorFix2.setUser(mockUser);

        mockUser.setSensorSessions(List.of(mockSensorFix));
        mockUser2.setSensorSessions(List.of(mockSensorFix2));


        UserAndSessionSensorDTO mockuserAndSessionSensorDTO1 = new UserAndSessionSensorDTO();
        mockuserAndSessionSensorDTO1.setId(mockUser.getId());
        mockuserAndSessionSensorDTO1.setName(mockUser.getFirstName());

        mockuserAndSessionSensorDTO1.setLatitude(mockUser.getUserLocation().getLocation().getX());
        mockuserAndSessionSensorDTO1.setLongitude(mockUser.getUserLocation().getLocation().getY());


        List<UserAndSessionSensorDTO> mockApiResponse = userLocationService.getAllWorkersSessionSensorAndTheirLocation();

        UserAndSessionSensorDTO dto = mockApiResponse.get(0);
        assertEquals(mockUser.getId(), dto.getId());
        assertEquals(mockUser.getFirstName(), dto.getName());
        assertEquals(userLocation1.getLocation().getY(), dto.getLatitude(), 0.001);
        assertEquals(userLocation1.getLocation().getX(), dto.getLongitude(), 0.001);
//        assertEquals(sensorLocation2.getY(), dto.getSensorlatitude(), 0.001);
        assertEquals(mockSensor, dto.getSensor());

    }

    @Test
    void returnNotFound_whenNoWorkersFound() throws Exception {
        List<UserAndSessionSensorDTO> userAndSessionSensorDTOList = new ArrayList<>();

        verify(userRepository, never()).findAll();

        List<UserAndSessionSensorDTO> mockApiResponse = userLocationService.getAllWorkersSessionSensorAndTheirLocation();
        assertEquals(userAndSessionSensorDTOList, mockApiResponse);

    }

    @Test
    void shouldDeleteUserById() throws Exception{
        Long mockUserId = 2L;
        User user  = new User();
        user.setId(mockUserId);

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(mockUserId);


        ResponseEntity<ApiResponse> response = userService.deleteUserById(mockUserId);
        System.out.println(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

    }


    @Test
    void shouldDeactivateUserById() throws Exception{
        Long mockUserId = 10L;
        User user  = new User();
        user.setId(mockUserId);
        user.setIsActive(true);

        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn( user);

        ResponseEntity response = userService.deactivateUserById(mockUserId);


        assertNotNull(response.getBody());
        ApiResponse  mockResponse = (ApiResponse) response.getBody();
        System.out.println(mockResponse.getData());
        System.out.println(mockResponse.getStatus());

        System.out.println(mockResponse.getMessage());
        assertEquals("User Successfully deactivated",
                mockResponse.getData());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void  shouldGetAllSupervizor() throws  Exception{
        User mockSupervizor = new User();
        mockSupervizor.setId(15L);
        mockSupervizor.setFirstName("supervizormock");
        mockSupervizor.setSurName("supervizorsurnamemock");
        mockSupervizor.setRole(Role.SUPERVISOR);

        User mockSupervizor2 = new User();
        mockSupervizor2.setId(20L);
        mockSupervizor2.setFirstName("supervizormock2");
        mockSupervizor.setSurName("supervizorsurname2mock");
        mockSupervizor2.setRole(Role.SUPERVISOR);
        List<User> userList = new ArrayList<>();

        userList.add(mockSupervizor);
        userList.add(mockSupervizor2);

        when(userRepository.findAllByRole(Role.SUPERVISOR)).thenReturn(userList);
        UserOnlineStatusDTO userOnlineStatusDTO1 = new UserOnlineStatusDTO();
        UserOnlineStatusDTO userOnlineStatusDTO2 = new UserOnlineStatusDTO();

        userOnlineStatusDTO1.setId(15L);
        userOnlineStatusDTO1.setFirstName(mockSupervizor.getFirstName());
        userOnlineStatusDTO1.setSurName(mockSupervizor.getSurName());
        userOnlineStatusDTO1.setRole(mockSupervizor.getRole());

        userOnlineStatusDTO2.setId(20L);
        userOnlineStatusDTO2.setFirstName(mockSupervizor2.getFirstName());
        userOnlineStatusDTO2.setSurName(mockSupervizor2.getSurName());
        userOnlineStatusDTO2.setRole(mockSupervizor2.getRole());


        List<UserOnlineStatusDTO> userOnlineStatusDTOList = new ArrayList<>();
        userOnlineStatusDTOList.add(userOnlineStatusDTO1);
        userOnlineStatusDTOList.add(userOnlineStatusDTO2);

        List<UserOnlineStatusDTO> mockSuperVizorList  =  userService.getAllSuperVizor();

        assertEquals(mockSupervizor.getId(),mockSuperVizorList.get(0).getId());
        assertEquals(mockSupervizor.getFirstName(),mockSuperVizorList.get(0).getFirstName());
        assertEquals(mockSupervizor.getSurName(),mockSuperVizorList.get(0).getSurName());
        assertEquals(mockSupervizor2.getId(),mockSuperVizorList.get(1).getId());
        assertEquals(mockSupervizor2.getFirstName(),mockSuperVizorList.get(1).getFirstName());
        assertEquals(mockSupervizor2.getSurName(),mockSuperVizorList.get(1).getSurName());

        assertEquals(userOnlineStatusDTOList,mockSuperVizorList);
        assertNotNull(mockSuperVizorList);


    }

    @Test
    void  shouldGetAllSupervizorAndWorker() throws  Exception{
        User mockSupervizor = new User();
        mockSupervizor.setId(15L);
        mockSupervizor.setFirstName("supervizormock");
        mockSupervizor.setSurName("supervizorsurnamemock");
        mockSupervizor.setRole(Role.SUPERVISOR);
        mockSupervizor.setLastLoginTime(new Date());

        User mockWorker = new User();
        mockWorker.setId(20L);
        mockWorker.setFirstName("workermock");
        mockWorker.setSurName("workersurnamemock");
        mockWorker.setRole(Role.WORKER);
        mockWorker.setLastLoginTime(new Date());


        List<User> userList = new ArrayList<>();

        userList.add(mockSupervizor);
        userList.add(mockWorker);

        when(userRepository.findAllExceptManager()).thenReturn(userList);


        UserAndSupervizorsDTO userAndSupervizorsDTO = new UserAndSupervizorsDTO();
        UserAndSupervizorsDTO userAndSupervizorsDTO2 = new UserAndSupervizorsDTO();

        userAndSupervizorsDTO.setId(15L);
        userAndSupervizorsDTO.setFirstName(mockSupervizor.getFirstName());
        userAndSupervizorsDTO.setSurName(mockSupervizor.getSurName());
        userAndSupervizorsDTO.setRole(mockSupervizor.getRole());
        userAndSupervizorsDTO.setLastLoginTime(new Date());

        userAndSupervizorsDTO2.setId(20L);
        userAndSupervizorsDTO2.setFirstName(mockWorker.getFirstName());
        userAndSupervizorsDTO2.setSurName(mockWorker.getSurName());
        userAndSupervizorsDTO2.setRole(mockWorker.getRole());
        userAndSupervizorsDTO2.setLastLoginTime(new Date());


        List<UserAndSupervizorsDTO> userOnlineStatusDTOList = new ArrayList<>();
        userOnlineStatusDTOList.add(userAndSupervizorsDTO);
        userOnlineStatusDTOList.add(userAndSupervizorsDTO2);

        List<UserAndSupervizorsDTO> mockUserListExceptManager  =  userService.getAllSupervizorAndWorker();

        System.out.println(mockSupervizor.getLastLoginTime());
        System.out.println(mockWorker.getUserOnlineStatus());

        assertEquals(mockSupervizor.getId(),mockUserListExceptManager.get(0).getId());
        assertEquals(mockSupervizor.getFirstName(),mockUserListExceptManager.get(0).getFirstName());
        assertEquals(mockSupervizor.getSurName(),mockUserListExceptManager.get(0).getSurName());

        assertEquals(mockWorker.getId(),mockUserListExceptManager.get(1).getId());
        assertEquals(mockWorker.getFirstName(),mockUserListExceptManager.get(1).getFirstName());
        assertEquals(mockWorker.getSurName(),mockUserListExceptManager.get(1).getSurName());

        assertNotNull(mockUserListExceptManager);

        verify(userRepository, times(1)).findAllExceptManager();

    }

    @Test
    void  shouldGetAllSensorForManagerUse() throws  Exception{

    }
}
