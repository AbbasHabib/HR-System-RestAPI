package com.spring.ExceptionsCustom;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> exceptionResponse(CustomException ce) {
        return new ResponseEntity<>(ce.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
