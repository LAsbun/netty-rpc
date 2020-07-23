package github.LAsbun.transform.client;

import github.LAsbun.entity.RPCRequest;
import github.LAsbun.entity.RPCResponse;
import github.LAsbun.entity.enums.RPCMessageTypeEnum;
import github.LAsbun.transform.client.impl.ClientChannelProviderImpl;
import github.LAsbun.transform.client.impl.ClientMessageProcessServiceImpl;
import github.LAsbun.utils.SingletonFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * Created by sws
 * <p>
 * 职责：
 * 1 消费response
 * 2 处理超时channel
 *
 * @create 2020-07-21 11:03 PM
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private final ClientMessageProcessService processService;

    private final ClientChannelProvider clientChannelProvider;

    public NettyClientHandler() {
        this.processService = SingletonFactory.getInstance(ClientMessageProcessServiceImpl.class);
        this.clientChannelProvider = SingletonFactory.getInstance(ClientChannelProviderImpl.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            RPCResponse rpcResponse = (RPCResponse) msg;

            log.info("[{}] client received msg", rpcResponse.getRequestId());

            // 表示接受到了服务端返回的数据
            processService.complete(rpcResponse);
        } finally {
            // 手动释放掉，防止内存泄露
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            log.info(" state");
            RPCRequest rpcRequest = RPCRequest.builder().rpcMessageTypeEnum(RPCMessageTypeEnum.HEART_BEAT).build();
            Channel channel = clientChannelProvider.get((InetSocketAddress) ctx.channel().remoteAddress());
            channel.writeAndFlush(rpcRequest);
            log.info("state send done");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client error");
        cause.printStackTrace();
        ctx.close();
    }
}
