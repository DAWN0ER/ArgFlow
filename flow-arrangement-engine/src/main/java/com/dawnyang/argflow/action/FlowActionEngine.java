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

    public StatusResult execute(String strategyName, Object input) throws TaskException {
        BaseStrategy strategy = strategyMap.get(strategyName);
        if (Objects.isNull(strategy)) {
            throw new TaskInitException(strategyName);
        }
        TaskInfoDto taskInfo = new TaskInfoDto(strategyName);
        recordTask(taskInfo, strategy);
        taskInfo.setTaskStatus(TaskStatusEnum.RUNNING.getCode());
        try {
            return processSinceOrder(0, strategy, input, taskInfo);
        } catch (Exception e) {
            throw new TaskRunningException(strategyName, e);
        }
    }

    // TODO 这个应该做成工厂的
    public void initCacheCapacity(int capacity) {
        if (MapUtils.isEmpty(lruCache)) {
            this.cacheCapacity = capacity;
        }
    }

    // TODO 各种判NULL
    public StatusResult awakeTask(Long taskId, String strategyName, Object input) throws TaskException {
        BaseStrategy strategy = strategyMap.get(strategyName);
        if (Objects.isNull(strategy)) {
            throw new TaskInitException(strategyName);
        }
        TaskInfoDto taskInfo = getTaskInfo(taskId, strategy, strategyName);
        Integer currentNode = taskInfo.getCurrentNode();
        // TODO 检查记录是否完整匹配 包括 currentNode，taskId，taskStatus，resultMap

        StrategyNode strategyNode = strategy.getNodeArrangement().get(currentNode);
        // TODO Handler 是否实现了 wait 接口，如果没有，也是记录损坏异常（要么 current node 错了， 要么服务迭代老流程坏掉了）
        // TODO 流程灰度更新支持

        EnableWait handler = (EnableWait) strategyNode.getHandler();
        StatusResult result = handler.waitFor(input);

        taskInfo.setTaskStatus(TaskStatusEnum.RUNNING.getCode());
        taskInfo.getResultMap().put(strategyNode.getName(), result);
        recordTask(taskInfo, strategy);

        if (BaseHandlerStatusEnum.NEXT.getStatus().equals(result.getStatus())) {
            return processSinceOrder(currentNode + 1, strategy, input, taskInfo);
        }
        if (BaseHandlerStatusEnum.WAIT.getStatus().equals(result.getStatus())) {
            throw new TaskWaitException(strategyNode.getName(), TaskWaitException.Reason.WAIT_TWICE);
        }
        // 自定义 switcher
        HashMap<Integer, Integer> switcher = strategyNode.getSwitcher();
        if (MapUtils.isNotEmpty(switcher)) {
            Integer target = switcher.get(result.getStatus());
            if (Objects.equals(target, -1)) {
                Object resultIntegration = strategy.integrateResult(taskInfo.getResultMap(), strategyNode.getName());
                taskInfo.setTaskStatus(TaskStatusEnum.FINISHED.getCode());
                taskInfo.setResultIntegration(resultIntegration);
                recordTask(taskInfo, strategy);
                return new StatusResult<>(TaskStatusEnum.FINISHED.getCode(), resultIntegration);
            }
            if (Objects.nonNull(target) && target > currentNode) {
                return processSinceOrder(target, strategy, input, taskInfo);
            }
        }
        return processAbnormalResult(taskInfo, result, strategy);
    }

    private StatusResult processSinceOrder(int order, BaseStrategy strategy, Object input, TaskInfoDto taskInfo) throws TaskException {
        ArrayList<StrategyNode> strategyNodeArrangement = strategy.getNodeArrangement();
        while (order < strategyNodeArrangement.size()) {
            StrategyNode strategyNode = strategyNodeArrangement.get(order);
            taskInfo.setCurrentNode(order);
            StatusResult result = strategyNode.getHandler().handler(input);
            taskInfo.getResultMap().put(strategyNode.getName(), result);
            recordTask(taskInfo, strategy);

            // 默认编排
            if (BaseHandlerStatusEnum.WAIT.getStatus().equals(result.getStatus())) {
                if (strategyNode.getHandler() instanceof EnableWait) {
                    TaskWaitInfo waitInfo = new TaskWaitInfo();
                    waitInfo.setTaskId(taskInfo.getTaskId());
                    String waitHandler = strategyNodeArrangement.get(taskInfo.getCurrentNode()).getName();
                    waitInfo.setWaitHandler(waitHandler);
                    waitInfo.setHandlerResultData(result.getData());
                    return new StatusResult<>(TaskStatusEnum.WAIT.getCode(), waitInfo);
                } else {
                    throw new TaskWaitException(strategyNode.getName(), TaskWaitException.Reason.NOT_SUPPORT);
                }
            }
            if (BaseHandlerStatusEnum.NEXT.getStatus().equals(result.getStatus())) {
                order++;
                continue;
            }
            // 自定义 switcher
            HashMap<Integer, Integer> switcher = strategyNode.getSwitcher();
            if (MapUtils.isNotEmpty(switcher)) {
                Integer target = switcher.get(result.getStatus());
                if (Objects.equals(target, -1)) {
                    break;
                }
                if (Objects.nonNull(target) && target > order) {
                    order = target;
                    continue;
                }
            }
            // 默认定义的特殊状态
            return processAbnormalResult(taskInfo, result, strategy);
        }

        String endHandler = strategyNodeArrangement.get(taskInfo.getCurrentNode()).getName();
        Object resultIntegration = strategy.integrateResult(taskInfo.getResultMap(), endHandler);
        taskInfo.setTaskStatus(TaskStatusEnum.FINISHED.getCode());
        taskInfo.setResultIntegration(resultIntegration);
        recordTask(taskInfo, strategy);
        return new StatusResult<>(TaskStatusEnum.FINISHED.getCode(), resultIntegration);
    }

    private StatusResult processAbnormalResult(TaskInfoDto taskInfo, StatusResult result, BaseStrategy strategy) {
        UnnaturalEndTaskInfo endTaskInfo = new UnnaturalEndTaskInfo();
        endTaskInfo.setTaskId(taskInfo.getTaskId());
        endTaskInfo.setResult(result);
        String endHandler = strategy.getNodeArrangement().get(taskInfo.getCurrentNode()).getName();
        endTaskInfo.setAbnormalHandler(endHandler);
        if (BaseHandlerStatusEnum.EXCEPTION.getStatus().equals(result.getStatus())) {
            taskInfo.setTaskStatus(TaskStatusEnum.EXCEPTION.getCode());
            recordTask(taskInfo, strategy);
            return new StatusResult<>(TaskStatusEnum.EXCEPTION.getCode(), endTaskInfo);
        }
        if (BaseHandlerStatusEnum.FAIL.getStatus().equals(result.getStatus())) {
            taskInfo.setTaskStatus(TaskStatusEnum.FAIL.getCode());
            recordTask(taskInfo, strategy);
            return new StatusResult<>(TaskStatusEnum.FAIL.getCode(), endTaskInfo);
        }
        taskInfo.setTaskStatus(TaskStatusEnum.UNEXPECTED_STATUS.getCode());
        recordTask(taskInfo, strategy);
        return new StatusResult<>(TaskStatusEnum.UNEXPECTED_STATUS.getCode(), endTaskInfo);
    }

    private void recordTask(TaskInfoDto taskInfo, BaseStrategy strategy) throws TaskRecordException {
        lruCache.record(taskInfo);
        if (strategy instanceof TaskDurable) {
            boolean success = false;
            try {
                success = ((TaskDurable) strategy).recordTaskInfo(taskInfo);
            } catch (Exception e) {
                throw new TaskRecordException(taskInfo.getTaskId(), taskInfo.getStrategyName(), e);
            }
            if (!success) {
                throw new TaskRecordException(taskInfo.getTaskId(), taskInfo.getStrategyName(), TaskRecordException.Reason.RECORD_ERROR);
            }
        }
    }

    private TaskInfoDto getTaskInfo(Long taskId, BaseStrategy strategy, String strategyName) {
        TaskInfoDto taskInfo = null;
        taskInfo = lruCache.getTask(taskId);
        if (Objects.nonNull(taskInfo)) {
            return taskInfo;
        }
        if (strategy instanceof TaskDurable) {
            try {
                taskInfo = ((TaskDurable) strategy).getTaskInfo(taskId);
            } catch (Exception e) {
                throw new TaskRecordException(taskId, strategyName, e);
            }
        }
        if (Objects.isNull(taskInfo)) {
            throw new TaskRecordException(taskId, strategyName, TaskRecordException.Reason.GET_ERROR);
        }
        return taskInfo;
    }
}
