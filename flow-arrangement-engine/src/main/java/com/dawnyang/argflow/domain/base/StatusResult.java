package com.dawnyang.argflow.domain.base;

import lombok.Data;

@Data
public class StatusResult<T> {

    private Integer status;
    private T data;
}
