package com.sahibinden.exceptions;

public class NotFoundException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Kayıt bulunamadi";
    public NotFoundException() {
        super(DEFAULT_MESSAGE);
    }
    public NotFoundException(String message) {
        super(message);
    }
}