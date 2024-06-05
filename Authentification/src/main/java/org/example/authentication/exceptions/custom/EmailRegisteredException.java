package org.example.authentication.exceptions.custom;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailRegisteredException extends ExceptionHandler {
    public EmailRegisteredException(String message) {
        super(message);
    }

}
