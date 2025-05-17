package com.example.EcoTrack.service;

import com.example.EcoTrack.model.User;
import com.example.EcoTrack.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.*;
import io.jsonwebtoken.security.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtService {
    private  String secretKey="";
    private UserRepository userRepository;
    private JwtService jwtService;
    private UserDetailsService userDetailService;
    private SecretKey getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public JwtService(UserRepository userRepository,  UserDetailsService userDetailService){
        this.userRepository = userRepository;
        this.userDetailService = userDetailService;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private static final long EXPIRATION_MS = 2 * 60 * 60 * 1000 ; // 2 SAAT (test için)


    //ezberlemek gerekebilir
    //zorunlu alanlar subject expiration issuedAt diğerleri opsiyonel
    //signwith e algoritma ile imzalayıp güvenli hale getiiyor herhalde o yüzden
    public String generateToken(String firstName) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);
        Map<String , Object > claims = new HashMap();

        Collection<? extends GrantedAuthority> authorities = userDetailService.loadUserByUsername(firstName).getAuthorities();
        claims.put("authorities" , authorities.stream().map(authority ->  authority.getAuthority()).collect(Collectors.toList()) );

        User user  = userRepository.findByFirstName(firstName);
        return  Jwts.builder()
                .claims()
                .add(claims)
                .subject(firstName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate)
                .and()
                .signWith(getKey())
                .compact();
    };
    public  String extractFirstname(String  token){
        return extractAllClaims(token).getSubject();
    }
    public  boolean verify(String token){
        Claims claims =  Jwts.parser()
                .verifyWith(getKey())
                .clockSkewSeconds(30)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        if(userRepository.findByFirstName(claims.getSubject()) == null) {
            throw new JwtException("User not found");
        }
        return  true;



    }

    public  <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                ;
    }

}
