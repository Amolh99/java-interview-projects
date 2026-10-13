package com.example.exceptions;

/** Unchecked: the input is wrong, so retrying will not help. */
public class InvalidOrderException extends RuntimeException {

    public InvalidOrderException(String message) {
        super(message);
    }
}
