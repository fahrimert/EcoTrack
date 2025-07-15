package com.example.EcoTrack.Tests.AuthTests;


import com.example.EcoTrack.auth.controller.AuthController;
import com.example.EcoTrack.auth.dto.UserRequestDTO;
import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.auth.service.JwtService;
import com.example.EcoTrack.auth.service.RefreshTokenService;
import com.example.EcoTrack.notification.service.NotificationService;
import com.example.EcoTrack.security.config.SecurityConfig;
import com.example.EcoTrack.sensors.service.SensorService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.example.EcoTrack.task.service.TaskService;
import com.example.EcoTrack.user.repository.UserOnlineStatusRepository;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.user.service.UserLocationService;
import com.example.EcoTrack.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
//bunların hepsi integration test olmalı
public class AuthIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserOnlineStatusRepository userOnlineStatusRepository;

    @Autowired
    SimpMessagingTemplate messagingTemplate;


    @Autowired
    UserService userService;

    @Autowired
    UserLocationService userLocationService;


    @Autowired
    NotificationService notificationService;

    @Autowired
    JwtService  jwtService;


    @Autowired
    SensorService sensorService;


    @Autowired
    TaskService taskService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailServicee;

    @Autowired
    private SecurityConfig securityConfig;



    @Autowired
    AuthController controller;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;



    //integrasyon testlerini yapmaya çalışıcam
    @Test
    void loginShouldReturn200_whenValidRequest() throws Exception{
        UserRequestDTO validRequest = new UserRequestDTO(
        );
        validRequest.setEmail("manageruser@example.com");
        validRequest.setFirstName("ManagerUser");
        validRequest.setPassword("test1234");

        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                "/auth/login",
                validRequest,
                ApiResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData())
                .isInstanceOf(Map.class)
                        .hasFieldOrProperty("accessToken")
                .hasFieldOrProperty("refreshToken");





}

        @Test
        void loginShouldReturn403_whenInvalidCredentials() throws Exception{
            UserRequestDTO invalidRequest = new UserRequestDTO(
            );
            invalidRequest.setEmail("wrongmanager@example.com");
            invalidRequest.setFirstName("WrongManager");
            invalidRequest.setPassword("test1234321");

            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                    "/auth/login",
                    invalidRequest,
                    ApiResponse.class
            );

            ApiResponse<?> mockResponse = ApiResponse.error(
                    "Error",
                    List.of("Credentials is wrong"),
                    HttpStatus.FORBIDDEN
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(response.getBody().getErrors().get(0))
                    .isEqualTo("User Not Found");
        }

    @Test
    void login_NullEmailRequest_ShouldReturnEmailCannotBeNull() throws  Exception {
        UserRequestDTO invalidRequest = new UserRequestDTO(
        );
        invalidRequest.setEmail("");
        invalidRequest.setFirstName("WrongManager");
        invalidRequest.setPassword("test1234321");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/auth/login",
                invalidRequest,
                String.class
        );
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> errorMap = mapper.readValue(response.getBody(), Map.class);



        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorMap.get("email")).isEqualTo("Email Kısmı Boş Kalamaz");



        //        assertThat(response.getBody().getErrors())
//                .asList()
//                .contains("Email Kısmı Boş Kalamaz");
//
//


    }

    @Test
    void login_wrongFormatEmailRequest_ShouldReturnEmailFormatWrong() throws  Exception {
        UserRequestDTO invalidRequest = new UserRequestDTO(
        );
        invalidRequest.setEmail("wrongmanagerwrongmanager.com");
        invalidRequest.setFirstName("ManagerUser");
        invalidRequest.setPassword("test1234");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/auth/login",
                invalidRequest,
                String.class
        );
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> errorMap = mapper.readValue(response.getBody(), Map.class);



        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorMap.get("email")).isEqualTo("Email Formatı Yanlış");





    }

    @Test
    void login_LessThanMinSizeFirstname_ShouldReturnError() throws  Exception {
        UserRequestDTO invalidRequest = new UserRequestDTO(
        );
        invalidRequest.setEmail("manageruser@example.com");
        invalidRequest.setFirstName("d");
        invalidRequest.setPassword("test1234");

        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                "/auth/login",
                invalidRequest,
                ApiResponse.class
        );

        assertThat(response.getBody().getErrors().get(0))
                .isEqualTo("İsim 2 ila 30 karakter arasında olmalı");
    }

    @Test
    void login_greaterThanMaxFirstname_ShouldReturnError() throws  Exception {
        UserRequestDTO invalidRequest = new UserRequestDTO(
        );
        invalidRequest.setEmail("manageruser@example.com");
        invalidRequest.setFirstName("mockusermockusermockusermockusermockusermockusermockusermockusermockusermockusermockuser");
        invalidRequest.setPassword("test1234");

        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                "/auth/login",
                invalidRequest,
                ApiResponse.class
        );

        assertThat(response.getBody().getErrors().get(0))
                .isEqualTo("İsim 2 ila 30 karakter arasında olmalı");

    }
    @Test
    void login_NullPasswordRequest_ShouldPasswordCannotBeNull() throws  Exception {
        UserRequestDTO invalidRequest = new UserRequestDTO(
        );
        invalidRequest.setEmail("manageruser@example.com");
        invalidRequest.setFirstName("ManagerUser");
        invalidRequest.setPassword("");

        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                "/auth/login",
                invalidRequest,
                ApiResponse.class
        );

        assertThat(response.getBody().getErrors().get(0))
                .isEqualTo("Parola Kısmı Boş Kalamaz");



    }

    @Test
    void login_NullPasswordRequestAndEmail_ShouldReturnBothNullEmailAndNullPasswordError() throws  Exception {
        UserRequestDTO invalidRequest = new UserRequestDTO(
        );
        invalidRequest.setEmail("");
        invalidRequest.setFirstName("ManagerUser");
        invalidRequest.setPassword("");

        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                "/auth/login",
                invalidRequest,
                ApiResponse.class
        );

        assertThat(response.getBody().getErrors())
                .isEqualTo( List.of("Parola Kısmı Boş Kalamaz","Email Kısmı Boş Kalamaz" ));



    }

    @Test
    void login_NullFirstnameRequestAndEmail_ShouldReturnBothNullFirstnameAndNullEmailError() throws  Exception {
        UserRequestDTO invalidRequest = new UserRequestDTO(
        );
        invalidRequest.setEmail("");
        invalidRequest.setFirstName("");
        invalidRequest.setPassword("test1234");

        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                "/auth/login",
                invalidRequest,
                ApiResponse.class
        );

        assertThat(response.getBody().getErrors())
                .contains( "Emoji veya geçersiz karakter",  "Email Kısmı Boş Kalamaz","İsim 2 ila 30 karakter arasında olmalı","İsim Kısmı Boş Kalamaz" );

    }

    @Test
    void login_NullFirstnameRequestAndPassword_ShouldReturnBothNullFirstnameAndPasswordIsNullError() throws  Exception {

        UserRequestDTO invalidRequest = new UserRequestDTO(
        );
        invalidRequest.setEmail("");
        invalidRequest.setFirstName("");
        invalidRequest.setPassword("test1234");

        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                "/auth/login",
                invalidRequest,
                ApiResponse.class
        );
        assertThat(response.getBody().getErrors())
                .contains(  "Email Kısmı Boş Kalamaz","İsim 2 ila 30 karakter arasında olmalı","İsim Kısmı Boş Kalamaz");

        ;


    }

    @Test
    void login_WhenAllFieldsBlank_ShouldReturnAllValidationErrors() throws  Exception {

            UserRequestDTO invalidRequest = new UserRequestDTO(
            );
            invalidRequest.setEmail("");
            invalidRequest.setFirstName("");
            invalidRequest.setPassword("");

            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                    "/auth/login",
                    invalidRequest,
                    ApiResponse.class
            );
            assertThat(response.getBody().getErrors())
                    .contains( "Parola Kısmı Boş Kalamaz" ,  "Email Kısmı Boş Kalamaz","İsim 2 ila 30 karakter arasında olmalı","İsim Kısmı Boş Kalamaz");

    }

    @Test
    void login_FirstNameWithEmoji_ShouldReturn400 () throws  Exception {
        UserRequestDTO invalidRequest = new UserRequestDTO(
        );
        invalidRequest.setEmail("mock@example.com");
        invalidRequest.setFirstName("\"Mock\\uD83D\\uDD25\"");
        invalidRequest.setPassword("test1234");

        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                "/auth/login",
                invalidRequest,
                ApiResponse.class
        );
        assertThat(response.getBody().getErrors())
                .contains( "Emoji veya geçersiz karakter" );


    }

    @Test
    public  void  shoulReturn_UnauthorizedStatusCode_And_UserDeactivated_Login(){
        UserRequestDTO invalidRequest = new UserRequestDTO(
        );
        invalidRequest.setEmail("workeruser4@example.com");
        invalidRequest.setFirstName("WorkerUser4");
        invalidRequest.setPassword("test1234");

        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                "/auth/login",
                invalidRequest,
                ApiResponse.class
        );
        assertThat(response.getBody().getErrors())
                .contains( "User Deactivated" );



    }



}
