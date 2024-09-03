package com.dawnyang.argflow.domain.base;

import com.dawnyang.argflow.action.FlowHandler;

import java.util.HashMap;

public class StrategyNode {

    private String name;
    private FlowHandler handler;
    private HashMap<Integer,Integer> switcher;
}
