package com.example.springexample.test;

import com.dawnyang.argflow.action.FlowActionEngine;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.task.AbortedTaskInfo;
import com.dawnyang.argflow.domain.task.WaitTaskInfo;
import com.dawnyang.argflow.utils.TaskResultCaster;
import com.example.springexample.SpringExampleApplicationTests;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/07/20:41
 */
public class EngineTest extends SpringExampleApplicationTests {

    @Resource
    private FlowActionEngine engine;

    private Gson gson = new Gson();

    @Test
    public void strategy1Test() {
        String strategyName = "myStrategy1";
        StatusResult<List<String>> result = engine.execute(strategyName, "HE");
        AbortedTaskInfo ifAborted = TaskResultCaster.getIfAborted(result);
        List<String> ifFinished = TaskResultCaster.getIfFinished(result);
        System.out.println(ifAborted);
        System.out.println(ifFinished);

    }

    @Test
    public void strategy2Test() {
        String strategyName = "myStrategy2";
        StatusResult<JsonObject> result = engine.execute(strategyName, "NEXT");
        System.out.println("[Result]=" + gson.toJson(result));
        System.out.println(gson.toJson(TaskResultCaster.getIfFinished(result)));
        System.out.println(gson.toJson(TaskResultCaster.getIfWait(result)));
        System.out.println(gson.toJson(TaskResultCaster.getIfAborted(result)));

    }

    @Test
    public void castTest() {
        String strategyName = "myStrategy1";
        StatusResult<List<String>> result = engine.execute(strategyName, "CCC");
        System.out.println("[Result]=" + gson.toJson(result));
        System.out.println(gson.toJson(TaskResultCaster.getIfFinished(result)));
        System.out.println(gson.toJson(TaskResultCaster.getIfWait(result)));
        System.out.println(gson.toJson(TaskResultCaster.getIfAborted(result)));
        WaitTaskInfo waitTaskInfo = TaskResultCaster.getIfWait(result);
        if (waitTaskInfo != null){
            Long taskId = waitTaskInfo.getTaskId();
            result = engine.awakeTask(taskId, strategyName, "NEXT");
            System.out.println(gson.toJson(TaskResultCaster.getIfFinished(result)));
            System.out.println(gson.toJson(TaskResultCaster.getIfWait(result)));
            System.out.println(gson.toJson(TaskResultCaster.getIfAborted(result)));
        }

    }
}
