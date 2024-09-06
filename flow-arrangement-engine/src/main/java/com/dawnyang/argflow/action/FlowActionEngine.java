package com.dawnyang.argflow.action;

import com.dawnyang.argflow.api.BaseStrategy;
import com.dawnyang.argflow.api.EnableWait;
import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.api.TaskDurable;
import com.dawnyang.argflow.domain.base.NameSwitchers;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.base.StrategyNode;
import com.dawnyang.argflow.domain.task.TaskWaitInfo;
import com.dawnyang.argflow.domain.exception.*;
import com.dawnyang.argflow.domain.exception.strategy.NoHandlerException;
import com.dawnyang.argflow.domain.exception.strategy.WrongStrategyException;
import com.dawnyang.argflow.domain.exception.task.TaskInitException;
import com.dawnyang.argflow.domain.exception.task.TaskRecordException;
import com.dawnyang.argflow.domain.exception.task.TaskRunningException;
import com.dawnyang.argflow.domain.exception.task.TaskWaitException;
import com.dawnyang.argflow.domain.task.TaskInfoDto;
import com.dawnyang.argflow.domain.enums.TaskStatusEnum;
import com.dawnyang.argflow.domain.task.UnnaturalEndTaskInfo;
import com.dawnyang.argflow.utils.StrategyArrangementBuilder;
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
        NameSwitchers switchers = strategy.getSwitchers();
        ArrayList<StrategyNode> nodeList = StrategyArrangementBuilder.newBuilder()
                .sequenceHandler(nameArrangement, handlerBeans)
                .initSwitchers(switchers)
                .build();
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
            throw new TaskInitException(strategyName);
        }
        TaskInfoDto taskInfo = new TaskInfoDto(strategyName);
        recordTask(taskInfo,strategy);

        ArrayList<StrategyNode> strategyNodeArrangement = strategy.getNodeArrangement();
        int order = 0;
        Object resultIntegration = null;
        taskInfo.setTaskStatus(TaskStatusEnum.RUNNING.getCode());
        try{
            while(order < strategyNodeArrangement.size()) {
                StrategyNode strategyNode = strategyNodeArrangement.get(order);
                taskInfo.setCurrentNode(order);

                StatusResult result = strategyNode.getHandler().handler(input);
                taskInfo.getResultMap().put(strategyNode.getName(),result);
                recordTask(taskInfo,strategy);

                if(BaseHandlerStatusEnum.WAIT.getStatus().equals(result.getStatus())){
                    if(strategyNode.getHandler() instanceof EnableWait){
                        TaskWaitInfo waitInfo = new TaskWaitInfo();
                        waitInfo.setTaskId(taskInfo.getTaskId());
                        String waitHandler = strategyNodeArrangement.get(taskInfo.getCurrentNode()).getName();
                        waitInfo.setWaitHandler(waitHandler);
                        waitInfo.setHandlerResultData(result.getData());
                        return new StatusResult<>(TaskStatusEnum.WAIT.getCode(), waitInfo);
                    } else {
                        throw new TaskWaitException(strategyNode.getName());
                    }
                }
                if(BaseHandlerStatusEnum.NEXT.getStatus().equals(result.getStatus())){
                    order++;
                    continue;
                }
                // switcher
                HashMap<Integer, Integer> switcher = strategyNode.getSwitcher();
                if(MapUtils.isNotEmpty(switcher)){
                    Integer target = switcher.get(result.getStatus());
                    if(Objects.equals(target,-1)){
                        break;
                    }
                    if(Objects.nonNull(target) && target > order){
                        order = target;
                        continue;
                    }
                }
                // 默认定义的特殊状态
                UnnaturalEndTaskInfo endTaskInfo = new UnnaturalEndTaskInfo();
                endTaskInfo.setTaskId(taskInfo.getTaskId());
                endTaskInfo.setResult(result);
                String endHandler = strategyNodeArrangement.get(taskInfo.getCurrentNode()).getName();
                endTaskInfo.setAbnormalHandler(endHandler);
                if(BaseHandlerStatusEnum.EXCEPTION.getStatus().equals(result.getStatus())){
                    taskInfo.setTaskStatus(TaskStatusEnum.EXCEPTION.getCode());
                    recordTask(taskInfo,strategy);
                    return new StatusResult<>(TaskStatusEnum.EXCEPTION.getCode(),endTaskInfo);
                }
                if(BaseHandlerStatusEnum.FAIL.getStatus().equals(result.getStatus())){
                    taskInfo.setTaskStatus(TaskStatusEnum.FAIL.getCode());
                    recordTask(taskInfo,strategy);
                    return new StatusResult<>(TaskStatusEnum.FAIL.getCode(), endTaskInfo);
                }
                taskInfo.setTaskStatus(TaskStatusEnum.UNEXPECTED_STATUS.getCode());
                recordTask(taskInfo,strategy);
                return new StatusResult<>(TaskStatusEnum.UNEXPECTED_STATUS.getCode(),endTaskInfo);
            }
            String endHandler = strategyNodeArrangement.get(taskInfo.getCurrentNode()).getName();
            resultIntegration = strategy.integrateResult(taskInfo.getResultMap(), endHandler);
        } catch (Exception e){
            throw new TaskRunningException(strategyName,e);
        }
        taskInfo.setTaskStatus(TaskStatusEnum.FINISHED.getCode());
        recordTask(taskInfo,strategy);
        return new StatusResult<>(TaskStatusEnum.FINISHED.getCode(), resultIntegration);
    }

    // TODO 这个应该做成工厂的
    public void initCacheCapacity(int capacity){
        if(MapUtils.isEmpty(lruCache)) {
            this.cacheCapacity = capacity;
        }
    }



    private void recordTask(TaskInfoDto taskInfo, BaseStrategy strategy) throws TaskRecordException {
        lruCache.record(taskInfo);
        if(strategy instanceof TaskDurable){
            if(!((TaskDurable) strategy).recordTaskInfo(taskInfo)) {
               throw new TaskRecordException(taskInfo.getTaskId(),taskInfo.getStrategyName());
            }
        }
    }
}
