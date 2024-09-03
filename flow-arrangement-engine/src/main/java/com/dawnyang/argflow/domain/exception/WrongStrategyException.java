package com.dawnyang.argflow.domain.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
public class WrongStrategyException extends StrategyException{

    private static final String MSG_F = "Fail to init strategy named \"%s\".";

    public WrongStrategyException(String name, Throwable cause){
        super(String.format(MSG_F,name),cause);
    }

}
