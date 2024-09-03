package com.dawnyang.argflow.domain.task;

import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.utils.MistUidGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
@Getter
@NoArgsConstructor
public class TaskInfoDto {

    private Long taskId;
    private Integer taskStatus;
    private String strategyName;
    private Map<String, StatusResult> resultMap;
    private Integer currentNode;

    public TaskInfoDto(String strategyName){
        this.taskId = MistUidGenerator.getUid();
        this.taskStatus = TaskStatusEnum.INIT.getCode();
        this.strategyName = strategyName;
        this.resultMap = new HashMap<>();
    }

    public void setCurrentNode(Integer currentNode) {
        this.currentNode = currentNode;
    }
}
