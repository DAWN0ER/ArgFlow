package com.dawnyang.argflow.action;

import com.dawnyang.argflow.domain.base.StatusResult;

import java.util.Set;

public interface FlowHandler<INPUT,OUTPUT> {

    StatusResult<OUTPUT> handler(INPUT input);
    Set<Integer> supportCustomStatus();

}
