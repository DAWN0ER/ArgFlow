package com.example.springexample.example.handler;

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
 * @Since: 2024/09/09/11:05
 */
@Service
public class ShopVerifyHandler implements FlowHandler<GoodsOrderContext,String> {
    @Override
    public StatusResult<String> handler(GoodsOrderContext goodsOrderContext) {
        if(Objects.nonNull(goodsOrderContext) && Objects.nonNull(goodsOrderContext.getShopId())){
            return new StatusResult<>(BaseHandlerStatusEnum.NEXT.getStatus(), "Have ShopId!");
        }
        return new StatusResult<>(BaseHandlerStatusEnum.FAIL.getStatus(), "No ShopId!");
    }

    @Override
    public Set<Integer> customStatus() {
        return null;
    }
}
