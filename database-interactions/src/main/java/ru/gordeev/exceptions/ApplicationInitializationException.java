package ru.gordeev.exceptions;

public class ApplicationInitializationException extends RuntimeException {

    public ApplicationInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
