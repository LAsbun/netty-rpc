package github.LAsbun.transform.client;

import java.lang.reflect.InvocationHandler;

/**
 * Created by sws
 */

public interface ClientProxy extends InvocationHandler {

    /**
     * 获取动态代理类，
     *
     * @param clazz 代理类
     * @param <T>   实例类型
     * @return 实例
     */
    <T> T getProxy(Class<T> clazz);
}
