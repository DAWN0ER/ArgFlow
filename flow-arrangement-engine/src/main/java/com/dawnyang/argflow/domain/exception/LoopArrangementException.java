package com.dawnyang.argflow.domain.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
public class LoopArrangementException extends RuntimeException{

    private static final String MSG = "Handler \"%s\"'s target handler \"%s\"'s arrangement sequence is before it.";

    public LoopArrangementException(String name, String target){
        super(String.format(MSG,name,target));
    }
}
