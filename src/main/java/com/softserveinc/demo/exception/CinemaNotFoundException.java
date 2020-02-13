package com.softserveinc.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CinemaNotFoundException extends ResponseStatusException {

    public CinemaNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
