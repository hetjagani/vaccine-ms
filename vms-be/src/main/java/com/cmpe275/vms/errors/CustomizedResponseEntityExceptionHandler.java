package com.cmpe275.vms.errors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@ControllerAdvice
@RestController
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity handleResponseException(Exception ex) {
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

}
