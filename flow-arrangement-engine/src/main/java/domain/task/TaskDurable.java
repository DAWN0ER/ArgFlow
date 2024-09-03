package domain.task;

public interface TaskDurable {

    void recordTaskInfo(TaskInfoDto taskInfo);
    TaskInfoDto getTaskInfo(Long taskId);
}
