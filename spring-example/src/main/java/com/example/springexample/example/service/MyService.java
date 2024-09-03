package com.example.springexample.example.service;

import com.dawnyang.argflow.action.FlowActionEngine;
import com.dawnyang.argflow.domain.base.StatusResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
@Service
public class MyService implements InitializingBean {

    @Resource
    private FlowActionEngine engine;

    @Override
    public void afterPropertiesSet() throws Exception {
        engine.tell("myStrategy1");
        StatusResult result = engine.execute("myStrategy1", "[TEST]");
        ArrayList data = (ArrayList) result.getData();
        System.out.println(data);
    }
}
