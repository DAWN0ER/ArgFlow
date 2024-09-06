package com.dawnyang.argflow.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
@AllArgsConstructor
@Getter
public enum BaseHandlerStatusEnum {

    /** 优先级高于 sNameSwitcher **/
    NEXT(1),
    WAIT(2),

    /**
     * 优先级低于 sNameSwitcher
     * 默认流程给结束，输出当前 Handler 节点的结果
     */
    FAIL(0),
    EXCEPTION(-1)
    ;

    private final Integer status;
}
