package com.example.springexample.example.handler;

import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import com.dawnyang.argflow.domain.base.StatusResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class MyHandler2 implements FlowHandler<String,String> {
    @Override
    public StatusResult<String> handler(String s) {
        String k = Objects.isNull(s) ? "" : s;
        return new StatusResult<>(BaseHandlerStatusEnum.NEXT.getStatus(),k+"-myHandler2");
    }

    @Override
    public Set<Integer> customStatus() {
        return null;
    }
}
