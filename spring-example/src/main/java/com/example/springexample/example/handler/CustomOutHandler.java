package com.example.springexample.example.handler;

import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomOutHandler implements FlowHandler<String, String> {
    @Override
    public StatusResult<String> handler(String input) {
        System.out.println("CustomOutHandler(H?Enum)=" + input);
        int res = BaseHandlerStatusEnum.NEXT.getStatus();
        try {
            Status status = Status.valueOf(input);
            res = status.code;
        } catch (IllegalArgumentException e) {
            return new StatusResult<>(res, "CustomOutHandler="+input);
        }
        return new StatusResult<>(res, "CustomOutHandler="+input);
    }

    @Override
    public Set<Integer> customStatus() {
        return Arrays.stream(Status.values()).map(e->e.code).collect(Collectors.toSet());
    }

    public enum Status{
        H2(4),
        H3(9),
        HE(18),
        ;

        public final int code;

        Status(int code){
            this.code = code;
        }
    }
}
