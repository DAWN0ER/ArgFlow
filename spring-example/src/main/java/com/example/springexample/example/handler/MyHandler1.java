package com.example.springexample.example.handler;

import com.dawnyang.argflow.action.FlowHandler;
import com.dawnyang.argflow.domain.base.StatusResult;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MyHandler1 implements FlowHandler<String,String> {
    @Override
    public StatusResult<String> handler(String s) {
        return null;
    }

    @Override
    public Set<Integer> supportCustomStatus() {
        return null;
    }
}
