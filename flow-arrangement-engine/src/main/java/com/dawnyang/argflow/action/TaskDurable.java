package com.dawnyang.argflow.action;

import com.dawnyang.argflow.domain.task.TaskInfoDto;

public interface TaskDurable {

    void recordTaskInfo(TaskInfoDto taskInfo);

    TaskInfoDto getTaskInfo(Long taskId);
}
