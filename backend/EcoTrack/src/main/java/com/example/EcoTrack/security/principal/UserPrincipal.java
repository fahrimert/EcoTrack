package com.example.EcoTrack.security.principal;

import com.example.EcoTrack.user.model.Role;
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
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
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
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getIsActive() != null && user.getIsActive();
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }
}
