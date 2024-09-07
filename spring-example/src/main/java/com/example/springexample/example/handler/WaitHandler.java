package com.example.springexample.example.handler;

import com.dawnyang.argflow.api.EnableWait;
import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class WaitHandler implements FlowHandler<String, String>, EnableWait<String, String> {
    @Override
    public StatusResult<String> handler(String input) {
        System.out.println("WaitHandler(WAIT)" + input);
        return new StatusResult<>(BaseHandlerStatusEnum.WAIT.getStatus(), "WaitHandler-[WAIT]:" + input);
    }

    @Override
    public Set<Integer> customStatus() {
        return null;
    }

    @Override
    public StatusResult<String> waitFor(String input) {
        System.out.println("WaitHandler Awake!!!=" + input);
        if ("WAIT".equals(input)) {
            return new StatusResult<>(BaseHandlerStatusEnum.WAIT.getStatus(), "WaitHandler-WAIT TWICE:");
        }
        return new StatusResult<>(BaseHandlerStatusEnum.NEXT.getStatus(), "WaitHandler-[WAIT]:AWAKE=" + input);
    }
}
