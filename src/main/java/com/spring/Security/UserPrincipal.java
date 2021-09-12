package com.spring.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails
{
    private UserCredentials userCredentials;

    public UserPrincipal(UserCredentials userCredentials)
    {
        this.userCredentials = userCredentials;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        List<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + this.userCredentials.getUserRole());
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword()
    {
        return this.userCredentials.getPassword();
    }

    @Override
    public String getUsername()
    {

        return this.userCredentials.getUserName();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}