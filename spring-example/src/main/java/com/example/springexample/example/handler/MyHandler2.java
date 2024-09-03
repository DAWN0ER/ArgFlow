package com.example.springexample.example.handler;

import com.dawnyang.argflow.action.FlowHandler;
import com.dawnyang.argflow.domain.base.StatusResult;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MyHandler2 implements FlowHandler<String,String> {
    @Override
    public StatusResult<String> handler(String s) {
        return null;
    }

    @Override
    public Set<Integer> customStatus() {
        return null;
    }
}
