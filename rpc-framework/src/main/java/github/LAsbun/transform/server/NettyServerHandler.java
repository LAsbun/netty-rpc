package github.LAsbun.transform.server;

import github.LAsbun.entity.RPCRequest;
import github.LAsbun.entity.RPCResponse;
import github.LAsbun.entity.enums.RPCMessageTypeEnum;
import github.LAsbun.entity.enums.RPCResponseCodeEnum;
import github.LAsbun.handler.RPCRequestHandler;
import github.LAsbun.handler.RPCRequestHandlerImpl;
import github.LAsbun.threadpool.ThreadPoolManagerService;
import github.LAsbun.threadpool.ThreadPoolManagerServiceImpl;
import github.LAsbun.utils.SingletonFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by sws
 *
 * @create 2020-07-20 7:45 AM
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final ThreadPoolManagerService poolManager;

    private final RPCRequestHandler rpcRequestHandler;

    public NettyServerHandler() {
        log.info("[NettyServerHandler] NettyServerHandler 初始化了");
        // 这里进行初始化
        this.poolManager = SingletonFactory.getInstance(ThreadPoolManagerServiceImpl.class);
        this.rpcRequestHandler = SingletonFactory.getInstance(RPCRequestHandlerImpl.class);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("[NettyServerHandler] received a message:{}", msg);
        RPCRequest rpcRequest = (RPCRequest) msg;

        if (RPCMessageTypeEnum.HEART_BEAT.equals(rpcRequest.getRpcMessageTypeEnum())) {
            log.info("[NettyServerHandler] receive hearbeat");
            return;
        }

        poolManager.getExecutorService(rpcRequest.getInterfaceName()).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("[{}] start invoke", rpcRequest.getRequestId());
                    Object result = rpcRequestHandler.handle(rpcRequest);
                    log.info("[{}] end invoke", rpcRequest.getRequestId());

                    if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                        log.info("[{}] is writable drop", rpcRequest.getRequestId());
                        RPCResponse<Object> success = RPCResponse.success(result, rpcRequest.getRequestId());
                        // 如果失败了，就把channel关闭?
                        ctx.writeAndFlush(success).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                    } else {
                        log.info("[{}] not writable drop", rpcRequest.getRequestId());
                    }
                    // todo 这里失败了要怎么处理？
                } finally {
                    // 释放byteBuf 不然会有内存泄露的风险
                    ReferenceCountUtil.release(msg);
                }

            }
        });
        super.channelRead(ctx, msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("[NettyServerHandler] caught error");
        cause.printStackTrace();
        ctx.close();
    }
}
