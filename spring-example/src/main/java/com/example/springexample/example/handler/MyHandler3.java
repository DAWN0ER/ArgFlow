package com.example.springexample.example.handler;

import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/22:55
 */
@Service
public class MyHandler3 implements FlowHandler<String,String> {

    @Override
    public StatusResult<String> handler(String input) {
        System.out.println("MyHandler3(Base)=" + input);
        int res = BaseHandlerStatusEnum.FAIL.getStatus();
        try {
            BaseHandlerStatusEnum anEnum = BaseHandlerStatusEnum.valueOf(input);
            res= anEnum.getStatus();
        } catch (IllegalArgumentException e) {
            return new StatusResult<>(res, "myHandler3="+input);
        }
        return new StatusResult<>(res, "myHandler3="+input);
    }

    @Override
    public Set<Integer> customStatus() {
        return null;
    }
}
