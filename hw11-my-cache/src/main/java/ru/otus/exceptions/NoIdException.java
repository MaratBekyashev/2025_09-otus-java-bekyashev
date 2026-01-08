package ru.otus.exceptions;

public class NoIdException extends RuntimeException {

    public NoIdException(String message) {
        super(message);
    }

    public NoIdException(Exception ex) {
        super(ex);
    }
}
