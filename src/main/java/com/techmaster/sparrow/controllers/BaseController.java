package com.techmaster.sparrow.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseController {

    public static final String SUCCESS_RETRIEVAL_MSG = "Successfully retrieved the data";

    protected UserDetails getUserDetails() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new UsernameNotFoundException("User could not be found in the context");
        }
        return userDetails;
    }

    protected String getUserName() {
        UserDetails userDetails = getUserDetails();
        return userDetails.getUsername();
    }

    protected List<String> getUserRoles() {
        UserDetails userDetails = getUserDetails();
        if (userDetails.getAuthorities() != null &&
                !userDetails.getAuthorities().isEmpty()) {

            return userDetails
                    .getAuthorities()
                    .stream()
                    .map(a -> a.getAuthority())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    protected boolean isAdmin() {
        return getUserRoles().stream().anyMatch(a -> a.equalsIgnoreCase("ADMIN"));
    }

}
