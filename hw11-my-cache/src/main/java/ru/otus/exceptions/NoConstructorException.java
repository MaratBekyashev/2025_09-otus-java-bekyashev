package ru.otus.exceptions;

public class NoConstructorException extends RuntimeException {

    public NoConstructorException(String message) {
        super(message);
    }

    public NoConstructorException(Exception ex) {
        super(ex);
    }
}
