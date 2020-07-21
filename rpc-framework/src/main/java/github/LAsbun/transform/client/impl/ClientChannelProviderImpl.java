package github.LAsbun.transform.client.impl;

import github.LAsbun.transform.client.ClientChannelProvider;
import github.LAsbun.transform.client.NettyClient;
import github.LAsbun.utils.SingletonFactory;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import sun.security.krb5.internal.NetClient;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sws
 *
 * @create 2020-07-21 11:24 PM
 */
@Slf4j
public class ClientChannelProviderImpl implements ClientChannelProvider {

    private static Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    private static NettyClient nettyClient;

    public ClientChannelProviderImpl() {
        nettyClient = SingletonFactory.getInstance(NettyClient.class);
    }

    @Override
    public Channel get(InetSocketAddress inetSocketAddress) {
        String ipAddress = inetSocketAddress.toString();
        log.info("[{}] start get channel", ipAddress);
        // 如果channel 可用
        if (channelMap.containsKey(ipAddress)) {
            Channel channel = channelMap.get(ipAddress);
            if (null != channel && channel.isActive()) {
                return channel;
            } else {
                remove(inetSocketAddress);
            }
        }

        Channel channel = nettyClient.doConnect(inetSocketAddress);
        channelMap.put(ipAddress, channel);

        return channel;
    }

    @Override
    public void remove(InetSocketAddress inetSocketAddress) {
        String ipAddress = inetSocketAddress.toString();
        log.info("[{}] remove channel", ipAddress);
        channelMap.remove(ipAddress);
        log.info("[{}] remove channel done", ipAddress);

    }
}
