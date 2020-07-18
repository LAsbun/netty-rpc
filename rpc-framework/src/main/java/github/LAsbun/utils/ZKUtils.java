package github.LAsbun.utils;

import github.LAsbun.config.ZKConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sws
 */

@Slf4j
public final class ZKUtils {

    private static final CuratorFramework zkclient;

    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    private static final int ZK_BASE_SLEEP_TIME = 1000;

    private static final int ZK_MAX_RETRY = 5;

    private static Set<String> zkRegisteredServicePathSet = ConcurrentHashMap.newKeySet();
    private static Map<String, List<String>> providerIpAddressMap = new ConcurrentHashMap<>();


    static {
        zkclient = getZkClient();
    }

    // 获取zkClient
    private static CuratorFramework getZkClient() {

        // 重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(ZK_BASE_SLEEP_TIME, ZK_MAX_RETRY);

        // 创建一个客户端
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZK_ADDRESS)
                .retryPolicy(retryPolicy)
                .build();
        curatorFramework.start();
        return curatorFramework;

    }

    public static void registerService(String zkServicePath) {

        try {
            if (zkRegisteredServicePathSet.contains(zkServicePath) || zkclient.checkExists().forPath(zkServicePath) != null) {
                log.info("[{}] registerService 节点已经存在", zkServicePath);
            } else {
                log.info("[{}] registerService 节点不存在 需要注册", zkServicePath);
                // 创建zk 持久化节点
                zkclient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zkServicePath);
                log.info("[{}] registerService 注册成功", zkServicePath);

            }

            zkRegisteredServicePathSet.add(zkServicePath);

        } catch (Exception e) {
            log.info("[{}] registerService 发生错误:{}", zkServicePath, e);
        }

    }


    // 生成路径
    public static String generateZKServicePath(String rootPath, String serviceName, String ipPort) {
        return generateZKServicePathPrefix(rootPath, serviceName) + ipPort;
    }

    // zk 前缀
    public static String generateZKServicePathPrefix(String rootPath, String serviceName) {
        return rootPath + serviceName;
    }

    // 获取指定的list
    public static List<String> getProviderServiceIpList(String serviceName) {

        if (providerIpAddressMap.containsKey(serviceName)) {
            return providerIpAddressMap.get(serviceName);
        }

        List<String> providerIpList;
        String zkServicePathPrefix = generateZKServicePathPrefix(ZKConfig.ZK_ROOT_PATH, serviceName);
        try {
            providerIpList = zkclient.getChildren().forPath(zkServicePathPrefix);
            providerIpAddressMap.put(serviceName, providerIpList);
            // 这里是注册监听事件
            registerListener(serviceName);
        } catch (Exception e) {
            log.info("[{}] 获取zk节点信息异常", serviceName, e);
            providerIpList = new ArrayList<>();
        }

        return providerIpList;


    }


    /**
     * // 注册监听事件-->lookup
     *
     * @param serviceName
     */
    private static void registerListener(String serviceName) throws Exception {

        log.info("[{}] start registerListener", serviceName);
        String servicePathPrefix = generateZKServicePathPrefix(ZKConfig.ZK_ROOT_PATH, serviceName);
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkclient, servicePathPrefix, true);
        // 这个其实是，一直监听zk,如果节点变了，会自动更新providerIpAddressMap
        PathChildrenCacheListener listener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> strings = curatorFramework.getChildren().forPath(servicePathPrefix);
            providerIpAddressMap.put(serviceName, strings);
        };

        pathChildrenCache.getListenable().addListener(listener);

        pathChildrenCache.start();
        log.info("[{}] end registerListener", serviceName);


    }
}
