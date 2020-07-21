package github.LAsbun.transform.client;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * Created by sws
 * 客户端netty channel manager
 *
 * @create 2020-07-21 8:06 AM
 */

public interface ClientChannelProvider {

    /**
     * 根据地址获取对应的监听channel
     *
     * @param inetSocketAddress 服务端地址
     * @return 监听的channel
     */
    Channel get(InetSocketAddress inetSocketAddress);


    /**
     * 移除对应的channel
     *
     * @param inetSocketAddress 服务端地址
     */
    void remove(InetSocketAddress inetSocketAddress);
}
