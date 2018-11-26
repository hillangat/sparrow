package com.techmaster.sparrow.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    protected Authentication getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    protected String getUserName() {
        Authentication authentication = getUserDetails();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
    }

    protected List<String> getUserRoles() {
        Authentication userDetails = getUserDetails();
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
