package com.dawnyang.argflow.domain.base;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/03/20:26
 */
@Data
@AllArgsConstructor
public class StatusResult<T> {

    private Integer status;
    private T data;
}
