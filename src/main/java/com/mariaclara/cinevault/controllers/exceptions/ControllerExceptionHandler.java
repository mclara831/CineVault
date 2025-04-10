package com.mariaclara.cinevault.controllers.exceptions;

import com.mariaclara.cinevault.services.exceptions.ResourceAlreadyExistsException;

import com.mariaclara.cinevault.services.exceptions.ResourceNotFoundException;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.security.authentication.BadCredentialsException;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandartError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new StandartError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<StandartError> resourceAlreadyExists(ResourceAlreadyExistsException e, HttpServletRequest request) {
        String error = "Resource already exists on the database";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(new StandartError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandartError> BadCredentials(BadCredentialsException e, HttpServletRequest request) {
        String error = e.getMessage();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(new StandartError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI()));
    }
}
