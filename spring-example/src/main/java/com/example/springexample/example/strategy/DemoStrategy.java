package com.example.springexample.example.strategy;

import com.dawnyang.argflow.api.BaseStrategy;
import com.dawnyang.argflow.domain.base.NameSwitchers;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.utils.SwitcherBuilder;
import com.example.springexample.example.handler.*;
import com.google.common.base.CaseFormat;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/09/12:29
 */
@Service
public class DemoStrategy extends BaseStrategy {
    @Override
    public String[] handlerNameArrangement() {
        return new String[]{
                toName(LoginVerifyHandler.class),
                toName(BaseVerifyHandler.class),
                toName(ShopVerifyHandler.class),
                toName(GetWarehouseInfoHandler.class),
                toName(CheckPrivateStockHandler.class),
                toName(CheckPlatformStockHandler.class),
                toName(CreatePayInfoHandler.class),
                toName(UserPayHandler.class),
                toName(LogisticsHandler.class)
        };
    }

    @Override
    public NameSwitchers getSwitchers() {
        return SwitcherBuilder.newBuilder()
                .addSwitcher(toName(GetWarehouseInfoHandler.class),
                        GetWarehouseInfoHandler.Status.PRIVATE.getCode(),
                        toName(CheckPrivateStockHandler.class))
                .addSwitcher(toName(GetWarehouseInfoHandler.class),
                        GetWarehouseInfoHandler.Status.PLATFORM.code,
                        toName(CheckPlatformStockHandler.class))
                .addSwitcher(toName(CheckPrivateStockHandler.class),
                        99,
                        toName(CreatePayInfoHandler.class))
                .addSwitcher(toName(CheckPlatformStockHandler.class),
                        99,
                        toName(CreatePayInfoHandler.class))
                .build();
    }

    @Override
    public String integrateResult(Map<String, StatusResult> resultMap, String endHandler) {
        System.out.println("=========================");
        System.out.println("endHandler=" + endHandler);
        String[] strings = handlerNameArrangement();
        for (String name : strings) {
            StatusResult result = resultMap.get(name);
            if (Objects.isNull(result)) {
                continue;
            }
            System.out.println(name + ":=:" + result.getStatus() + ", res:" + result.getData());
        }
        System.out.println("=========================");
        return "integrateResult!";
    }

    private String toName(Class<?> clazz) {
        String input = clazz.getSimpleName();
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, input);
    }
}
