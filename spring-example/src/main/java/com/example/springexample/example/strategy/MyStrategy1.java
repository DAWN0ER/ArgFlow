package com.example.springexample.example.strategy;

import com.dawnyang.argflow.api.BaseStrategy;
import com.dawnyang.argflow.domain.base.NameSwitchers;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.utils.SwitcherBuilder;
import com.example.springexample.example.handler.CustomOutHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MyStrategy1 extends BaseStrategy {

    @Override
    public String[] handlerNameArrangement() {
        return new String[]{
                "customOutHandler",
                "waitHandler",
                "baseOutHandler"
        };
    }

    @Override
    public NameSwitchers getSwitchers() {
        return SwitcherBuilder.newBuilder()
                .addSwitcher("customOutHandler", CustomOutHandler.Status.H3.code, "baseOutHandler")
                .addSwitcher("customOutHandler", CustomOutHandler.Status.H2.code, "waitHandler")
                .addSwitcher("customOutHandler", CustomOutHandler.Status.HE.code, NameSwitchers.END_FLOW)
//                .addSwitcher("waitHandler", BaseHandlerStatusEnum.WAIT.getStatus(), NameSwitchers.END_FLOW)
                .build();
    }

    @Override
    public List<String> integrateResult(Map<String, StatusResult> resultMap, String endHandler) {
        System.out.println("integrateResult!");
        String[] order = handlerNameArrangement();
        List<String> arrayList = new ArrayList<>();
        for (String s : order) {
            if (resultMap.containsKey(s)) {
                String data = (String) resultMap.get(s).getData();
                System.out.println("Res=[" + s + "=" + data + "]");
                arrayList.add(data);
            }
        }
        return arrayList;
    }
}
