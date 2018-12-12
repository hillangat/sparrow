package com.techmaster.sparrow.controllers;

import com.techmaster.sparrow.entities.misc.ErrorResponse;
import com.techmaster.sparrow.exception.SparrowRestfulApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class RestfulApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(SparrowRestfulApiException.class)
    public final ResponseEntity<ErrorResponse> handleUserNotFoundException(SparrowRestfulApiException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
