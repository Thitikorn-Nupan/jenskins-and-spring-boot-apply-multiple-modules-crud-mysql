package com.ttknp.api.exception;


public class ContentNotAllowed extends RuntimeException {

    public ContentNotAllowed(Exception exception) {
        super(exception.getMessage(), exception);
    }

}