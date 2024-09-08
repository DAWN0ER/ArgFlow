package com.dawnyang.argflow.utils;

import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.domain.base.NameSwitchers;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import com.dawnyang.argflow.domain.base.StrategyNode;
import com.dawnyang.argflow.domain.exception.StrategyException;
import com.dawnyang.argflow.domain.exception.strategy.WrongSwitcherException;
import com.dawnyang.argflow.domain.exception.strategy.NoHandlerException;
import org.apache.commons.collections.MapUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: 策略节点编排 Builder
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
public class StrategyArrangementBuilder {

    private ArrayList<StrategyNode> nodes;
    private Map<String, Integer> node2order;

    public static StrategyArrangementBuilder newBuilder() {
        return new StrategyArrangementBuilder();
    }

    public StrategyArrangementBuilder sequenceHandler(String[] names, Map<String, FlowHandler> name2handler) throws StrategyException {
        ArrayList<StrategyNode> tmpList = new ArrayList<>();
        Map<String, Integer> tmpMap = new HashMap<>();
        for (int order = 0; order < names.length; order++) {
            String name = names[order];
            FlowHandler handler = name2handler.get(name);
            if (Objects.isNull(handler)) {
                throw new NoHandlerException(name);
            }
            StrategyNode strategyNode = new StrategyNode(name, handler, null);
            tmpList.add(strategyNode);
            tmpMap.put(name, order);
        }
        this.nodes = tmpList;
        this.node2order = tmpMap;
        return this;
    }

    public StrategyArrangementBuilder initSwitchers(NameSwitchers nameSwitchers) throws StrategyException {
        if (!NameSwitchers.haveSwitcher(nameSwitchers)) {
            return this;
        }
        Map<String, Map<Integer, String>> nameMap = nameSwitchers.getNameMap();
        nameMap.forEach((name, map) -> {
            if (MapUtils.isEmpty(map)) {
                return;
            }
            Integer order = node2order.get(name);
            if (Objects.isNull(order)) {
                throw new NoHandlerException(name);
            }

            // 验证 switcher 的 status 是结果集的子集
            FlowHandler<?,?> handler = nodes.get(order).getHandler();
            Set<Integer> outSet = Optional.ofNullable(handler.customStatus()).orElse(new HashSet<>());
            List<Integer> baseStatus = Arrays.stream(BaseHandlerStatusEnum.values())
                    .map(BaseHandlerStatusEnum::getStatus)
                    .collect(Collectors.toList());
            outSet.addAll(baseStatus);
            Set<Integer> transferSet = map.keySet();
            if(!outSet.containsAll(transferSet)){
                throw new WrongSwitcherException(name,null, WrongSwitcherException.WrongType.WRONG_STATUS);
            }
            // 初始化 switcher
            HashMap<Integer, Integer> switcher = new HashMap<>();
            map.forEach((status, target) -> {

                if (NameSwitchers.END_FLOW.equals(target)){
                    switcher.put(status,-1); // -1 表示流程结束
                    return;
                }
                Integer targetOrder = node2order.get(target);
                if (Objects.isNull(targetOrder)) {
                    throw new NoHandlerException(target);
                }
                // 判断是否成环
                if (targetOrder <= order) {
                    throw new WrongSwitcherException(name, target, WrongSwitcherException.WrongType.LOOP);
                }
                switcher.put(status, targetOrder);
            });
            nodes.get(order).setSwitcher(switcher);
        });
        return this;
    }

    public ArrayList<StrategyNode> build() {
        return nodes;
    }
}
