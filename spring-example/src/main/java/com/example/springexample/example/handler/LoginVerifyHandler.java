package com.example.springexample.example.handler;

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
 * @Since: 2024/09/09/11:03
 */
@Service
public class LoginVerifyHandler implements FlowHandler<UserPayContext,String> {

    @Override
    public StatusResult<String> handler(UserPayContext userPayContext) {
        if (Objects.nonNull(userPayContext) && "login".equals(userPayContext.getFingerprint())){
            return new StatusResult<>(BaseHandlerStatusEnum.NEXT.getStatus(), "Online!");
        }
        return new StatusResult<>(BaseHandlerStatusEnum.FAIL.getStatus(), "Offline!");
    }

    @Override
    public Set<Integer> customStatus() {
        return null;
    }
}
