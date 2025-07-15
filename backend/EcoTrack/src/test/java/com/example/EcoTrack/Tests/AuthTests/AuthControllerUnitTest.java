package com.example.EcoTrack.Tests.AuthTests;

import com.example.EcoTrack.auth.controller.AuthController;
import com.example.EcoTrack.auth.dto.UserRequestDTO;
import com.example.EcoTrack.auth.service.AuthService;
import com.example.EcoTrack.shared.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)

public class AuthControllerUnitTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private  AuthService authService;

    @InjectMocks
    private AuthController controller;

    @Mock
    private BindingResult bindingResult;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void login_ShouldReturnTokensAnd200_WhenRequestIsValid() throws Exception{
        UserRequestDTO validRequest = new UserRequestDTO(
                "workeruser66@example.com",
                "WorkerUser66",
                "test1234"
        );



        ApiResponse<?> expectedResponse = ApiResponse.success(
                        Map.of(
                                "accessToken", "mockAccessToken",
                                "refreshToken", "MockRefreshToken"
                        )
                );

        when(authService.login(validRequest)).thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<ApiResponse<?>> response = controller.login(validRequest,bindingResult);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("mockAccessToken", ((Map) response.getBody().getData()).get("accessToken"));
        assertEquals("MockRefreshToken", ((Map) response.getBody().getData()).get("refreshToken"));

    }




}
