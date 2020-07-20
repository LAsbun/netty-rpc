package github.LAsbun.handler;

import github.LAsbun.entity.RPCRequest;

/**
 * Created by sws
 */

public interface RPCRequestHandler {

    /**
     * 处理请求，并返回
     *
     * @param rpcRequest 请求参数
     * @return 处理结果
     */
    Object handle(RPCRequest rpcRequest);
}
