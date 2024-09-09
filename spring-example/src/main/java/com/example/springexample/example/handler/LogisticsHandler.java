package com.example.springexample.example.handler;

import com.dawnyang.argflow.api.EnableWait;
import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import com.example.springexample.example.domain.GoodsOrderContext;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/09/11:12
 */
@Service
public class LogisticsHandler implements FlowHandler<GoodsOrderContext, String>, EnableWait<GoodsOrderContext, String> {

    @Override
    public StatusResult<String> handler(GoodsOrderContext context) {
        String address = context.getAddress();
        context.setLogisticsStatus(0);
        return new StatusResult<>(BaseHandlerStatusEnum.WAIT.getStatus(), address);
    }

    @Override
    public Set<Integer> customStatus() {
        return null;
    }

    @Override
    public StatusResult<String> waitFor(GoodsOrderContext goodsOrderContext) {
        if (Objects.nonNull(goodsOrderContext) && Objects.equals(1, goodsOrderContext.getLogisticsStatus())){
            return new StatusResult<>(BaseHandlerStatusEnum.NEXT.getStatus(), "商家已经发货");
        }
        return new StatusResult<>(BaseHandlerStatusEnum.FAIL.getStatus(), "商家拒绝发货");
    }
}
