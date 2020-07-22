package github.LAsbun.transform.client.impl;

import github.LAsbun.entity.RPCRequest;
import github.LAsbun.entity.RPCResponse;
import github.LAsbun.entity.enums.RPCMessageTypeEnum;
import github.LAsbun.transform.client.ClientProxy;
import github.LAsbun.transform.client.ClientTransport;
import github.LAsbun.utils.RPCCheckUtils;
import github.LAsbun.utils.SingletonFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Created by sws
 */
@Slf4j
public class ClientProxyImpl implements ClientProxy {

    private final ClientTransport clientTransport;

    public ClientProxyImpl() {
        this.clientTransport = SingletonFactory.getInstance(ClientTransportImpl.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RPCRequest rpcRequest = RPCRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .rpcMessageTypeEnum(RPCMessageTypeEnum.RPC_SERVICE)
                .interfaceName(method.getDeclaringClass().getName()) // 这里是用method, 因为此时的proxy是一个代理实例
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();

        CompletableFuture<RPCResponse> completableFuture = clientTransport.sendRequest(rpcRequest);

        RPCResponse rpcResponse = completableFuture.get();

        RPCCheckUtils.checkRequestAndResponse(rpcRequest, rpcResponse);

        return rpcResponse;
    }
}
