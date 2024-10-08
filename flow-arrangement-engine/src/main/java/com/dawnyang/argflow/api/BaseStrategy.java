package com.dawnyang.argflow.api;

import com.dawnyang.argflow.domain.base.NameSwitchers;
import com.dawnyang.argflow.domain.base.StatusResult;
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

    abstract public String[] handlerNameArrangement();

    abstract public NameSwitchers getSwitchers();

    abstract public Object integrateResult(Map<String, StatusResult> resultMap, String endHandler);
}
