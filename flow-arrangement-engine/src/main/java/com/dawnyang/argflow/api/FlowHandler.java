package com.dawnyang.argflow.api;

import com.dawnyang.argflow.domain.base.StatusResult;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
public interface FlowHandler<INPUT, OUTPUT> {

    StatusResult<OUTPUT> handler(INPUT input);

    Set<Integer> customStatus();

}
