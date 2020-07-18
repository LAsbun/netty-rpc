package github.LAsbun.register;

import github.LAsbun.loader.LoadBalance;
import github.LAsbun.loader.RandomLoadBalance;
import github.LAsbun.utils.ZKUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by sws
 */

@Slf4j
public class ServiceDiscoverImpl implements ServiceDiscover {

    private final LoadBalance loadBalance;

    public ServiceDiscoverImpl() {
        this.loadBalance = new RandomLoadBalance();
    }

    /**
     * 找到提供服务的ip:port
     *
     * @param serviceName 服务名称
     * @return 找到提供服务的ip:port
     */
    @Override
    public InetSocketAddress lookup(String serviceName) {

        log.info("[{}] start loopup provider", serviceName);

        List<String> providerList = ZKUtils.getProviderServiceIpList(serviceName);

        log.info("[{}] start loopup provider size:{}", serviceName, providerList.size());

        if (providerList.size() == 0) {
            log.info("[{}] 没有找到provider", serviceName);
            return null;
        }

        String ipAddress = this.loadBalance.chooseBestIpAddress(providerList);

        String[] split = ipAddress.split(":");

        return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
    }
}
