package com.dawnyang.argflow.action;

import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.exception.TaskException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/10/07/21:04
 */
public interface FlowEngine {

    <T> StatusResult<T> execute(String strategyName, Object input) throws TaskException;

    <T> StatusResult<T> awakeTask(Long taskId, String strategyName, Object input) throws TaskException;

}
