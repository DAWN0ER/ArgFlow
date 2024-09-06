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
    public TaskWaitException(String handler) {
        super("Handler:\"" + handler + "\" is not EnableWait Can`t output status of WAIT(=2)");
    }
}
