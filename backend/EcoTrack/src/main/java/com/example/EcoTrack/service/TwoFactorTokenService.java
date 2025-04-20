package com.example.mertsecurity.Service;

import com.example.mertsecurity.model.RefreshToken;
import com.example.mertsecurity.model.TwoFactorCode;
import com.example.mertsecurity.model.Usera;
import com.example.mertsecurity.repository.RefreshTokenRepository;
import com.example.mertsecurity.repository.UserRepository;
import com.example.mertsecurity.repository.twoFactorRepository;
import com.example.mertsecurity.response.ApiResponse;
import com.example.mertsecurity.security.Principal.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


@Service
public class TwoFactorTokenService {
    private final twoFactorRepository twoFactorRepositoryy;
    private  final  UserRepository userRepository;

    public TwoFactorTokenService(twoFactorRepository twoFactorRepositoryy, UserRepository userRepository) {
        this.twoFactorRepositoryy = twoFactorRepositoryy;
        this.userRepository = userRepository;
    }
    Date now = new Date();
    private static final long EXPIRATION_MS = (5 * 60 * 1000);; // 5 dakka

    Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);
    public  String  createTwoFactoAuthToken(Usera savedUser){
        String twoFactorCodeUUID = UUID.randomUUID().toString();
        Usera existingUser = userRepository.findByUsername(savedUser.getUsername());
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
        Usera user = twoFactorCode.getUser();


        try {
            if (user.getTwoFactorCode().getTwoFactorToken().equals(token) && user.getTwoFactorCode().getExpiresAt().after(now)){
                user.setTwoFactorAuthbeenverified(true);
                userRepository.save(user);
                System.out.println(userRepository.findByUsername(user.getUsername()).isTwoFactorAuthbeenverified() +  "isit false or true");
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
