package com.dawnyang.argflow.domain.exception;

public class NoHandlerException extends RuntimeException {

    private static final String FOR_ONE = "Handler bean: \"%s\" not Found, please check if the name is correct or the bean init successfully";
    private static final String FOR_ALL = "Not Found any Handler, please check if the name is correct or the bean init successfully";

    public NoHandlerException(String name) {
        super(String.format(FOR_ONE, name));
    }

    public NoHandlerException() {
        super(FOR_ALL);
    }

}
