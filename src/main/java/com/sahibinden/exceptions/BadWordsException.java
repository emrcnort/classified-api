package com.sahibinden.exceptions;

public class BadWordsException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Ilan basligi veya aciklamasi kotu kelimeler iceriyor.";
    public BadWordsException() {
        super(DEFAULT_MESSAGE);
    }
    public BadWordsException(String message) {
        super(message);
    }
}