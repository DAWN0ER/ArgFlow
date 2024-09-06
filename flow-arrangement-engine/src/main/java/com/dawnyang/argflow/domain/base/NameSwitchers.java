package com.dawnyang.argflow.domain.base;

import org.apache.commons.collections.MapUtils;

import java.util.Map;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/06/18:19
 */
public class NameSwitchers {

    public static final String END_FLOW = "0_END_FLOW";

    private Map<String, Map<Integer,String>> nameMap;

    public Map<String, Map<Integer, String>> getNameMap() {
        return nameMap;
    }

    public void setNameMap(Map<String, Map<Integer, String>> nameMap) {
        this.nameMap = nameMap;
    }

    public static boolean haveSwitcher(NameSwitchers switchers){
        return Objects.nonNull(switchers) && MapUtils.isNotEmpty(switchers.getNameMap());
    }
}
