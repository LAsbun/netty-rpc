package github.LAsbun.transform.client;

import github.LAsbun.entity.RPCResponse;
import github.LAsbun.transform.client.impl.ClientMessageProcessServiceImpl;
import github.LAsbun.utils.SingletonFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

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

    public NettyClientHandler() {
        this.processService = SingletonFactory.getInstance(ClientMessageProcessServiceImpl.class);
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
        // note:处理超时, 目前没有做心跳机制，所以暂时不用实现
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client error");
        cause.printStackTrace();
        ctx.close();
    }
}
