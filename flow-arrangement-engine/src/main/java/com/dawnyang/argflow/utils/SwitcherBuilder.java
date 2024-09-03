package com.dawnyang.argflow.utils;

import java.util.HashMap;
import java.util.Map;

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
