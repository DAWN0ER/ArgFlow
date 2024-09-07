package com.example.springexample.example.strategy;

import com.dawnyang.argflow.api.BaseStrategy;
import com.dawnyang.argflow.api.TaskDurable;
import com.dawnyang.argflow.domain.base.NameSwitchers;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import com.dawnyang.argflow.domain.task.TaskInfoDto;
import com.dawnyang.argflow.utils.SwitcherBuilder;
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
                "baseOutHandler",
                "customOutHandler"
        };
    }

    @Override
    public NameSwitchers getSwitchers() {
        return SwitcherBuilder.newBuilder()
                .addSwitcher("baseOutHandler", BaseHandlerStatusEnum.FAIL.getStatus(), NameSwitchers.END_FLOW)
                .addSwitcher("baseOutHandler", BaseHandlerStatusEnum.EXCEPTION.getStatus(), "customOutHandler")
                .build();
    }

    @Override
    public JsonObject integrateResult(Map<String, StatusResult> resultMap, String endHandler) {
        System.out.println("integrateResult!!2");
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
