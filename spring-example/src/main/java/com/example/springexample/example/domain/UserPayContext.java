package com.example.springexample.example.domain;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/09/10:36
 */
@Data
public class UserPayContext {
    private Integer userId;
    private String userName;
    private String fingerprint;
    private Long payId;
    private Integer payStatus;
}
