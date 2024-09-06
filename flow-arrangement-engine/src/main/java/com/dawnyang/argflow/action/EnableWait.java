package com.dawnyang.argflow.action;

import com.dawnyang.argflow.domain.base.StatusResult;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/06/12:05
 */
public interface EnableWait<INPUT, OUTPUT> {

    StatusResult<OUTPUT> waitFor(INPUT input);

}
