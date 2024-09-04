package com.dawnyang.argflow.domain.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/04/21:02
 */
public class TaskRecordException extends TaskException {

    public TaskRecordException(Long taskId, String strategy){
        super("Record task fail at taskId="+taskId + ", strategy=" + strategy);
    }
}
