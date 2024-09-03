package com.example.springexample.example.handler;

import com.dawnyang.argflow.action.FlowHandler;
import com.dawnyang.argflow.domain.base.StatusResult;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MyHandler1 implements FlowHandler<String, String> {
    @Override
    public StatusResult<String> handler(String s) {
        String k = Objects.isNull(s) ? "" : s;
        return new StatusResult<>(Status.CUS.code, k+"-myHandler1");
    }

    @Override
    public Set<Integer> customStatus() {
        return Arrays.stream(Status.values()).map(e->e.code).collect(Collectors.toSet());
    }

    public enum Status{
        CUS(4),
        COS(9)
        ;

        public final int code;

        Status(int code){
            this.code = code;
        }
    }
}
