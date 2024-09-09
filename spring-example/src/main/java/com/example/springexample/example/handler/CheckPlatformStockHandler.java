package com.example.springexample.example.handler;

import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.example.springexample.example.domain.GoodsInfo;
import com.example.springexample.example.domain.GoodsOrderContext;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/09/11:09
 */
@Service
public class CheckPlatformStockHandler implements FlowHandler<GoodsOrderContext,String> {
    @Override
    public StatusResult<String> handler(GoodsOrderContext goodsOrderContext) {
        List<GoodsInfo> goodsInfoList = goodsOrderContext.getGoodsInfoList();
        Gson gson = new Gson();
        String msg = this.getClass().getSimpleName() + gson.toJson(goodsInfoList);
        return new StatusResult<>(99, msg);
    }

    @Override
    public Set<Integer> customStatus() {
        HashSet<Integer> set = new HashSet<>();
        set.add(99);
        return set;
    }
}
