package com.klub.store.api.exception;

public class ForbiddenException extends Exception{

    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
