package com.example.springexample.example.strategy;

import com.dawnyang.argflow.api.BaseStrategy;
import com.dawnyang.argflow.api.TaskDurable;
import com.dawnyang.argflow.domain.base.NameSwitchers;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.task.TaskInfoDto;
import com.dawnyang.argflow.utils.SwitcherBuilder;
import com.example.springexample.example.handler.MyHandler1;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/06/18:06
 */
@Service
public class MyStrategy2 extends BaseStrategy implements TaskDurable {
    @Override
    public String[] handlerNameArrangement() {
        return new String[]{
                "myHandler1",
                "myHandler3",
                "myHandler2"
        };
    }

    @Override
    public NameSwitchers getSwitchers() {
        return SwitcherBuilder.newBuilder()
                .addSwitcher("myHandler1", MyHandler1.Status.CCC.code, NameSwitchers.END_FLOW)
                .addSwitcher("myHandler1", MyHandler1.Status.CUS.code, "myHandler3")
                .addSwitcher("myHandler1",MyHandler1.Status.COS.code, "myHandler2")
                .build();
    }

    @Override
    public JsonObject integrateResult(Map<String, StatusResult<?>> resultMap, String endHandler) {
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        jsonObject.add("map",gson.toJsonTree(resultMap));
        jsonObject.add("name",gson.toJsonTree(endHandler));
        return jsonObject;
    }

    @Override
    public boolean recordTaskInfo(TaskInfoDto taskInfo) {
        System.out.println("recordTaskInfo!:"+new Gson().toJson(taskInfo));
        return true;
    }

    @Override
    public TaskInfoDto getTaskInfo(Long taskId) {
        return null;
    }
}
