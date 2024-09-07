package com.dawnyang.argflow.domain.task;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/06/17:35
 */
@Data
public class WaitTaskInfo {

    private Long taskId;
    private String waitHandler;
    private Object handlerResultData;

}
