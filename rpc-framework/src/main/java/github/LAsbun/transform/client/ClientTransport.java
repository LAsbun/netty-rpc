package github.LAsbun.transform.client;

import github.LAsbun.entity.RPCRequest;
import github.LAsbun.entity.RPCResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Created by sws
 * 客户端请求接口
 *
 * @create 2020-07-21 7:57 AM
 */

public interface ClientTransport {

    /**
     * 发送请求
     *
     * @param rpcRequest 请求主体
     * @return 服务端响应数据
     */
    CompletableFuture<RPCResponse> sendRequest(RPCRequest rpcRequest);
}
