package com.dawnyang.argflow.domain.task;

import com.dawnyang.argflow.domain.base.StatusResult;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/06/18:58
 */
@Data
public class UnnaturalEndTaskInfo {

    private Long taskId;
    private StatusResult result;
    private String abnormalHandler;
}
