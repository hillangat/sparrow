package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.entities.ErrorResponse;
import com.techmaster.sparrow.exception.SparrowRestfulApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseController {

    public static final String SUCCESS_RETRIEVAL_MSG = "Successfully retrieved the data";

    @ExceptionHandler(SparrowRestfulApiException.class)
    public final ResponseEntity<ErrorResponse> handleException(SparrowRestfulApiException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    protected String getUserName() {
        Authentication authentication = getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
    }

    protected List<String> getUserRoles() {
        Authentication userDetails = getAuthentication();
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