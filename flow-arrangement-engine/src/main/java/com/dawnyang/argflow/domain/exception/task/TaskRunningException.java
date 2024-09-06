package com.dawnyang.argflow.domain.exception.task;

import com.dawnyang.argflow.domain.exception.TaskException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/06/12:56
 */
public class TaskRunningException extends TaskException {

    public TaskRunningException(String strategyName, Throwable cause) {
        super("Exception when running task of strategy:" + strategyName, cause);
    }
}
