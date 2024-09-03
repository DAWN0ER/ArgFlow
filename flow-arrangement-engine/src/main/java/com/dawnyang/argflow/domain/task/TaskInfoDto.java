package com.dawnyang.argflow.domain.task;

import com.dawnyang.argflow.domain.base.BaseStatus;
import com.dawnyang.argflow.domain.base.StatusResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class TaskInfoDto {

    private Long taskId;
    private BaseStatus taskStatus;
    private String strategyName;
    private Map<String, StatusResult> outputRecord;
    private Integer currentNode;

}
