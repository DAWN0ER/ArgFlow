package com.dawnyang.argflow.action;

import com.dawnyang.argflow.domain.base.StrategyNode;
import com.dawnyang.argflow.domain.exception.NoHandlerException;
import com.dawnyang.argflow.domain.exception.WrongStrategyException;
import com.dawnyang.argflow.domain.exception.WrongSwitcherException;
import com.dawnyang.argflow.utils.StrategyNodesBuilder;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
@Slf4j
public class FlowActionEngine implements InitializingBean, ApplicationContextAware {

    private ApplicationContext springContext;
    private Map<String, BaseStrategy> strategyMap;

    public void setApplicationContext(ApplicationContext applicationContext) throws NoHandlerException {
        this.springContext = applicationContext;
    }

    public void afterPropertiesSet() {
        Map<String, FlowHandler> handlerBeans = springContext.getBeansOfType(FlowHandler.class, false, true);
        if (MapUtils.isEmpty(handlerBeans)) {
            new NoHandlerException().printStackTrace();
            return;
        }
        Map<String, BaseStrategy> strategyBeanMap = springContext.getBeansOfType(BaseStrategy.class, false, true);
        if (strategyBeanMap.isEmpty()) {
            this.strategyMap = Collections.emptyMap();
            return;
        }
        // 初始化所有策略
        this.strategyMap = new HashMap<>();
        strategyBeanMap.forEach((name, strategy) -> {
            try {
                initStrategy(handlerBeans, strategy);
                this.strategyMap.put(name, strategy);
            } catch (NoHandlerException | WrongSwitcherException e) {
                new WrongStrategyException(name, e).printStackTrace();
            }
        });
    }

    private void initStrategy(Map<String, FlowHandler> handlerBeans, BaseStrategy strategy) throws NoHandlerException, WrongSwitcherException {
        String[] nameArrangement = strategy.handlerNameArrangement();
        Map<String, Map<Integer, String>> switchers = strategy.getSwitchers();
        boolean isSequential = MapUtils.isEmpty(switchers);
        ArrayList<StrategyNode> nodeList;
        if (isSequential) {
            nodeList = StrategyNodesBuilder.newBuilder()
                    .sequenceHandler(nameArrangement, handlerBeans)
                    .build();
        } else {
            nodeList = StrategyNodesBuilder.newBuilder()
                    .sequenceHandler(nameArrangement, handlerBeans)
                    .initSwitchers(switchers)
                    .build();
        }
        strategy.setNodeArrangement(nodeList);
    }

    public void tell(String name){
        BaseStrategy baseStrategy = strategyMap.get(name);
        if(Objects.isNull(baseStrategy)){
            return;
        }
        ArrayList<StrategyNode> nodeArrangement = baseStrategy.getNodeArrangement();
        List<String> instanceList = nodeArrangement.stream().map(e -> e.getHandler().toString()).collect(Collectors.toList());
        log.info("Strategy details:\nhandlersNames={},\nswitchers={},\nhandlerInstance={},\nNodeJson={}",
                baseStrategy.handlerNameArrangement(),
                baseStrategy.getSwitchers(),
                instanceList,
                new Gson().toJson(nodeArrangement)
        );
    }
}
