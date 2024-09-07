package com.example.springexample.test;

import com.dawnyang.argflow.action.FlowActionEngine;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.TaskStatusEnum;
import com.dawnyang.argflow.domain.task.TaskWaitInfo;
import com.dawnyang.argflow.domain.task.UnnaturalEndTaskInfo;
import com.example.springexample.SpringExampleApplicationTests;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
        StatusResult<List<String>> result = engine.execute(strategyName, "FAIL");
        System.out.println(gson.toJson(result));
        if (TaskStatusEnum.FAIL.getCode().equals(result.getStatus())) {
            UnnaturalEndTaskInfo taskInfo = (UnnaturalEndTaskInfo) result.getData();
            System.out.println("FAIL!!" + gson.toJson(taskInfo));
        }
        if (TaskStatusEnum.FINISHED.getCode().equals(result.getStatus())) {
            result.getData().forEach(System.out::println);
        }
        if (Objects.equals(TaskStatusEnum.WAIT.getCode(), result.getStatus())) {
            TaskWaitInfo waitInfo = (TaskWaitInfo) result.getData();
            System.out.println("WAIT!!" + gson.toJson(waitInfo));
            Long taskId = waitInfo.getTaskId();
            result = engine.awakeTask(taskId, strategyName, "EXCEPTION");
        }
        if (TaskStatusEnum.FINISHED.getCode().equals(result.getStatus())) {
            result.getData().forEach(System.out::println);
        }
        if (TaskStatusEnum.FAIL.getCode().equals(result.getStatus())) {
            UnnaturalEndTaskInfo taskInfo = (UnnaturalEndTaskInfo) result.getData();
            System.out.println("FAIL!!");
            System.out.println(taskInfo.getResult().getData());
            System.out.println(gson.toJson(taskInfo));
        }
        if (TaskStatusEnum.EXCEPTION.getCode().equals(result.getStatus())) {
            UnnaturalEndTaskInfo taskInfo = (UnnaturalEndTaskInfo) result.getData();
            System.out.println("EXCEPTION!!");
            System.out.println(taskInfo.getResult().getData());
            System.out.println(gson.toJson(taskInfo));
        }
        if (TaskStatusEnum.UNEXPECTED_STATUS.getCode().equals(result.getStatus())) {
            UnnaturalEndTaskInfo taskInfo = (UnnaturalEndTaskInfo) result.getData();
            System.out.println("UNEXPECTED_STATUS!!");
            System.out.println(taskInfo.getResult().getData());
            System.out.println(gson.toJson(taskInfo));
        }
    }

    @Test
    public void strategy2Test() {
        String strategyName = "myStrategy2";
        StatusResult<JsonObject> result = engine.execute(strategyName, "FAIL");
        System.out.println("[Result]=" + gson.toJson(result));
        if (TaskStatusEnum.FAIL.getCode().equals(result.getStatus())) {
            UnnaturalEndTaskInfo taskInfo = UnnaturalEndTaskInfo.class.cast(result.getData());
            System.out.println("FAIL!!" + gson.toJson(taskInfo));
        }
        if (TaskStatusEnum.FINISHED.getCode().equals(result.getStatus())) {
            System.out.println(gson.toJson(result));
            return;
        }
        if (Objects.equals(TaskStatusEnum.WAIT.getCode(), result.getStatus())) {
            TaskWaitInfo waitInfo = TaskWaitInfo.class.cast(result.getData());
            System.out.println("WAIT!!" + gson.toJson(waitInfo));
            Long taskId = waitInfo.getTaskId();
            result = engine.awakeTask(taskId, strategyName, "EXCEPTION");
        }
        if (TaskStatusEnum.FINISHED.getCode().equals(result.getStatus())) {
            System.out.println(gson.toJson(result));
            return;
        }
        if (TaskStatusEnum.FAIL.getCode().equals(result.getStatus())) {
            UnnaturalEndTaskInfo taskInfo = UnnaturalEndTaskInfo.class.cast(result.getData());
            System.out.println("FAIL!!");
            System.out.println(taskInfo.getResult().getData());
            System.out.println(gson.toJson(taskInfo));
        }
        if (TaskStatusEnum.EXCEPTION.getCode().equals(result.getStatus())) {
            UnnaturalEndTaskInfo taskInfo = UnnaturalEndTaskInfo.class.cast(result.getData());
            System.out.println("EXCEPTION!!");
            System.out.println(taskInfo.getResult().getData());
            System.out.println(gson.toJson(taskInfo));
        }
        if (TaskStatusEnum.UNEXPECTED_STATUS.getCode().equals(result.getStatus())) {
            UnnaturalEndTaskInfo taskInfo = UnnaturalEndTaskInfo.class.cast(result.getData());
            System.out.println("UNEXPECTED_STATUS!!");
            System.out.println(taskInfo.getResult().getData());
            System.out.println(gson.toJson(taskInfo));
        }
    }
}
