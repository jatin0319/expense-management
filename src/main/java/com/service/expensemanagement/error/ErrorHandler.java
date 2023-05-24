package com.service.expensemanagement.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> exception(Exception exception) {
        ErrorDto error = ErrorDto.builder()
                .message(exception.getMessage())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .timeStamp(String.valueOf(Instant.now().toEpochMilli()))
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
