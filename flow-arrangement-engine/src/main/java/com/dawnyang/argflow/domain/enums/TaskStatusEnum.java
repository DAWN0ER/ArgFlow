package com.dawnyang.argflow.domain.enums;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/04/20:15
 */
public enum TaskStatusEnum {
    INIT(0),
    RUNNING(1),
    FINISHED(2),
    WAIT(3),

    FAIL(-1),
    EXCEPTION(-2),
    UNEXPECTED_STATUS(-3)
    ;

    private final Integer code;

    TaskStatusEnum(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
