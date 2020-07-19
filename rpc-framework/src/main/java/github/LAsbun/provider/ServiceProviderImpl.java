package github.LAsbun.provider;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sws
 *
 * @create 2020-07-20 6:51 AM
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    Set<String> registeredServiceSet = ConcurrentHashMap.newKeySet();

    /**
     * 注册本地的服务
     *
     * @param service      服务实例(这里注册的都是单例，后面可以使用动态代理)
     * @param serviceClass 服务对应的class
     */
    @Override
    public <T> void addService(T service, Class<T> serviceClass) {
        String canonicalName = serviceClass.getCanonicalName();
        if (registeredServiceSet.contains(canonicalName)) {
            log.info("[{}] 已经注册过", canonicalName);
            return;
        }

        registeredServiceSet.add(canonicalName);
        serviceMap.put(canonicalName, service);
    }

    @Override
    public Object getService(String serviceName) {
        //todo 没有获取到，抛出异常?
        return serviceMap.get(serviceName);
    }
}
