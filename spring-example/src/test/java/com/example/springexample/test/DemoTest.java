package com.example.springexample.test;

import com.dawnyang.argflow.action.FlowEngine;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.task.AbortedTaskInfo;
import com.dawnyang.argflow.domain.task.WaitTaskInfo;
import com.dawnyang.argflow.utils.TaskResultCaster;
import com.example.springexample.SpringExampleApplicationTests;
import com.example.springexample.example.domain.GoodsInfo;
import com.example.springexample.example.domain.GoodsOrderContext;
import com.google.gson.Gson;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/09/12:46
 */
public class DemoTest extends SpringExampleApplicationTests {

    @Resource
    FlowEngine engine;

    @Test
    public void testDemo() {
        GoodsOrderContext context = getExample();

        Gson gson = new Gson();
        String res = null;
        String demoStrategy = "demoStrategy";
        StatusResult<String> result = engine.execute(demoStrategy, context);

        do {
            System.out.println(gson.toJson(result));
            // 执行失败逻辑
            AbortedTaskInfo ifAborted = TaskResultCaster.getIfAborted(result);
            if (Objects.nonNull(ifAborted)) {
                System.out.println(gson.toJson(ifAborted));
                return;
            }
            // 中断处理逻辑
            WaitTaskInfo ifWait = TaskResultCaster.getIfWait(result);
            if (Objects.nonNull(ifWait)) {
                String handler = ifWait.getWaitHandler();
                switch (handler) {
                    case "userPayHandler":
                        context.setPayStatus(1); // 付款
                        System.out.println("付款订单号:" + ifWait.getHandlerResultData());
                        result = engine.awakeTask(ifWait.getTaskId(), demoStrategy, context);
                        break;
                    case "logisticsHandler":
                        context.setLogisticsStatus(1); // 发货
                        System.out.println("发货:"+ ifWait.getHandlerResultData());
                        result = engine.awakeTask(ifWait.getTaskId(), demoStrategy, context);
                        break;
                    default:
                        System.out.println(gson.toJson(ifWait));
                        return;
                }
            }
            // 结束拿到结果
            res = TaskResultCaster.getIfFinished(result);
        } while (Objects.isNull(res));
        System.out.println(res + "  !!!");
    }

    private GoodsOrderContext getExample(){
        GoodsOrderContext context = new GoodsOrderContext();
        context.setUserId(17);
        context.setUserName("User");
        context.setFingerprint("login");
        context.setAddress("address-B221");
        context.setCouponId(999);
        context.setShopId(1);

        GoodsInfo goods = new GoodsInfo();
        goods.setGoodsId(12211L);
        goods.setGoodsName("KFC");
        goods.setDescription("desc");
        goods.setNum(12);
        goods.setPrice(new BigDecimal("19.9"));
        context.setGoodsInfoList(Collections.singletonList(goods));
        context.setShopSnapshot("snapshot");
        return context;
    }
}
