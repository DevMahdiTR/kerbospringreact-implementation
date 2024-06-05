package org.kerberos.tgs.exceptions;


import org.jetbrains.annotations.NotNull;
import org.kerberos.tgs.exceptions.custom.ResourceNotFoundException;
import org.kerberos.tgs.exceptions.custom.UnauthorizedActionException;
import org.kerberos.tgs.exceptions.utility.ApiError;
import org.kerberos.tgs.exceptions.utility.ResponseEntityBuilder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(@NotNull ResourceNotFoundException ex) {
        return ex.handle(HttpStatus.NOT_FOUND, "Resource not found.", ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    protected ResponseEntity<Object> handleUnauthorizedActionException(@NotNull UnauthorizedActionException ex) {
        return ex.handle(HttpStatus.UNAUTHORIZED, "The requested action is unauthorized. Access denied.", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(@NotNull IllegalArgumentException ex) {
        return handleException(HttpStatus.BAD_REQUEST, "Validation failed", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllException(@NotNull Exception ex, WebRequest request) {
        return handleException(HttpStatus.BAD_REQUEST, "Other exception occurs", ex.getMessage());
    }



    //Override Methode should have separate handling exception function , cannot change core code.
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NotNull HttpMessageNotReadableException ex, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
        return handleException(HttpStatus.BAD_REQUEST, "Malformed JSON found.", ex.getMessage());
    }


    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(@NotNull HttpMediaTypeNotSupportedException ex,  @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
        return handleException(HttpStatus.BAD_REQUEST, "Unsupported Media Type", ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(@NotNull NoHandlerFoundException ex, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, "Method not supported", ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex,  @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
        return handleException(
                HttpStatus.BAD_REQUEST,
                "Validation Errors",
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + " : " + error.getDefaultMessage())
                        .collect(Collectors.joining("; ")));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(@NotNull HttpRequestMethodNotSupportedException ex,  @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest request) {
        return handleException(HttpStatus.NOT_FOUND, "Method not supported", ex.getMessage());
    }


    private @NotNull ResponseEntity<Object> handleException(HttpStatus status, String message, String errorMessage) {
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