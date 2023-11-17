package com.assignment.beam.exceptions;

public class PhoneAlreadyAvailableException extends RuntimeException {
    public PhoneAlreadyAvailableException(String message) {
        super(message);
    }
}
