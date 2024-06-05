package org.example.authentication.exceptions.custom;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ExpiredTokenException extends ExceptionHandler {
    public ExpiredTokenException(String msg) {
        super(msg);
    }

}

