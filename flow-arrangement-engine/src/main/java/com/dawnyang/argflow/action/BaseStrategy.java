package com.dawnyang.argflow.action;

import com.dawnyang.argflow.domain.base.StrategyNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
@Getter
@Setter
public abstract class BaseStrategy {

    protected ArrayList<StrategyNode> nodeArrangement;

    abstract public String[] getHandlerNameArrangement();

    abstract public Map<String, Map<Integer, String>> getSwitchers();
}
