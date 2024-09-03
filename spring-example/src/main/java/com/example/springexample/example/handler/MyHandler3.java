package com.example.springexample.example.handler;

import com.dawnyang.argflow.action.FlowHandler;
import com.dawnyang.argflow.domain.base.BaseStatus;
import com.dawnyang.argflow.domain.base.StatusResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
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
    public StatusResult<String> handler(String s) {
        String k = Objects.isNull(s) ? "" : s;
        return new StatusResult<>(BaseStatus.SUCCESS.getStatus(),k+"-myHandler333");
    }

    @Override
    public Set<Integer> customStatus() {
        return null;
    }
}
