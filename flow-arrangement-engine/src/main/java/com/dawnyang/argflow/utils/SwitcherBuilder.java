package com.dawnyang.argflow.utils;

import com.dawnyang.argflow.domain.base.NameSwitchers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: 分支器 Builder
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
public class SwitcherBuilder {

    private final Map<String, Map<Integer, String>> nameMap = new HashMap<>();

    public static SwitcherBuilder newBuilder() {
        return new SwitcherBuilder();
    }

    public NameSwitchers build() {
        NameSwitchers nameSwitchers = new NameSwitchers();
        nameSwitchers.setNameMap(nameMap);
        return nameSwitchers;
    }

    public SwitcherBuilder addSwitcher(String name, int status, String target) {
        if (nameMap.containsKey(name)) {
            nameMap.get(name).put(status, target);
        } else {
            HashMap<Integer, String> tmpMap = new HashMap<>();
            tmpMap.put(status, target);
            nameMap.put(name, tmpMap);
        }
        return this;
    }

}
