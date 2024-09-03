package com.dawnyang.argflow.domain.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
public class WrongSwitcherException extends RuntimeException {

    private static final String LOOP_F = "Handler \"%s\"'s target handler:\"%s\"'s arrangement sequence is before it.";
    private static final String WRONG_STATUS_F = "Some key not found in handler:\"%s\" support status set";

    public WrongSwitcherException(String name, String target, WrongType type) {
        super(getMsg(name,target,type));
    }

    private static String getMsg(String name, String target, WrongType type){
        switch (type){
            case LOOP:
                return String.format(LOOP_F,name,target);
            case WRONG_STATUS:
                return String.format(WRONG_STATUS_F,name);
        }
        return "Unknown Exception!";
    }

    public enum WrongType{
        LOOP,
        WRONG_STATUS
        ;
    }
}
