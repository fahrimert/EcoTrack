package com.example.EcoTrack.auth.security.customUserDetail;

import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.UserRepository;
import com.example.EcoTrack.auth.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String firstName) throws UsernameNotFoundException {
        User user = userRepository.findByFirstName(firstName);
        if (user == null) {
            throw new UsernameNotFoundException("Kullanıcı bulunamadı: " + firstName);
        }
        return new UserPrincipal(user);
    }

}
