package com.coremodule.coremodule.security;

import org.springframework.security.core.GrantedAuthority;


public class GrantAuthorityImplementation implements GrantedAuthority {

    String authority;

    public GrantAuthorityImplementation(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}