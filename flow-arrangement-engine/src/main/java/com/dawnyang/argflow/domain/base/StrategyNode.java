package com.dawnyang.argflow.domain.base;

import com.dawnyang.argflow.action.FlowHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
@Getter
@AllArgsConstructor
public class StrategyNode {

    private String name;
    private FlowHandler handler;
    private HashMap<Integer, Integer> switcher;

    public void setSwitcher(HashMap<Integer, Integer> switcher) {
        this.switcher = switcher;
    }
}
