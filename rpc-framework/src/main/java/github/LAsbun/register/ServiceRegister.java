package github.LAsbun.register;

import java.net.InetSocketAddress;

/**
 * Created by sws
 * 服务注册类
 */

public interface ServiceRegister {

    /**
     * 注册服务
     *
     * @param serviceName       服务名称
     * @param inetSocketAddress 服务地址
     */
    void registerService(String serviceName, InetSocketAddress inetSocketAddress);


}
