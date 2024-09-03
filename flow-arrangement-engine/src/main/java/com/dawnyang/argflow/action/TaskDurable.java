package com.dawnyang.argflow.action;

import com.dawnyang.argflow.domain.task.TaskInfoDto;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
public interface TaskDurable {

    void recordTaskInfo(TaskInfoDto taskInfo);

    TaskInfoDto getTaskInfo(Long taskId);
}
