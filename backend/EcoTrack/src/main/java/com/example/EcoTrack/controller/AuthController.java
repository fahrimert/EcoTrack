package com.example.mertsecurity.Controller;

import com.example.mertsecurity.Service.*;

import com.example.mertsecurity.model.ResetPassword;
import com.example.mertsecurity.model.TwoFactorCode;
import com.example.mertsecurity.model.Usera;
import com.example.mertsecurity.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.apache.coyote.http11.Constants.a;

@RestController
public class LoginController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private  UserRepository userRepository;
    private RefreshTokenService refreshTokenService;
    private TwoFactorTokenService twoFactorTokenService;
    private ResetPasswordService resetPasswordService;
    private  ResetPasswordTokenService resetPasswordTokenService;
    private OTPService otpService;
    public LoginController(UserService userService, RefreshTokenService refreshTokenService, TwoFactorTokenService twoFactorTokenService, ResetPasswordService resetPasswordService, ResetPasswordTokenService resetPasswordTokenService, OTPService otpService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.twoFactorTokenService = twoFactorTokenService;
        this.resetPasswordService = resetPasswordService;
        this.resetPasswordTokenService = resetPasswordTokenService;
        this.otpService = otpService;
    }


    @PostMapping("/register")
    public Usera register(@RequestBody Usera registerRequest){

                return userService.register(registerRequest);

    };

    @PostMapping("/login")
    public ResponseEntity<String[]> login(@RequestBody Usera loginRequest,HttpServletRequest request) {
        return userService.login(loginRequest);

    }

    @GetMapping("/login")
    public String  login() {
        return "Login page";
    }


    @PostMapping("/refreshToken/{refreshToken}")
    public String refreshTokenController(HttpServletRequest request, HttpServletResponse response, @PathVariable String refreshToken ){
return  refreshTokenService.findByToken(refreshToken,request,response);
    }


    @PostMapping("/twofactorToken")
    public ResponseEntity<String> twoFactorTokenController(@RequestBody TwoFactorCode twoFactorCode) {

        return  twoFactorTokenService.verifyTwoFactorToken(twoFactorCode.getTwoFactorToken());
    }

    @PostMapping("/changePassword")
    public  ResponseEntity<String> resetPasswordController(){
        //eğer hali hazırda token varsa o Tokeni yoksa  token oluşturup yeni email sent döndür
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usera userInTheSession = userService.findByUsername(username);
        return   otpService.sendCodeToEmailForResetting(userInTheSession);
    }

    @PostMapping("/changePassword/{resetPasswordToken}")
    public  ResponseEntity<String> resetPasswordController(@PathVariable String resetPasswordToken, @RequestBody ResetPassword resetPassword){
        //eğer hali hazırda token varsa o Tokeni yoksa  token oluşturup yeni email sent döndür
        resetPasswordTokenService.verifyResetPasswordToken(resetPasswordToken);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usera userInTheSession = userService.findByUsername(username);
        return resetPasswordService.resetPassword(resetPassword.getOldPassword(),resetPassword.newPassword,resetPassword.confirmNewPassword);
    }
}
