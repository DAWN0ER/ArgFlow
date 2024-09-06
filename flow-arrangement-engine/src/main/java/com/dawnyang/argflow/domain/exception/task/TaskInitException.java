package com.dawnyang.argflow.domain.exception.task;

import com.dawnyang.argflow.domain.exception.TaskException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/04/20:26
 */
public class TaskInitException extends TaskException {

    public TaskInitException(String strategyName){
        super("Not found strategy bean id = \"" + strategyName + "\"");
    }
}
