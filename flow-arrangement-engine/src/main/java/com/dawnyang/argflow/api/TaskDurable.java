package com.dawnyang.argflow.api;

import com.dawnyang.argflow.domain.task.TaskInfoDto;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
public interface TaskDurable {

    boolean recordTaskInfo(TaskInfoDto taskInfo);

    TaskInfoDto getTaskInfo(Long taskId);
}
