package com.dawnyang.argflow.domain.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/04/20:29
 */
public abstract class StrategyException extends RuntimeException{

    public StrategyException() {
        super();
    }

    public StrategyException(String message) {
        super(message);
    }

    public StrategyException(String message, Throwable cause) {
        super(message, cause);
    }

    public StrategyException(Throwable cause) {
        super(cause);
    }

    protected StrategyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
