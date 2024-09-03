package com.dawnyang.argflow.domain.task;

import com.dawnyang.argflow.domain.base.BaseStatus;
import com.dawnyang.argflow.domain.base.StatusResult;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private BaseStatus taskStatus;
    private String strategyName;
    private Map<String, StatusResult> outputRecord;
    private Integer currentNode;

}
