package github.LAsbun.transform.client.impl;

import github.LAsbun.entity.RPCResponse;
import github.LAsbun.transform.client.ClientMessageProcessService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sws
 *
 * @create 2020-07-21 11:09 PM
 */
@Slf4j
public class ClientMessageProcessServiceImpl implements ClientMessageProcessService {

    // 保存等待结果的channel
    public static Map<String, CompletableFuture<RPCResponse>> futureMap = new ConcurrentHashMap();

    @Override
    public void put(String requestId, CompletableFuture<RPCResponse> future) {

        futureMap.put(requestId, future);
    }

    @Override
    public void complete(RPCResponse rpcResponse) {

        CompletableFuture<RPCResponse> completableFuture = futureMap.get(rpcResponse.getRequestId());
        if (null != completableFuture) {
            completableFuture.complete(rpcResponse);
        } else {
            throw new IllegalStateException("不存在的requestFuture:" + rpcResponse.getRequestId());
        }
    }
}
