package github.LAsbun.transform.client;

import github.LAsbun.entity.RPCRequest;
import github.LAsbun.entity.RPCResponse;
import github.LAsbun.serialize.FastjsonSerializerImpl;
import github.LAsbun.serialize.Serializer;
import github.LAsbun.transform.codec.RPCDecoder;
import github.LAsbun.transform.codec.RPCEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * Created by sws
 *
 * @create 2020-07-21 10:48 PM
 */
@Slf4j
public final class NettyClient {

    private static Bootstrap bootstrap;

    private static EventLoopGroup eventLoopGroup;

    static {

        Serializer serializer = new FastjsonSerializerImpl();

        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new RPCDecoder(serializer, RPCResponse.class));
                        socketChannel.pipeline().addLast(new RPCEncoder(serializer, RPCRequest.class));
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
    }

    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> channelCompleteFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("[doConnect] 客户端连接成功:{}", inetSocketAddress.toString());
                channelCompleteFuture.complete(future.channel());
            } else {
                log.info("[doConnetc] 客户端连接失败:{}", inetSocketAddress.toString());
                throw new IllegalStateException();
            }
        });
        return channelCompleteFuture.get();
    }

    // 安全的关闭eventLoop
    public void close() {
        log.info("client eventloop close");
        eventLoopGroup.shutdownGracefully();
    }
}
