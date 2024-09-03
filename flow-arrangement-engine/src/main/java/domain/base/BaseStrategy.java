package domain.base;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
public abstract class BaseStrategy {

    protected ArrayList<StrategyNode> nodeArrangement;

    abstract public String[] getHandlerNameArrangement();
    abstract public Map<String, Map<Integer,String>> getSwitchers();
}
