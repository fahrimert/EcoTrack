package com.example.mertsecurity.Service;

import com.example.mertsecurity.model.RefreshToken;
import com.example.mertsecurity.model.Usera;
import com.example.mertsecurity.repository.RefreshTokenRepository;
import com.example.mertsecurity.repository.UserRepository;
import com.example.mertsecurity.security.Principal.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


@Service
public class RefreshTokenService  {
    private final RefreshTokenRepository refreshTokenRepository;
    private  final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }
    Date now = new Date();
    private static final long EXPIRATION_MS = 7 * 24 * 60 * 1000; // 7 gün

    Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);
    public  String  createRefreshToken(String username, Usera dbUser, UserRepository userRepository){


        RefreshToken refreshToken =new RefreshToken(
        ) ;
        refreshToken.setUser(dbUser);
        refreshToken.setExpiresAt(expiryDate);
        refreshToken.setDevice_info("a");
        refreshToken.setToken(UUID.randomUUID().toString());
        dbUser.setRefreshToken(refreshToken);

        RefreshToken refreshToken1 = refreshTokenRepository.save(refreshToken);
        return  refreshToken1.getToken();
    }
    public String findByToken(String token, HttpServletRequest request, HttpServletResponse response){
        RefreshToken tokenn = refreshTokenRepository.findBytoken(token);

        Usera usera = tokenn.getUser();
        RefreshToken prevToken = expiredVerify(tokenn);

        if (prevToken != null){
            return  jwtService.generateToken(usera.getUsername());
            //access token döndürmemiz lazım yeni
        }
        else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            new SecurityContextLogoutHandler().logout(request, response, authentication);

        }

        return token;
    }

    public RefreshToken expiredVerify(RefreshToken refreshToken){
        Date now = new Date();

        if (refreshToken.getExpiresAt().before(now)){
          refreshTokenRepository.deleteById(refreshToken.getId());
          throw  new RuntimeException("Refresh Token Expired");
        }
        else {
            return  refreshToken;
        }
    }
}
