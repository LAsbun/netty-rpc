package github.LAsbun.transform.client;

import github.LAsbun.entity.RPCResponse;

import java.util.concurrent.CompletableFuture;

/**
 * Created by sws
 * <p>
 * 管理client 发出去与接收回来的message
 */

public interface ClientMessageProcessService {

    /**
     * 临时保存请求后的future
     *
     * @param requestId 请求的唯一Id
     * @param future    结果
     */
    void put(String requestId, CompletableFuture<RPCResponse> future);

    void complete(RPCResponse rpcResponse);
}
