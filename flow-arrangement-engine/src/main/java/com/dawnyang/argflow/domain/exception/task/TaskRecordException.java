package com.dawnyang.argflow.domain.exception.task;

import com.dawnyang.argflow.domain.exception.TaskException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/04/21:02
 */
public class TaskRecordException extends TaskException {

    public TaskRecordException(Long taskId, String strategy, Reason reason) {
        super(getMsg(taskId, strategy, reason));
    }

    public TaskRecordException(Long taskId, String strategy, Throwable cause) {
        super(getMsg(taskId, strategy, Reason.UNKNOWN), cause);
    }

    public enum Reason {
        RECORD_ERROR,
        GET_ERROR,
        CORRUPTED,
        UNKNOWN
    }

    private static String getMsg(Long taskId, String strategy, Reason reason) {
        switch (reason) {
            case GET_ERROR:
                return "Fail to get task fail at taskId=\"" + taskId + "\", strategy:\"" + strategy + "\"";
            case RECORD_ERROR:
                return "Fail to record task at taskId:\"" + taskId + "\", strategy:\"" + strategy + "\"";
            case CORRUPTED:
                return "Record was corrupted at taskId:\"" + taskId + "\", strategy:\"" + strategy + "\"";
        }
        return "Unknown error occurred at taskId:\"" + taskId + "\", strategy:\"" + strategy + "\"";
    }
}
