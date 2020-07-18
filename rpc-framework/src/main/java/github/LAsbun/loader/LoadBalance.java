package github.LAsbun.loader;

import java.util.List;

/**
 * Created by sws
 */

public interface LoadBalance {

    /**
     * 选择ip. 负载均衡
     *
     * @param providerServerIpList 全部提供者
     * @return 某一个ip
     */
    String chooseBestIpAddress(List<String> providerServerIpList);

}
