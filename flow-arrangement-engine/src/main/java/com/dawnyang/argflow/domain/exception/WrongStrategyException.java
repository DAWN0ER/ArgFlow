package com.dawnyang.argflow.domain.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
public class WrongStrategyException extends RuntimeException{

    private static final String MSG_F = "Something wrong in strategy named \"%s\".";

    public WrongStrategyException(String name, Throwable cause){
        super(String.format(MSG_F,name),cause);
    }

}
