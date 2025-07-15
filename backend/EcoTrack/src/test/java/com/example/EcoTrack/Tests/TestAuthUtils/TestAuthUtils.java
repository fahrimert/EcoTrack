package com.example.EcoTrack.Tests.TestAuthUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import javax.crypto.SecretKey;


public class TestAuthUtils {

    private static final String MOCK_SECRET_KEY = "YH8m5gQ7qFDs8UzAfh07e4YH9PWx7tNQkV2T3n8KJ9ZsrVe/1ACmQvLDaU39MfVjc5KL7/BVFC6RzBptn3FJvw==\n";


    private static SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(MOCK_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }



  
    private  static final Long MOCK_USER_ID = 1L;

    private  static  final  String MOCK_USERNAME = "test@example.com";

    public  static  String generateMockToken(){
        return Jwts.builder()
                .setSubject(MOCK_USER_ID.toString())
                .claim("TestUser",MOCK_USERNAME)
                .signWith(getKey())
                .compact();
    }
    public static MockHttpServletRequestBuilder addTokenToRequest(MockHttpServletRequestBuilder request) {
        return request.header("Authorization", "Bearer " + generateMockToken());
    }
}
