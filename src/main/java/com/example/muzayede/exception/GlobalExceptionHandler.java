package com.example.muzayede.exception;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse>
    handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req)
    {
        ErrorResponse resp = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource not found!",
                ex.getMessage(),
                req.getRequestURI()
        );

        return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficentBalanceException.class)
    public ResponseEntity<ErrorResponse>
    handleInsufficentBalance(InsufficentBalanceException ex, HttpServletRequest req)
    {
        ErrorResponse resp = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Insufficent Balance!",
                ex.getMessage(),
                req.getRequestURI()
        );
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse>
    handleMethodArgNotValid(MethodArgumentNotValidException ex, HttpServletRequest req)
    {

        StringBuilder errorMessage = new StringBuilder("Validation Error: ");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage.append("[").append(error.getField()).append(": ").append(error.getDefaultMessage()).append("] ")
        );

        ErrorResponse err = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errorMessage.toString(),
                req.getRequestURI()
        );
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse>
    handleGeneralExceptions(RuntimeException ex, HttpServletRequest req)
    {
        ErrorResponse resp = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Some kind of error occured! why?",
                ex.getMessage(),
                req.getRequestURI()
        );
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
}
