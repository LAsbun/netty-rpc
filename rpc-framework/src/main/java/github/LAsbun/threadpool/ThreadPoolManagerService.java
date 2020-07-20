package github.LAsbun.threadpool;

import java.util.concurrent.ExecutorService;

/**
 * Created by sws
 */

public interface ThreadPoolManagerService {


    /**
     * 获取接口对应的线程池
     *
     * @param interfaceName
     * @return
     */
    ExecutorService getExecutorService(String interfaceName);
}
