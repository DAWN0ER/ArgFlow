package com.dawnyang.argflow.utils;

import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.TaskStatusEnum;
import com.dawnyang.argflow.domain.task.AbortedTaskInfo;
import com.dawnyang.argflow.domain.task.WaitTaskInfo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/07/22:21
 */
public class TaskResultCaster {

    public static AbortedTaskInfo getIfAborted(StatusResult<?> result){
        if (
                TaskStatusEnum.FAIL.getCode().equals(result.getStatus())
                || TaskStatusEnum.EXCEPTION.getCode().equals(result.getStatus())
                || TaskStatusEnum.UNEXPECTED_STATUS.getCode().equals(result.getStatus())
        ){
            return (AbortedTaskInfo) result.getData();
        }
        return null;
    }

    public static WaitTaskInfo getIfWait(StatusResult<?> result){
        if (TaskStatusEnum.WAIT.getCode().equals(result.getStatus())){
            return (WaitTaskInfo) result.getData();
        }
        return null;
    }

    public static <T> T getIfFinished(StatusResult<T> result){
        if (TaskStatusEnum.FINISHED.getCode().equals(result.getStatus())){
            return result.getData();
        }
        return null;
    }
}
