package com.example.EcoTrack.security.customUserDetail;

import com.example.EcoTrack.user.model.User;
import com.example.EcoTrack.user.repository.UserRepository;
import com.example.EcoTrack.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        ;
        if (user == null) {
            throw new UsernameNotFoundException("Kullanıcı bulunamadı: " + email);
        }

        return new UserPrincipal(user);
    }

}
