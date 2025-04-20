package com.example.EcoTrack.security.filters;

import com.example.EcoTrack.repository.UserRepository;
import com.example.EcoTrack.security.customUserDetail.CustomUserDetailService;
import com.example.EcoTrack.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    private UserRepository userRepository;
    private CustomUserDetailService userDetailServicee;
    public JwtFilter(JwtService jwtService, CustomUserDetailService userDetailServicee, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userDetailServicee = userDetailServicee;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

    try{
        String customHeader = request.getHeader("Authorization");
        if (customHeader != null && customHeader.startsWith("Bearer ")){
                String token = customHeader.substring(7);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (jwtService.extractFirstname(token) != null && authentication == null ){
                    //authenticate etmem lazım
                    if (jwtService.verify(token)){
                        String firstName = jwtService.extractFirstname(token);
                        Claims claims = jwtService.extractAllClaims(token);
                        List<String> roles = claims.get("authorities", List.class);
                        UserDetails userDetails = userDetailServicee.loadUserByUsername(firstName);

                        List<GrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }


                }



        }


        filterChain.doFilter(request,response);}
        catch (ExpiredJwtException ex) {
            System.err.println("❌ Expired token: " + ex.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token süresi doldu. Lütfen yeniden giriş yapın.");
        } catch (JwtException | IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Geçersiz token");
        }
    }

}
