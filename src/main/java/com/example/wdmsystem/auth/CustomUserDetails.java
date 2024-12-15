package com.example.wdmsystem.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final int merchantId;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username, String password, int merchantId, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.merchantId = merchantId;
        this.authorities = authorities;
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
        return true;
    }
}
