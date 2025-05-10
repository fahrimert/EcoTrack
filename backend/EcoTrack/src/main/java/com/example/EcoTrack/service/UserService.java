package com.example.EcoTrack.service;

import com.example.EcoTrack.dto.ApiResponse;
import com.example.EcoTrack.dto.UserRequestDTO;
import com.example.EcoTrack.model.RefreshToken;
import com.example.EcoTrack.model.User;
import com.example.EcoTrack.repository.RefreshTokenRepository;
import com.example.EcoTrack.repository.UserRepository;
import com.example.EcoTrack.security.customUserDetail.CustomUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.hibernate.Internal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private  final UserRepository userRepository;
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;


    private  final CustomUserDetailService userDetailServicee;
    private  final JwtService jwtService;
    private  final  RefreshTokenService refreshTokenService;
    private  final  OTPService otpService;
    private  final RefreshTokenRepository refreshTokenRepository;
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager,  CustomUserDetailService userDetailServicee1, JwtService jwtService, RefreshTokenService refreshTokenService, OTPService otpService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        this.authenticationManager = authenticationManager;
        this.userDetailServicee = userDetailServicee1;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.otpService = otpService;
        this.refreshTokenRepository = refreshTokenRepository;
    }



    public User findByUsername(String username) {
        return userRepository.findByFirstName(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //ilk step bu aga biz 3. step falan yazmışız buna
    public ResponseEntity<ApiResponse<?>> login (@RequestBody UserRequestDTO user){

            User dbUser = userRepository.findByFirstName(user.getFirstName());
            if (dbUser == null ) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(
                                "Not Found",
                                List.of("User Not Found"),
                                HttpStatus.FORBIDDEN
                        ));

            }
        if (!dbUser.getEmail().equals(user.getEmail     ())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Error",
                            List.of("Credentials is wrong"),
                            HttpStatus.FORBIDDEN
                    ));
        }
        if (!dbUser.getFirstName().equals(user.getFirstName())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Error",
                            List.of("Credentials is wrong"),
                            HttpStatus.FORBIDDEN
                    ));
        }
        System.out.println(bCryptPasswordEncoder.matches(user.getPassword(),dbUser.getPassword()));
        if (!bCryptPasswordEncoder.matches(user.getPassword(),dbUser.getPassword())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(
                            "Error",
                            List.of("Credentials is wrong"),
                            HttpStatus.FORBIDDEN
                    ));
        }
            if (dbUser.isTwoFactorAuthbeenverified() == false) {
                otpService.sendCodeToEmail(dbUser);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(
                                "2FA required",
                                List.of("Please check your email for 2 factor authentication"),
                                HttpStatus.FORBIDDEN
                        ));
            } else {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dbUser.getFirstName(),
                                user.getPassword()

                        )
                );

                UserDetails userDetails = userDetailServicee.loadUserByUsername(user.getFirstName());
                String token = jwtService.generateToken(userDetails.getUsername());
                String refreshToken = refreshTokenService.createRefreshToken(dbUser.getFirstName(), dbUser, userRepository);
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(ApiResponse.success(
                                Map.of(

                                        "accessToken", token,
                                        "refreshToken", refreshToken
                                )
                        ));

            }
        };

    public ResponseEntity<ApiResponse<Boolean>> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByFirstName(authentication.getName());

        if (authentication != null && authentication.isAuthenticated()){
            String token = request.getHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")){
                token = token.substring(7);
            }else{
                throw new RuntimeException("Access token does not exist");
            }

            RefreshToken refreshToken = refreshTokenService.findByUserId(user.getId());
            System.out.println(user);
            System.out.println(refreshToken.getUser().getFirstName());
            System.out.println(refreshToken.getId());

            user.setRefreshToken(null);
            userRepository.save(user);
            refreshTokenRepository.delete(refreshToken);
            new SecurityContextLogoutHandler().logout(request, response, authentication);

            return ResponseEntity.ok(ApiResponse.success(true));

        }
        else {
            throw new RuntimeException("User does not exists");
        }
    }


}
