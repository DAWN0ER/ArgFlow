package com.dawnyang.argflow.action;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/07/20:28
 */
public class FlowEngineFactory {

    public static FlowActionEngine generate(){
        FlowActionEngine engine = new FlowActionEngine();
        return engine;
    }

    public static FlowActionEngine generate(int capacity){
        FlowActionEngine engine = new FlowActionEngine();
        engine.initCacheCapacity(capacity);
        return engine;
    }
}
