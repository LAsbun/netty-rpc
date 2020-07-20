package github.LAsbun.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import github.LAsbun.config.ThreadConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by sws
 * 这里对不同的接口由不同的线程池，后续可以支持不同的接口，不同的并发数->see Dubbo
 */
@Slf4j
public class ThreadPoolManagerServiceImpl implements ThreadPoolManagerService {

    private static Map<String, ExecutorService> executorServiceMap = new ConcurrentHashMap<>();

    /**
     * 获取接口对应的线程池
     *
     * @param interfaceName
     * @return
     */
    @Override
    public ExecutorService getExecutorService(String interfaceName) {

        return executorServiceMap.computeIfAbsent(interfaceName, k -> createThreadPool(interfaceName));
    }

    // 创建一个线程池。
    private ExecutorService createThreadPool(String interfaceName) {
        ThreadConfig threadConfig = new ThreadConfig();
        return new ThreadPoolExecutor(threadConfig.getCorePoolSize(), threadConfig.getMaximumPoolSize(),
                threadConfig.getKeepAliveTime(), threadConfig.getUnit(),
                new LinkedBlockingDeque<>(1024)
                , createThreadPoolFactory(interfaceName));

    }

    private ThreadFactory createThreadPoolFactory(String interfaceName) {
        return new ThreadFactoryBuilder().setNameFormat(interfaceName + "-%d").build();
    }

}
