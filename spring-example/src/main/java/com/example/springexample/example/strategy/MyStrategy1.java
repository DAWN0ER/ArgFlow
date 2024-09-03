package com.example.springexample.example.strategy;

import com.dawnyang.argflow.action.BaseStrategy;

import java.util.Map;

public class MyStrategy1 extends BaseStrategy {
    @Override
    public String[] getHandlerNameArrangement() {
        return new String[]{

        };
    }

    @Override
    public Map<String, Map<Integer, String>> getSwitchers() {
        return null;
    }
}
