package github.LAsbun.register;

import java.net.InetSocketAddress;

/**
 * Created by sws
 * 发现服务
 */

public interface ServiceDiscover {

    /**
     * 找到提供服务的ip:port
     *
     * @param serviceName 服务名称
     * @return 找到提供服务的ip:port
     */
    InetSocketAddress lookup(String serviceName);
}
