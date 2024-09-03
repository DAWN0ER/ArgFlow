package com.dawnyang.argflow.domain.exception;

public class LoopArrangementException extends RuntimeException{

    private static final String MSG = "Handler \"%s\"'s target handler \"%s\"'s arrangement sequence is before it.";

    public LoopArrangementException(String name, String target){
        super(String.format(MSG,name,target));
    }
}
