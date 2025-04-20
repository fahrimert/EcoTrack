package com.example.EcoTrack.security.config;


import ch.qos.logback.classic.spi.ConfiguratorRank;
import com.example.EcoTrack.repository.UserRepository;
import com.example.EcoTrack.security.customUserDetail.CustomUserDetailService;
import com.example.EcoTrack.security.filters.JwtFilter;
import com.example.EcoTrack.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private CustomUserDetailService userDetailsService;
    private JwtService jwtService;

    private UserRepository userRepository;

    public SecurityConfig(CustomUserDetailService userDetailsService, JwtService jwtService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        authorize -> authorize.requestMatchers( "/login","/twofactorToken","/oauth2").permitAll()

                                //hem rol hem authority mevzusunu yapıyor authority yapınca admini korumuş oluyor
                                //authorities kısmında ise
                                .anyRequest().authenticated()
                )
//                .exceptionHandling(customizer -> customizer.authenticationEntryPoint( new  AuthenticationEntryPointt()))
                .sessionManagement(sessionmanagement -> sessionmanagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(jwtService, userDetailsService, userRepository), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return  new SessionRegistryImpl();
    }
    @Bean
    public AuthenticationManager authenticationManager(BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }



}
