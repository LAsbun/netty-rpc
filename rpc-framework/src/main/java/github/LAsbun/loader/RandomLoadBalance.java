package github.LAsbun.loader;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

/**
 * Created by sws
 */
@Slf4j
public class RandomLoadBalance implements LoadBalance {
    /**
     * 选择ip. 负载均衡
     *
     * @param providerServerIpList 全部提供者
     * @return 某一个ip
     */
    @Override
    public String chooseBestIpAddress(List<String> providerServerIpList) {

        return providerServerIpList.get(new Random().nextInt(providerServerIpList.size()));
    }
}
