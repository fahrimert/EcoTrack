package com.example.EcoTrack.auth.security.principal;

import com.example.EcoTrack.model.Role;
import com.example.EcoTrack.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserPrincipal implements UserDetails {

    private User user;
    private Role roles;

    public UserPrincipal(User user) {
        this.user = user;
        this.roles = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return   Stream.concat(
                Stream.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())),
                roles.getPermissions().stream().map(SimpleGrantedAuthority::new)
        )
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return  user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getFirstName();
    }
}
