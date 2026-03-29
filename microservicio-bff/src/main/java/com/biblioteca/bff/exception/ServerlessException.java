package com.biblioteca.bff.exception;

public class ServerlessException extends RuntimeException {
    public ServerlessException(String message) {
        super(message);
    }

    public ServerlessException(String message, Throwable cause) {
        super(message, cause);
    }
}
