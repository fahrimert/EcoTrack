package com.example.EcoTrack.security.filters;

import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.security.customUserDetail.CustomUserDetailService;
import com.example.EcoTrack.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final  UserRepository userRepository;
    private final  CustomUserDetailService userDetailServicee;

    public JwtFilter(JwtService jwtService, UserRepository userRepository, CustomUserDetailService userDetailServicee) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userDetailServicee = userDetailServicee;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final  String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;


    try{
        String customHeader = request.getHeader("Authorization");
        if (authHeader == null || !customHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = customHeader.substring(7);

        email = jwtService.extractEmail(jwt);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.verify(jwt)) {
                System.out.println("EMAİLL" + email);
                UserDetails userDetails = userDetailServicee.loadUserByUsername(email);
                System.out.println(userDetails.getUsername() + "userdetails getusername");
                System.out.println(userDetails.getAuthorities() + "userdetails getauthorities");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);

    } catch (ExpiredJwtException ex) {
        log.warn("Token süresi dolmuş: {}", ex.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Tokenin Süresi Doldu");
    }catch (Exception e) {
        log.error("JWT Filter Hatası: ", e); // e.getMessage() yerine e'nin kendisini veriyoruz.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // veya SC_INTERNAL_SERVER_ERROR
        response.getWriter().write("İç Sunucu Hatası: " + e.getMessage());
    }


    }

}
