package com.example.EcoTrack.service;

import com.example.EcoTrack.model.User;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OTPService {

    private TwoFactorTokenService twoFactorTokenService;


    public OTPService(TwoFactorTokenService twoFactorTokenService) {
        this.twoFactorTokenService = twoFactorTokenService;
    }





    public void sendCodeToEmail(User savedUser ){
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
