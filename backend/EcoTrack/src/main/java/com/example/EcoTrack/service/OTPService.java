package com.example.mertsecurity.Service;

import com.example.mertsecurity.model.ResetPasswordToken;
import com.example.mertsecurity.model.Usera;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OTPService {

    private TwoFactorTokenService twoFactorTokenService;

    private ResetPasswordTokenService resetPasswordTokenService;

    public OTPService(TwoFactorTokenService twoFactorTokenService,  ResetPasswordTokenService resetPasswordTokenService) {
        this.twoFactorTokenService = twoFactorTokenService;
        this.resetPasswordTokenService = resetPasswordTokenService;
    }

    public ResponseEntity<String> sendCodeToEmailForResetting(Usera userInTheSession) {



        String resetTokenCode = resetPasswordTokenService.createPasswordResetToken(userInTheSession);
        try {
            Resend resend = new Resend("re_9YFHXVWv_5wJDtrGF9rzQARhksq5DSYwq");

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("onboarding@resend.dev")
                    .to("fahotunuckaya@gmail.com")
                    .subject("Reset Password")
                    .text("Your reset link is " +
                            "http://localhost:8080/twofactorToken" + resetTokenCode)
                    .build();

            try {
                CreateEmailResponse data = resend.emails().send(params);
                return   ResponseEntity.status(HttpStatus.ACCEPTED).body("Email sent!!");

            } catch (ResendException e) {
                e.printStackTrace();
                return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!!");

            }
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
            return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!!");

        }
    }





    public void sendCodeToEmail(Usera savedUser ){
        String twoFactorCode = twoFactorTokenService.createTwoFactoAuthToken(savedUser);
        try{
                Resend resend = new Resend("re_9YFHXVWv_5wJDtrGF9rzQARhksq5DSYwq");

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("onboarding@resend.dev")
                    .to("fahotunuckaya@gmail.com")
                    .subject("2Fa verification")
                    .text("Your 2fa token is " + twoFactorCode + "please verify")
                    .build();
            try{
                CreateEmailResponse data = resend.emails().send(params);
                System.out.println(data.getId());
            }catch (ResendException e){
                e.printStackTrace();
            }
    }
    catch(RuntimeException ex){
        System.err.println(ex.getMessage());
    }
    }



}
