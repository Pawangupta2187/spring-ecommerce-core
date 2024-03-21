package com.coremodule.coremodule.service;

import com.coremodule.coremodule.security.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoggedInService {

    public String getLoggedInUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser userDetails = (AppUser) authentication.getPrincipal();
        return userDetails.getUsername();
    }
    public List<String> getLoggedInAuthority()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser userDetails = (AppUser) authentication.getPrincipal();
        return userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}

