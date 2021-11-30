package com.cmpe275.vms.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
@RestController
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity handleResponseException(Exception ex) {
        ex.printStackTrace();
        Error resp = new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<Error>(resp, resp.getStatus());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity handleResponseException(ResponseStatusException ex) {
        Error resp = new Error(ex.getStatus(), ex.getReason());
        return new ResponseEntity<Error>(resp, resp.getStatus());
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity handleEntityNotFoundException(NoSuchElementException ex) {
        Error resp = new Error(HttpStatus.NOT_FOUND, "requested resource not found");
        return new ResponseEntity<Error>(resp, resp.getStatus());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity handleUserAlreadyExistException(UserAlreadyExistsException ex) {
        Error resp = new Error(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<Error>(resp, resp.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity handleBadCredentialsException(BadCredentialsException ex) {
        Error resp = new Error(HttpStatus.FORBIDDEN, ex.getMessage());
        return new ResponseEntity<Error>(resp, resp.getStatus());
    }
}
