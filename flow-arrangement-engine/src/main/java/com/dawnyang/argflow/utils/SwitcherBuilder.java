package com.dawnyang.argflow.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
public class SwitcherBuilder {

    private final Map<String, Map<Integer, String>> nameSwitcher;

    public SwitcherBuilder() {
        this.nameSwitcher = new HashMap<>();
    }

    public static SwitcherBuilder newBuilder() {
        return new SwitcherBuilder();
    }

    public Map<String, Map<Integer, String>> build() {
        return nameSwitcher;
    }

    public SwitcherBuilder addSwitcher(String name, int status, String target) {
        if (nameSwitcher.containsKey(name)) {
            nameSwitcher.get(name).put(status, target);
        } else {
            HashMap<Integer, String> tmpMap = new HashMap<>();
            tmpMap.put(status, target);
            nameSwitcher.put(name, tmpMap);
        }
        return this;
    }

}
