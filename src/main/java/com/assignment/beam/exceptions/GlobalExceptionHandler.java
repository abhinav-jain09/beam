package com.assignment.beam.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(PhoneNotAvailableException.class)
    public ResponseEntity<String> handlePhoneNotAvailableException(PhoneNotAvailableException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PhoneAlreadyAvailableException.class)
    public ResponseEntity<String> handlePhoneAlreadyAvailableException(PhoneAlreadyAvailableException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectReturnUserException.class)
    public ResponseEntity<Object> handleIncorrectReturnUserException(IncorrectReturnUserException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(PhoneNotFoundException.class)
    public ResponseEntity<String> handlePhoneNotFoundException(PhoneNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
