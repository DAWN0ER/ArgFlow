package com.dawnyang.argflow.domain.task;

import com.dawnyang.argflow.api.UidGenerator;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.TaskStatusEnum;
import com.dawnyang.argflow.utils.MistUidGenerator;
import lombok.Data;
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
@Data
@NoArgsConstructor
public class TaskInfoDto {

    private Long taskId;
    private Integer taskStatus;
    private String strategyName;
    private Map<String, StatusResult> resultMap;
    private Integer currentNode;
    private Object resultIntegration;

    public TaskInfoDto(String strategyName){
        this(strategyName,MistUidGenerator.getInstance());
    }

    public TaskInfoDto(String strategyName, UidGenerator uidGenerator){
        this.taskId = uidGenerator.getUid();
        this.taskStatus = TaskStatusEnum.INIT.getCode();
        this.strategyName = strategyName;
        this.resultMap = new HashMap<>();
    }

}
