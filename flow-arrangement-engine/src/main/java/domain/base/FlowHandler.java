package domain.base;

import java.util.Set;

public interface FlowHandler<INPUT,OUTPUT> {

    StatusResult<OUTPUT> handler(INPUT input);
    Set<Integer> supportCustomStatus();

}
