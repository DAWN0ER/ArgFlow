package com.dawnyang.argflow.domain.base;

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
public enum BaseStatus {
    SUCCESS(1),
    FAIL(0),
    EXCEPTION(-1),
    WAIT(2)
    ;
    private final Integer status;
}
