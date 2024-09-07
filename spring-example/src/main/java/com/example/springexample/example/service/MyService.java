package com.example.springexample.example.service;

import com.dawnyang.argflow.action.FlowActionEngine;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.TaskStatusEnum;
import com.dawnyang.argflow.domain.task.UnnaturalEndTaskInfo;
import com.google.gson.Gson;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
//        engine.tell("myStrategy1");
        StatusResult<List<String>> result = engine.execute("myStrategy1", "[TEST]");
        if (Objects.equals(TaskStatusEnum.EXCEPTION.getCode(),result.getStatus())){
            UnnaturalEndTaskInfo data = (UnnaturalEndTaskInfo) result.getData();
            System.out.println(new Gson().toJson(data));
        }
        System.out.println(new Gson().toJson(result));
    }
}
