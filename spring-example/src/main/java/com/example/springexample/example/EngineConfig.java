package com.example.springexample.example;

import com.dawnyang.argflow.action.FlowActionEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:34
 */
@Configuration
public class EngineConfig {

    @Bean
    public FlowActionEngine flowActionEngine(){
        FlowActionEngine engine = new FlowActionEngine();
        engine.initCacheCapacity(20);
        return engine;
    }
}