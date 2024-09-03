package com.dawnyang.argflow.domain.exception;

public class WrongStrategyException extends RuntimeException{

    private static final String MSG_F = "Something wrong in strategy named \"%s\".";

    public WrongStrategyException(String name, Throwable cause){
        super(String.format(MSG_F,name),cause);
    }

}
