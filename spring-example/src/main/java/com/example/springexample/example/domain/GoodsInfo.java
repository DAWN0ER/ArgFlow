package com.example.springexample.example.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/09/10:54
 */
@Data
public class GoodsInfo {
    private Long goodsId;
    private String goodsName;
    private String description;
    private BigDecimal price;
    private Integer num;
}
