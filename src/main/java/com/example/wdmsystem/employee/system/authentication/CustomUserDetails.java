package com.example.wdmsystem.employee.system.authentication;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.wdmsystem.merchant.system.Merchant;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final Merchant merchant;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username, String password, Merchant merchant, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.merchant = merchant;
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
