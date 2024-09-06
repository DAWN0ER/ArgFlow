package com.dawnyang.argflow.domain.exception.task;

import com.dawnyang.argflow.domain.exception.TaskException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/06/12:09
 */
public class TaskWaitException extends TaskException {

    public TaskWaitException(String handler, Reason reason) {
        super(getMsg(handler, reason));
    }

    private static String getMsg(String handler, Reason reason){
        String msg = "UNKNOWN";
        switch (reason){
            case NOT_SUPPORT:
                msg = "Handler:\"" + handler + "\" is not EnableWait Can`t output status of WAIT(=2)";
                break;
            case WAIT_TWICE:
                msg = "Handler:\"" + handler + "\" can`t output status:WAIT(=2) in waitFor method";
                break;
        }
        return msg;
    }

    public enum Reason{
        NOT_SUPPORT,
        WAIT_TWICE
    }
}
