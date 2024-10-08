package com.dawnyang.argflow.api;

import com.dawnyang.argflow.domain.base.StatusResult;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/06/12:05
 */
public interface EnableWait<CONTEXT, OUTPUT> {

    StatusResult<OUTPUT> waitFor(CONTEXT context);

}
