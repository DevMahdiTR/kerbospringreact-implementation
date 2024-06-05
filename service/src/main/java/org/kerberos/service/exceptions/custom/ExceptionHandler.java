package org.kerberos.service.exceptions.custom;


import org.kerberos.service.exceptions.utility.ApiError;
import org.kerberos.service.exceptions.utility.ResponseEntityBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class ExceptionHandler extends RuntimeException {

    public ExceptionHandler(String message) {
        super(message);
    }

    public ResponseEntity<Object> handle(HttpStatus status, String message, String errorMessage) {
        List<String> details = new ArrayList<>();
        details.add(errorMessage);

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(message)
                .errors(details)
                .build();
        return ResponseEntityBuilder.build(apiError);
    }

}
