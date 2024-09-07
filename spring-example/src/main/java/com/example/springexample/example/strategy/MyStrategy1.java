package com.example.springexample.example.strategy;

import com.dawnyang.argflow.api.BaseStrategy;
import com.dawnyang.argflow.domain.base.NameSwitchers;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.utils.SwitcherBuilder;
import com.example.springexample.example.handler.MyHandler1;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MyStrategy1 extends BaseStrategy {

    @Override
    public String[] handlerNameArrangement() {
        return new String[]{
                "myHandler1",
                "myHandler2",
                "myHandler3"
        };
    }

    @Override
    public NameSwitchers getSwitchers() {
        return SwitcherBuilder.newBuilder()
                .addSwitcher("myHandler1", MyHandler1.Status.COS.code, "myHandler3")
                .addSwitcher("myHandler1",MyHandler1.Status.CUS.code, "myHandler2")
                .build();
    }

    @Override
    public List<String> integrateResult(Map<String, StatusResult<?>> resultMap, String endHandler) {
        String[] order = handlerNameArrangement();
        List<String> arrayList = new ArrayList<>();
        for (String s : order) {
            if(Objects.isNull(resultMap.get(s))){
                continue;
            }
            String data = (String) resultMap.get(s).getData();
            arrayList.add(data);
        }
        return arrayList;
    }
}
