package com.dawnyang.argflow.domain.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BaseStatus {
    SUCCESS(1),
    FAIL(0),
    EXCEPTION(-1),
    WAIT(2),
    ;
    private final Integer status;
}
