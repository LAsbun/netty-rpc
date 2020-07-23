package github.LAsbun.transform.server;

import github.LAsbun.entity.RPCRequest;
import github.LAsbun.entity.RPCResponse;
import github.LAsbun.provider.ServiceProvider;
import github.LAsbun.provider.ServiceProviderImpl;
import github.LAsbun.register.ServiceRegister;
import github.LAsbun.register.ServiceRegisterImpl;
import github.LAsbun.serialize.FastjsonSerializerImpl;
import github.LAsbun.serialize.Serializer;
import github.LAsbun.transform.codec.RPCDecoder;
import github.LAsbun.transform.codec.RPCEncoder;
import github.LAsbun.utils.SingletonFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.net.InetSocketAddress;

/**
 * Created by sws
 * 注册相关服务
 *
 * @create 2020-07-20 7:24 AM
 */
@Slf4j
public class NettyServer {

    private ServiceRegister serviceRegister;

    private ServiceProvider serviceProvider;

    private String address;

    private int port;

    private Serializer serializer;

    public NettyServer(String address, int port) {
        this.port = port;
        this.address = address;
        this.serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
        this.serviceRegister = SingletonFactory.getInstance(ServiceRegisterImpl.class);
        this.serializer = new FastjsonSerializerImpl();

    }

    // 注册服务
    public <T> void publishService(T service, Class<T> serviceClazz) {
        serviceProvider.addService(service, serviceClazz);
        serviceRegister.registerService(serviceClazz.getCanonicalName(), new InetSocketAddress(address, port));
    }

    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    // 系统临时存放完成三次握手的请求队列最大长度
                    .option(ChannelOption.SO_BACKLOG, 2014)
                    // 默认尽可能发送大数据块，减少网络传输
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 开启TCP底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 初始化
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //  这里是开启空闲检测，
                            socketChannel.pipeline().addLast(new IdleStateHandler(3, 0, 0));
                            socketChannel.pipeline().addLast(new RPCDecoder(serializer, RPCRequest.class));
                            socketChannel.pipeline().addLast(new RPCEncoder(serializer, RPCResponse.class));
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(address, port).sync();
            //等待服务端端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.info("启动服务异常", e);
        } finally {
            log.info("shutdown server");
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }
}
