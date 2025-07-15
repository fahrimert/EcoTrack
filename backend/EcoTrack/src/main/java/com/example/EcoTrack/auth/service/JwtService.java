    package com.example.EcoTrack.auth.service;

    import com.example.EcoTrack.user.repository.UserRepository;
    import io.jsonwebtoken.*;
    import io.jsonwebtoken.io.*;
    import io.jsonwebtoken.security.*;
    import jakarta.servlet.http.HttpServletRequest;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.stereotype.Component;
    import org.springframework.stereotype.Service;

    import javax.crypto.KeyGenerator;
    import javax.crypto.SecretKey;
    import java.security.NoSuchAlgorithmException;
    import java.util.*;
    import java.util.function.Function;
    import java.util.stream.Collectors;

    @Service
    public class JwtService {
        private UserRepository userRepository;
        private UserDetailsService userDetailService;

        private static final String secretKey  = "YH8m5gQ7qFDs8UzAfh07e4YH9PWx7tNQkV2T3n8KJ9ZsrVe/1ACmQvLDaU39MfVjc5KL7/BVFC6RzBptn3FJvw==\n";

        private SecretKey getKey(){
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        }
        public JwtService(UserRepository userRepository,  UserDetailsService userDetailService){
            this.userRepository = userRepository;
            this.userDetailService = userDetailService;

        }
        private static final long EXPIRATION_MS = 2 * 60 * 60 * 1000 ;


        public String generateToken(String firstName) {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + EXPIRATION_MS);
            Map<String , Object > claims = new HashMap();

            Collection<? extends GrantedAuthority> authorities = userDetailService.loadUserByUsername(firstName).getAuthorities();
            claims.put("authorities" , authorities.stream().map(authority ->  authority.getAuthority()).collect(Collectors.toList()) );

            return  Jwts.builder()
                    .claims()
                    .add(claims)
                    .subject(firstName)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(expiryDate)
                    .and()
                    .signWith(getKey())
                    .compact();
        };
        public  String extractFirstname(String  token){
            return extractAllClaims(token).getSubject();
        }
        public  boolean verify(String token){
            Claims claims =  Jwts.parser()
                    .verifyWith(getKey())
                    .clockSkewSeconds(30)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if(userRepository.findByFirstName(claims.getSubject()) == null) {
                throw new JwtException("User not found");
            }
            return  true;



        }

        public  <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
            final Claims claims = extractAllClaims(token);
            return claimResolver.apply(claims);
        }

        public Claims extractAllClaims(String token) {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    ;
        }

        public String extractTokenFromHeader(HttpServletRequest request) {
            String bearer = request.getHeader("Authorization");
            if (bearer != null && bearer.startsWith("Bearer ")){
                return bearer.substring(7);
            }
            throw new RuntimeException("No JWT token found in request headers");
        }
    }
