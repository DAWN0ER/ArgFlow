package com.example.springexample.example.handler;

import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import com.example.springexample.example.domain.GoodsOrderContext;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/09/11:07
 */
@Service
public class GetWarehouseInfoHandler implements FlowHandler<GoodsOrderContext,String> {
    @Override
    public StatusResult<String> handler(GoodsOrderContext goodsOrderContext) {
        Integer shopId = goodsOrderContext.getShopId();
        switch (shopId%2){
            case 0:
                return new StatusResult<>(Status.PRIVATE.code, shopId + "==private warehouse");
            case 1:
                return new StatusResult<>(Status.PLATFORM.code, shopId + "==platform public warehouse");
        }
        return new StatusResult<>(BaseHandlerStatusEnum.EXCEPTION.getStatus(), "No warehouse!");
    }

    @Override
    public Set<Integer> customStatus() {
        return Arrays.stream(Status.values()).map(Status::getCode).collect(Collectors.toSet());
    }

    @Getter
    public enum Status{
        PRIVATE(1000),
        PLATFORM(2000)
        ;
        public int code;
        Status(int code){
            this.code = code;
        }
    }
}
