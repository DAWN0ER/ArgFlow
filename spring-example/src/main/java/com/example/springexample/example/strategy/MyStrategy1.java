package com.example.springexample.example.strategy;

import com.dawnyang.argflow.action.BaseStrategy;
import com.dawnyang.argflow.utils.SwitcherBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MyStrategy1 extends BaseStrategy {

    @Override
    public String[] handlerNameArrangement() {
        return new String[]{
                "myHandler1",
                "myHandler2"
        };
    }

    @Override
    public Map<String, Map<Integer, String>> getSwitchers() {
        return SwitcherBuilder.newBuilder()
                .addSwitcher("myHandler1",1,"myHandler2")
                .addSwitcher("myHandler1",4,"myHandler2")
                .addSwitcher("myHandler1",9,"myHandler2")
                .build();
    }
}
