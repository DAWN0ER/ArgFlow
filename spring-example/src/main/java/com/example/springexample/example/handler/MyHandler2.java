package com.example.springexample.example.handler;

import com.dawnyang.argflow.api.EnableWait;
import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import com.dawnyang.argflow.domain.base.StatusResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class MyHandler2 implements FlowHandler<String,String>, EnableWait<String,String> {
    @Override
    public StatusResult<String> handler(String s) {
        String k = Objects.isNull(s) ? "" : s;
        return new StatusResult<>(BaseHandlerStatusEnum.FAIL.getStatus(),k+"-myHandler2");
    }

    @Override
    public Set<Integer> customStatus() {
        return null;
    }

    @Override
    public StatusResult<String> waitFor(String input) {
        System.out.println("Awake!!!" + input);
        return null;
    }
}
