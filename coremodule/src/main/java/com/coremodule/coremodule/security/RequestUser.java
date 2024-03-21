package com.coremodule.coremodule.security;

import com.coremodule.coremodule.entities.users.User;
import com.coremodule.coremodule.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Component
@Slf4j
@Order(1)
public class RequestUser implements Filter {
@Autowired
    UserRepository userRepository;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest) servletRequest;

        log.info("Request Method{}",httpServletRequest.getMethod());
        httpServletRequest.getUserPrincipal();
        Principal principal = httpServletRequest.getUserPrincipal();
        log.info("login user>>>>>>>>>"+principal.getName());
        String email=principal.getName();
        List<User> user=userRepository.findUserByemailId(email);


       // return principal.getName();
        filterChain.doFilter(servletRequest,servletResponse);
        HttpServletResponse httpServletResponse=(HttpServletResponse) servletResponse;
    }
}
