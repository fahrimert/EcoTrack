package com.example.EcoTrack.service;

import com.example.EcoTrack.model.TwoFactorCode;
import com.example.EcoTrack.model.User;
import com.example.EcoTrack.repository.UserRepository;
import com.example.EcoTrack.repository.twoFactorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


@Service
public class TwoFactorTokenService {
    private final twoFactorRepository twoFactorRepositoryy;
    private  final UserRepository userRepository;

    public TwoFactorTokenService(twoFactorRepository twoFactorRepositoryy, UserRepository userRepository) {
        this.twoFactorRepositoryy = twoFactorRepositoryy;
        this.userRepository = userRepository;
    }
    Date now = new Date();
    private static final long EXPIRATION_MS = (5 * 60 * 1000);; // 5 dakka

    Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);
    public  String  createTwoFactoAuthToken(User savedUser){
        String twoFactorCodeUUID = UUID.randomUUID().toString();
        User existingUser = userRepository.findByFirstName(savedUser.getFirstName());
        if (existingUser.getTwoFactorCode() != null) {
            twoFactorRepositoryy.delete(existingUser.getTwoFactorCode());
        }
        if (existingUser.getTwoFactorCode() == null){
            TwoFactorCode twoFactorCode =new TwoFactorCode();

            twoFactorCode.setUser(existingUser);
            twoFactorCode.setExpiresAt(expiryDate);
            twoFactorCode.setTwoFactorToken(twoFactorCodeUUID);

            existingUser.setTwoFactorCode(twoFactorCode);

            System.out.println(twoFactorCode.getTwoFactorToken());

            twoFactorRepositoryy.save(twoFactorCode);
            return  twoFactorCode.getTwoFactorToken();
        }
        else {
            return existingUser.getTwoFactorCode().getTwoFactorToken();
        }

    }

    public ResponseEntity<String> verifyTwoFactorToken(String token){
        TwoFactorCode twoFactorCode = twoFactorRepositoryy.findBytwoFactorToken(token);
        User user = twoFactorCode.getUser();


        try {
            if (user.getTwoFactorCode().getTwoFactorToken().equals(token) && user.getTwoFactorCode().getExpiresAt().after(now)){
                user.setTwoFactorAuthbeenverified(true);
                userRepository.save(user);
                return  ResponseEntity.status(HttpStatus.ACCEPTED).body("2 factor token is valid");
            }
            else{
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("2 factor token is not valid");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



}
