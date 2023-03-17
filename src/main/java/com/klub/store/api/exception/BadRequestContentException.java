package com.klub.store.api.exception;

public class BadRequestContentException extends Exception {

    public BadRequestContentException() {
        super();
    }

    public BadRequestContentException(String message) {
        super(message);
    }
}
