package com.example.springexample.example.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/09/10:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsOrderContext extends UserPayContext {
    private Integer shopId;
    private String shopName;
    private List<GoodsInfo> goodsInfoList;
    private String shopSnapshot;
    private String address;
    private Integer couponId;
    private Integer logisticsStatus;
}
