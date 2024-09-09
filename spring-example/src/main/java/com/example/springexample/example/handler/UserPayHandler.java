package com.example.springexample.example.handler;

import com.dawnyang.argflow.api.EnableWait;
import com.dawnyang.argflow.api.FlowHandler;
import com.dawnyang.argflow.domain.base.StatusResult;
import com.dawnyang.argflow.domain.enums.BaseHandlerStatusEnum;
import com.example.springexample.example.domain.UserPayContext;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description:
 * @Auther: Dawn Yang
 * @Since: 2024/09/09/11:09
 */
@Service
public class UserPayHandler implements FlowHandler<UserPayContext, String>, EnableWait<UserPayContext, String> {
    @Override
    public StatusResult<String> handler(UserPayContext context) {
        Long payId = context.getPayId();
        Integer userId = context.getUserId();
        if (userId % 7 == 0){
            return new StatusResult<>(BaseHandlerStatusEnum.NEXT.getStatus(), "%7 免密支付:"+payId);
        }
        return new StatusResult<>(BaseHandlerStatusEnum.WAIT.getStatus(), payId.toString());
    }

    @Override
    public Set<Integer> customStatus() {
        return null;
    }

    @Override
    public StatusResult<String> waitFor(UserPayContext userPayContext) {
        Integer payStatus = userPayContext.getPayStatus();
        if(Objects.equals(payStatus,1)){
            return new StatusResult<>(BaseHandlerStatusEnum.NEXT.getStatus(), "支付成功！");
        }
        return new StatusResult<>(BaseHandlerStatusEnum.FAIL.getStatus(), "支付失败！");
    }
}
