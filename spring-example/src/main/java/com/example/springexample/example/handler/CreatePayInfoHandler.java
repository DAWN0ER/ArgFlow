package com.example.springexample.example.handler;

import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import com.example.springexample.example.domain.GoodsOrderContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/09/11:43
 */
@Service
public class CreatePayInfoHandler implements FlowHandler<GoodsOrderContext, BigDecimal> {

    @Override
    public StatusResult<BigDecimal> handler(GoodsOrderContext goodsOrderContext) {
        BigDecimal sum = goodsOrderContext.getGoodsInfoList().stream()
                .map(e -> e.getPrice().multiply(new BigDecimal(e.getNum())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (Objects.equals(999, goodsOrderContext.getCouponId())) {
            sum = sum.multiply(new BigDecimal("0.5"));
            System.out.println(CreatePayInfoHandler.class.getSimpleName() + ":修改Context！");
        }
        goodsOrderContext.setPayId(999000999L);
        goodsOrderContext.setPayStatus(0);
        return new StatusResult<>(BaseHandlerStatusEnum.NEXT.getStatus(), sum);
    }

    @Override
    public Set<Integer> customStatus() {
        return null;
    }
}
