package com.example.springexample.example.handler;

import com.dawnyang.argflow.api.EnableWait;
import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MyHandler2 implements FlowHandler<String, String>, EnableWait<String, String> {
    @Override
    public StatusResult<String> handler(String input) {
        System.out.println("MyHandler2(WAIT)" + input);
        return new StatusResult<>(BaseHandlerStatusEnum.WAIT.getStatus(), "myHandler2-[WAIT]:" + input);
    }

    @Override
    public Set<Integer> customStatus() {
        return null;
    }

    @Override
    public StatusResult<String> waitFor(String input) {
        System.out.println("myHandler2 Awake!!!=" + input);
        if ("WAIT".equals(input)) {
            return new StatusResult<>(BaseHandlerStatusEnum.WAIT.getStatus(), "myHandler2-WAIT TWICE:");
        }
        return new StatusResult<>(BaseHandlerStatusEnum.NEXT.getStatus(), "myHandler2-[WAIT]:AWAKE=" + input);
    }
}
