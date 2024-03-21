package com.coremodule.coremodule.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AppUser implements UserDetails {

    private String useremail;
    private String password;
    private Boolean isEnabled;
    private Boolean isDelete;
    private boolean isAccountNonLocked;

    List<GrantAuthorityImplementation> grantAuthorities;

    public AppUser(String useremail, String password,
                   List<GrantAuthorityImplementation> grantAuthorities,Boolean isEnabled,Boolean isDelete,Boolean isAccountNonLocked) {
        this.useremail = useremail;
        this.password = password;
        this.isEnabled=isEnabled;
        this.isDelete=isDelete;
        this.isAccountNonLocked=isAccountNonLocked;
        this.grantAuthorities = grantAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return useremail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isDelete;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}