package github.LAsbun.transform.client.impl;

import github.LAsbun.entity.RPCRequest;
import github.LAsbun.entity.RPCResponse;
import github.LAsbun.register.ServiceDiscover;
import github.LAsbun.register.ServiceDiscoverImpl;
import github.LAsbun.transform.client.ClientChannelProvider;
import github.LAsbun.transform.client.ClientMessageProcessService;
import github.LAsbun.transform.client.ClientTransport;
import github.LAsbun.utils.SingletonFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * Created by sws
 *
 * @create 2020-07-21 11:24 PM
 */
@Slf4j
public class ClientTransportImpl implements ClientTransport {

    private final ServiceDiscover serviceDiscover;
    private final ClientChannelProvider channelProvider;
    private final ClientMessageProcessService messageProcessService;

    public ClientTransportImpl() {
        serviceDiscover = SingletonFactory.getInstance(ServiceDiscoverImpl.class);
        channelProvider = SingletonFactory.getInstance(ClientChannelProviderImpl.class);
        messageProcessService = SingletonFactory.getInstance(ClientMessageProcessServiceImpl.class);
    }

    @Override
    public CompletableFuture<RPCResponse> sendRequest(RPCRequest rpcRequest) {

        String requestId = rpcRequest.getRequestId();
        log.info("[{}] start sendRequest", requestId);

        InetSocketAddress inetSocketAddress = serviceDiscover.lookup(rpcRequest.getInterfaceName());
        if (null == inetSocketAddress) {
            log.info("[{}] 没有找到服务提供方", requestId);
            throw new IllegalStateException("没有找到服务提供方");
        }
        CompletableFuture<RPCResponse> future = new CompletableFuture<>();

        Channel channel = channelProvider.get(inetSocketAddress);
        if (null != channel && channel.isActive()) {
            //存储未返回的future
            messageProcessService.put(requestId, future);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    log.info("[{}] success send request", requestId);
                } else {
                    log.info("[{}] fail send request:{}", requestId, channelFuture.cause());
                    channelFuture.channel().close();
                    future.completeExceptionally(channelFuture.cause());
                }
            });
        } else {
            throw new IllegalStateException("channel 获取失败");
        }

        return future;
    }
}
