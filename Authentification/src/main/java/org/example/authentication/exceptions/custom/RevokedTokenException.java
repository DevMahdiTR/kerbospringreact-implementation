package org.example.authentication.exceptions.custom;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RevokedTokenException extends ExceptionHandler {
    public RevokedTokenException(String message) {
        super(message);
    }
}
