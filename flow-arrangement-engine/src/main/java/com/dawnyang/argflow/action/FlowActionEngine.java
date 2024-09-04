package com.dawnyang.argflow.action;

import com.dawnyang.argflow.domain.base.BaseStatus;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.base.StrategyNode;
import com.dawnyang.argflow.domain.exception.*;
import com.dawnyang.argflow.domain.task.TaskInfoDto;
import com.dawnyang.argflow.utils.StrategyNodesBuilder;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

    private int cacheCapacity = 100;
    private LRUCache lruCache;
    private ApplicationContext springContext;
    private Map<String, BaseStrategy> strategyMap;

    public void setApplicationContext(ApplicationContext applicationContext) {
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
        // 初始化 Cache
        this.lruCache = new LRUCache(cacheCapacity);
        // 初始化所有策略
        this.strategyMap = new ConcurrentHashMap<>();
        strategyBeanMap.forEach((name, strategy) -> {
            try {
                initStrategy(handlerBeans, strategy);
                this.strategyMap.put(name, strategy);
            } catch (StrategyException e) {
                new WrongStrategyException(name, e).printStackTrace();
            }
        });
    }

    private void initStrategy(Map<String, FlowHandler> handlerBeans, BaseStrategy strategy) throws StrategyException {
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

    public void tell(String name) {
        BaseStrategy baseStrategy = strategyMap.get(name);
        if (Objects.isNull(baseStrategy)) {
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

    public StatusResult execute(String strategyName, Object input) throws TaskException{
        BaseStrategy strategy = strategyMap.get(strategyName);
        if (Objects.isNull(strategy)) {
            throw new TaskInitException("Not found strategy bean id = \"" + strategyName + "\"");
        }
        TaskInfoDto taskInfo = new TaskInfoDto(strategyName);
        recordTask(taskInfo,strategy);

        ArrayList<StrategyNode> nodes = strategy.getNodeArrangement();
        int order = 0;
        while(order < nodes.size()) {
            taskInfo.setCurrentNode(order);
            StrategyNode strategyNode = nodes.get(order);

            StatusResult result = strategyNode.getHandler().handler(input);
            if(BaseStatus.EXCEPTION.getStatus().equals(result.getStatus())){
                // TODO 抛异常
            }
            if(BaseStatus.FAIL.getStatus().equals(result.getStatus())){
                // TODO 直接结束，返回TaskInfo ？
            }
            if(BaseStatus.WAIT.getStatus().equals(result.getStatus())){
                // TODO 返回TaskInfo ？
            }
            taskInfo.getResultMap().put(strategyNode.getName(),result);
            HashMap<Integer, Integer> switcher = strategyNode.getSwitcher();
            if(MapUtils.isNotEmpty(switcher)){
                Integer next = switcher.get(result.getStatus());
                if(Objects.nonNull(next)){
                    order = next;
                    continue;
                }
            }
            order++;
        }
        return new StatusResult<>(BaseStatus.SUCCESS.getStatus(), strategy.integrateResult(taskInfo.getResultMap()));
    }

    public void initCacheCapacity(int capacity){
        if(MapUtils.isEmpty(lruCache)) {
            this.cacheCapacity = capacity;
        }
    }

    private void recordTask(TaskInfoDto taskInfo, BaseStrategy strategy) throws TaskRecordException{
        lruCache.record(taskInfo);
        if(strategy instanceof TaskDurable){
            if(!((TaskDurable) strategy).recordTaskInfo(taskInfo)) {
               throw new TaskRecordException(taskInfo.getTaskId(),taskInfo.getStrategyName());
            }
        }
    }
}
