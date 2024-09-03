package com.dawnyang.argflow.utils;

import com.dawnyang.argflow.action.FlowHandler;
import com.dawnyang.argflow.domain.base.BaseStatus;
import com.dawnyang.argflow.domain.base.StrategyNode;
import com.dawnyang.argflow.domain.exception.WrongSwitcherException;
import com.dawnyang.argflow.domain.exception.NoHandlerException;
import org.apache.commons.collections.MapUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
public class StrategyNodesBuilder {

    private ArrayList<StrategyNode> nodes;
    private Map<String, Integer> node2order;

    public static StrategyNodesBuilder newBuilder() {
        return new StrategyNodesBuilder();
    }

    public StrategyNodesBuilder sequenceHandler(String[] names, Map<String, FlowHandler> name2handler) throws NoHandlerException {
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

    public StrategyNodesBuilder initSwitchers(Map<String, Map<Integer, String>> nameSwitchers) throws NoHandlerException, WrongSwitcherException {
        if (MapUtils.isEmpty(nameSwitchers)) {
            return this;
        }
        nameSwitchers.forEach((name, map) -> {
            if (MapUtils.isEmpty(map)) {
                return;
            }
            Integer order = node2order.get(name);
            if (Objects.isNull(order)) {
                throw new NoHandlerException(name);
            }
            // 验证 Switcher 是结果集的子集
            FlowHandler<?,?> handler = nodes.get(order).getHandler();
            Set<Integer> outSet = Optional.ofNullable(handler.customStatus()).orElse(new HashSet<>());
            outSet.add(BaseStatus.SUCCESS.getStatus()); // 默认成功也要加上
            Set<Integer> transferSet = map.keySet();
            if(!outSet.containsAll(transferSet)){
                throw new WrongSwitcherException(name,null, WrongSwitcherException.WrongType.WRONG_STATUS);
            }
            HashMap<Integer, Integer> switcher = new HashMap<>();
            map.forEach((status, target) -> {
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
