package github.LAsbun.register;

import github.LAsbun.config.ZKConfig;
import github.LAsbun.utils.ZKUtils;

import java.net.InetSocketAddress;

/**
 * Created by sws
 */

public class ServiceRegisterImpl implements ServiceRegister {
    /**
     * 注册服务
     *
     * @param serviceName       服务名称
     * @param inetSocketAddress 服务地址
     */
    @Override
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {

        String zkServicePath = ZKUtils.generateZKServicePath(ZKConfig.ZK_ROOT_PATH, serviceName, inetSocketAddress.toString());

        ZKUtils.registerService(zkServicePath);
    }
}
