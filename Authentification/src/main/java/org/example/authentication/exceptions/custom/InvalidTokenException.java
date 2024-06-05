package org.example.authentication.exceptions.custom;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends ExceptionHandler {
    public InvalidTokenException(String message) {
        super(message);
    }
}
